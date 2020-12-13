package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.TerminateBattle;
import bgu.spl.mics.application.messages.BombEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {
    private long duration;

    public R2D2Microservice(long duration) {
        super("R2D2");
        this.duration = duration;
    }

    @Override
    protected void initialize() {
        subscribeEvent(DeactivationEvent.class, (event) -> {
            try {
                Thread.sleep(duration);
                System.out.println("R2D2: shield generator deactivated");
            } catch (InterruptedException e) {
            }
            //todo
            sendEvent(new BombEvent());
            System.out.println("R2D2 : bombevent event sent");
        });
        subscribeBroadcast(TerminateBattle.class, (event) -> {
            terminate();
            System.out.println("R2D2 : terminated");
        });
    }

    @Override
    protected void close() {

    }
}
