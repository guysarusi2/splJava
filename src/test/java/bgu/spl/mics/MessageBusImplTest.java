package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {
    private MessageBusImpl mBus;
    private MicroService m1;
    private AttackEvent ev = new AttackEvent();
    private Broadcast broadcast = new SimpleBroadcast();

    @BeforeEach
    void setUp() {
        mBus = new MessageBusImpl();
        m1 = new SimpleMicroService("m1");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void subscribeEvent() {
        m1.subscribeEvent(ev.getClass(), (c) -> {
        });
        try {
            mBus.sendEvent(ev);
            assertNotNull(mBus.awaitMessage(m1));
        } catch (Exception e) {
            System.out.println("fail" + e);
        }
    }


    //todo fix the await
    @Test
    void subscribeBroadcast() {
        m1.subscribeBroadcast(broadcast.getClass(), (c) -> {
        });
        try {
            mBus.sendBroadcast(broadcast);
            assertNotNull(mBus.awaitMessage(m1));
        } catch (Exception e) {
            System.out.println("fail" + e);
        }
    }

    @Test
    void complete() {
    }

    //todo fix the await
    @Test
    void sendBroadcast() {
        m1.subscribeBroadcast(broadcast.getClass(), (c -> {
        }));
        try {
            mBus.sendBroadcast(broadcast);
            assertNotNull(mBus.awaitMessage(m1));
        } catch (Exception e) {
            System.out.println("fail" + e);
        }
    }

    @Test
    void sendEvent() {
        m1.subscribeEvent(ev.getClass(), (c) -> {
        });
        try {
            mBus.sendEvent(ev);
            assertNotNull(mBus.awaitMessage(m1));
        } catch (Exception e) {
            System.out.println("fail" + e);
        }
    }

    @Test
    void register() {
    }

//    @Test
//    void unregister() {
//    }

    @Test
    void awaitMessage() {
        m1.subscribeEvent(ev.getClass(), (c) -> {
        });
        try {
            mBus.sendEvent(ev);
            assertNotNull(mBus.awaitMessage(m1));
        } catch (Exception e) {
            System.out.println("fail" + e);
        }
    }
}