package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
    private ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<ConcurrentLinkedQueue<Message>>> messageTypeToQueueHash;//this hash is matching keys of Event type to its proper queue.
    private ConcurrentHashMap<MicroService, ConcurrentLinkedQueue<Message>> microServiceToMessageQueueHash;
    private ConcurrentHashMap<Event, Future> eventToFutureHash;
    private ConcurrentHashMap<MicroService, ConcurrentSkipListSet<ConcurrentLinkedQueue<Message>>> microServiceSubscriptions;

    //private ConcurrentHashMap<MicroService, ConcurrentSkipListSet<Class<? extends Message>>> microSubs;
    private ConcurrentHashMap<MicroService, ConcurrentLinkedQueue<Class<? extends Message>>> microSubs;


    private static class SingletonHolder {
        private static MessageBusImpl instance = new MessageBusImpl();
    }

    private MessageBusImpl() {
        eventToFutureHash = new ConcurrentHashMap();
        messageTypeToQueueHash = new ConcurrentHashMap();
        microServiceToMessageQueueHash = new ConcurrentHashMap<>();
        microServiceSubscriptions = new ConcurrentHashMap<>();

        microSubs = new ConcurrentHashMap<>();

        //todo remove
//        ConcurrentLinkedQueue<ConcurrentLinkedQueue<Message>> attackEventsQueue = new ConcurrentLinkedQueue<>();
//        ConcurrentLinkedQueue<ConcurrentLinkedQueue<Message>> deactivationEventsQueue = new ConcurrentLinkedQueue<>();
//        messageTypeToQueueHash.put(AttackEvent.class, attackEventsQueue);
//        messageTypeToQueueHash.put(DeactivationEvent.class, deactivationEventsQueue);
    }

    public static MessageBusImpl getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
        subscribe(type, m);
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        subscribe(type, m);
    }

    private void subscribe(Class<? extends Message> type, MicroService m) {
        if (!messageTypeToQueueHash.containsKey(type)) {
            synchronized (messageTypeToQueueHash) {                                   //todo: reconsider usage of synchronized
                if (!messageTypeToQueueHash.containsKey(type))
                    messageTypeToQueueHash.put(type, new ConcurrentLinkedQueue<>());
            }
        }

        ConcurrentLinkedQueue<ConcurrentLinkedQueue<Message>> messageQueue = messageTypeToQueueHash.get(type);
        ConcurrentLinkedQueue<Message> msQueue = microServiceToMessageQueueHash.get(m);
        messageQueue.add(msQueue);

        //todo SHAHAR
//        ConcurrentSkipListSet<ConcurrentLinkedQueue> mSubscriptionsSet = microServiceSubscriptions.get(m);
//        mSubscriptionsSet.add(messageQueue);

        //todo GUY
        microSubs.get(m).add(type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void complete(Event<T> e, T result) {
        Future<T> future = eventToFutureHash.get(e);
        future.resolve(result);
    }

    @Override
    public void sendBroadcast(Broadcast b) {
        if (messageTypeToQueueHash.containsKey(b.getClass())) {
            ConcurrentLinkedQueue<ConcurrentLinkedQueue<Message>> mainQueue = messageTypeToQueueHash.get(b.getClass());

            Iterator<ConcurrentLinkedQueue<Message>> iterator = mainQueue.iterator();
            while (iterator.hasNext()) {
                //todo shahar
                //iterator.next().add(b);
                //todo guy
                ConcurrentLinkedQueue<Message> microServiceQueue = iterator.next();
                synchronized (microServiceQueue) {
                    microServiceQueue.add(b);
                    microServiceQueue.notifyAll();
                }
            }
        }
    }


    @Override
    public <T> Future<T> sendEvent(Event<T> e) {

        if (messageTypeToQueueHash.containsKey(e.getClass())) {
            Future<T> future = new Future<>();
            eventToFutureHash.put(e, future);
            ConcurrentLinkedQueue<ConcurrentLinkedQueue<Message>> mainQueue = messageTypeToQueueHash.get(e.getClass());
            ConcurrentLinkedQueue<Message> firstQueue = mainQueue.poll();
            if (firstQueue == null) //todo when this condition happened?
                return null;
            //todo guy
            synchronized (firstQueue) {
                firstQueue.add(e);
                firstQueue.notifyAll();
            }
            mainQueue.add(firstQueue);
            return future;
        }
        return null;
    }

    @Override
    public void register(MicroService m) {
        if (!microServiceToMessageQueueHash.containsKey(m)) {
            ConcurrentLinkedQueue<Message> messagesQueue = new ConcurrentLinkedQueue<>();
            microServiceToMessageQueueHash.put(m, messagesQueue);
            microServiceSubscriptions.put(m, new ConcurrentSkipListSet<>());

            //todo guy
            microSubs.put(m, new ConcurrentLinkedQueue<>());
        }
    }


    @Override
    public void unregister(MicroService m) {
        if (microServiceToMessageQueueHash.containsKey(m)) {
            //todo add collection for message types of m and then iterate it. update in subscribe methods.

            //todo  SHAHAR
            ConcurrentSkipListSet<ConcurrentLinkedQueue<Message>> subscriptions = microServiceSubscriptions.get(m);
            Iterator<ConcurrentLinkedQueue<Message>> iterator = subscriptions.iterator();

            ConcurrentLinkedQueue<Message> mQueue = microServiceToMessageQueueHash.get(m);

            while (iterator.hasNext()) {
                ConcurrentLinkedQueue nextType = iterator.next();
                nextType.remove(mQueue);
            }


            //todo GUYSA
           /* ConcurrentLinkedQueue<Class<? extends Message>> subs = microSubs.get(m);
            ConcurrentLinkedQueue<Message> mQueue = microServiceToMessageQueueHash.get(m);
            Iterator<Class<? extends Message>> iterator = subs.iterator();

            while (iterator.hasNext()) {
                messageTypeToQueueHash.get(iterator.next()).remove(mQueue);
            }*/


            microServiceToMessageQueueHash.remove(m);
        }


    }

    @Override
    public Message awaitMessage(MicroService m) throws InterruptedException {
        ConcurrentLinkedQueue<Message> mQueue = microServiceToMessageQueueHash.get(m);
        synchronized (mQueue) {
            while (mQueue.isEmpty()) {
                //todo fix wait
                //m.wait();
                mQueue.wait();
            }
        }

        return mQueue.remove();
    }
}
