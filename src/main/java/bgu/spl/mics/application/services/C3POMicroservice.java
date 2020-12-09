package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.Ewoks;


/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {

    public C3POMicroservice() {
        super("C3PO");
    }

    @Override
    protected void initialize() {
        subscribeEvent(AttackEvent.class, (event) -> {
                    //  aquire ewoks -> sleep -> release ewoks
                    //todo
                    Ewoks.getInstance().aquireEwoks(event.getSerials());
                    try {
                        Thread.sleep(event.getDuration());
                    } catch (InterruptedException e) {
                    }
                    Ewoks.getInstance().releaseEwoks(event.getSerials());
                }
        );
    }
}
