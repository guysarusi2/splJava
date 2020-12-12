package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.TerminateBattle;
import bgu.spl.mics.application.messages.BombEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice extends MicroService {
    private long duration;

    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration = duration;
    }

    @Override
    protected void initialize() {
        subscribeEvent(BombEvent.class, (event) -> {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
            }
            //todo
            //sendBroadcast(new TerminateBattle());
        });
        subscribeBroadcast(TerminateBattle.class,(event)->{
            terminate();
        });
    }

    @Override
    protected void close() {

    }
}
