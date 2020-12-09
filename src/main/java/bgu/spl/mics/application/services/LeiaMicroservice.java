package bgu.spl.mics.application.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;


import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.Attack;


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

    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
        this.attacks = attacks;
    }

    @Override
    protected void initialize() {
        //send all attack object as attackevents
        for (Attack a : attacks) {
            Future<Boolean> future = sendEvent(new AttackEvent(a.getSerials(), a.getDuration()));
            while (future == null) {
                //todo sleep or some
                //CountDownLatch a= new CountDownLatch()
            }
        }
        //check if future finished and send deactivatione
        //chech if future finished and send bombevent
    }
}
