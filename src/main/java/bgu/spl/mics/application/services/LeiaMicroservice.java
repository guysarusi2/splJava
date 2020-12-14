package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;


import bgu.spl.mics.application.messages.TerminateBattle;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BombEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
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
        });

        //sleep so the others subscribe
        try {
            Main.latch.await();
        } catch (InterruptedException e) {
        }

        //send all attack object as attackevents
        for (int i = 0; i < attacks.length; i++) {
            Attack nextAttack = attacks[i];
            Future<Boolean> future = sendEvent(new AttackEvent(nextAttack.getSerials(), nextAttack.getDuration()));
            futures[i] = future;
        }
        //check that attacks finished
        for (int i = 0; i < futures.length; i++) {
            futures[i].get();
        }
        //send deactivation event
        Future<Boolean> future = sendEvent(new DeactivationEvent());
        if (future != null) {
            future.get();
            future = sendEvent(new BombEvent());
        }
        //send deactivation event
        if (future != null) {
            future.get();
            sendBroadcast(new TerminateBattle());
        }
    }

    @Override
    protected void close() {
        Diary.getInstance().setLeiaTerminate(System.currentTimeMillis());
    }
}
