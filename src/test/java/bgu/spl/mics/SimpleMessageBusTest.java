package bgu.spl.mics;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleMessageBusTest {

    private SimpleMessageBus mBus;
    private SimpleMicroService m1;
    private SimpleMicroService m2;
    private SimpleEvent event = new SimpleEvent();
    private SimpleBroadcast broadcast = new SimpleBroadcast();
    private Future<Integer> future = new Future();

    @BeforeEach
    void setUp() {
        mBus = new SimpleMessageBus();
        m1 = new SimpleMicroService("m1");
        mBus.register(m1);
        m2 = new SimpleMicroService("m2");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void subscribeEvent() {
        try {
            assertNull(mBus.eventSubsMap.get(event.getClass()));
            mBus.subscribeEvent(event.getClass(), m1);
            assertNotNull(mBus.eventSubsMap.get(event.getClass())[0]);
        } catch (Exception e) {
            System.out.println("Failsubs: " + e.getMessage());
        }
    }

    @Test
    void subscribeBroadcast() {
        try {
            assertNull(mBus.eventSubsMap.get(broadcast.getClass()));
            mBus.subscribeBroadcast(broadcast.getClass(), m1);
            assertNotNull(mBus.eventSubsMap.get(broadcast.getClass())[0]);
        } catch (Exception e) {
            System.out.println("Fail: " + e.getMessage());
        }
    }

    // @Test
//    void complete() {
//        try {
//            assertFalse(mBus.eventFutureMap.get(event.getClass()).isDone());
//            mBus.complete(event, 5);
//            assertTrue(mBus.eventFutureMap.get(event.getClass()).isDone());
//            assertEquals(mBus.eventFutureMap.get(event.getClass()).get(), 5);
//        } catch (Exception e) {
//            System.out.println("Fail: " + e.getMessage());
//        }
//    }

    //todo fix
      //  @Test
  /*  void sendBroadcast() {
        try {
            assertTrue(mBus.subsQueueMap.get(m1.getName()).isEmpty());
            mBus.subscribeBroadcast(broadcast.getClass(), m1);
            mBus.sendBroadcast(broadcast);
            assertTrue(mBus.subsQueueMap.get(m1.getName()).isEmpty());
        } catch (Exception e) {
            System.out.println("Fail: " + e.getMessage());
        }
    }*/
////
//    @Test
//    void sendEvent() {
//        try {
//            assertFalse(mBus.subsQueueMap.get(m1.getName()).isEmpty());
//            mBus.subscribeEvent(event.getClass(), m1);
//            mBus.sendEvent(event);
//            assertTrue(!mBus.subsQueueMap.get(m1.getName()).isEmpty());
//        } catch (Exception e) {
//            System.out.println("Fail: " + e.getMessage());
//        }
//    }
//
    @Test
    void register() {
        try {
            assertNull(mBus.subsQueueMap.get(m2.getName()));
            mBus.register(m2);
            assertNotNull(mBus.subsQueueMap.get(m2.getName()));
        } catch (Exception e) {
            System.out.println("Fail: " + e.getMessage());
        }
    }
//
//    @Test
//    void awaitMessage() {
//        try {
//            mBus.subscribeEvent(event.getClass(), m1);
//            mBus.sendEvent(event);
//            Message tmp= mBus.awaitMessage(m1);
//            assertNotNull(tmp);
//            assertEquals(tmp, event); //todo
//        } catch (Exception e) {
//            System.out.println("Fail: " + e.getMessage());
//        }
//    }
}