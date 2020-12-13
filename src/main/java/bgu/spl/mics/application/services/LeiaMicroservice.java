package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;


import bgu.spl.mics.TerminateBattle;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BombEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.EndOfAttackEvent;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;


/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
    private Attack[] attacks;
    private Future[] futures;

    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
        this.attacks = attacks;
        futures = new Future[attacks.length];
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TerminateBattle.class, (event) -> {
            terminate();
            //System.out.println("Leia : terminated");
        });


        //sleep so the others subscribe
        try {
            // TODO: 14/12/2020 delete
            // Thread.sleep(1000);
            Main.latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //send all attack object as attackevents
        for (int i = 0; i < attacks.length; i++) {
            //     for (Attack a : attacks) {
            Attack nextAttack = attacks[i];
            Future<Boolean> future = sendEvent(new AttackEvent(nextAttack.getSerials(), nextAttack.getDuration()));
            // TODO: 14/12/2020 remove while since latch?
            while (future == null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                future = sendEvent(new AttackEvent(nextAttack.getSerials(), nextAttack.getDuration()));
            }
            futures[i] = future;
        }
        //check that attacks finished
        for (int i = 0; i < futures.length; i++) {
            futures[i].get();
        }
        //send deactivation event and bomb event after
        Future<Boolean> deactivationFuture = sendEvent(new DeactivationEvent());
        //System.out.println("Leia : deactivation event sent");
        if (deactivationFuture != null) {
            deactivationFuture.get();
            sendEvent(new BombEvent());
            //System.out.println("Leia : bombevent event sent");
        }
    }

    @Override
    protected void close() {
        Diary.getInstance().setLeiaTerminate(System.currentTimeMillis());    //todo added
    }
}
