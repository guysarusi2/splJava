package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	public static MessageBus obj;		//singleton

	//private Queue<Queue<MicroService>> attackEventsQueue;
	//private Queue<Queue<MicroService>> deactivationEventsQueue;
	private ConcurrentHashMap<Class<? extends Message>,ConcurrentLinkedQueue<ConcurrentLinkedQueue<Message>>> messageTypeToQueueHash;//this hash is matching keys of Event type to its proper queue.
	private ConcurrentHashMap<MicroService,ConcurrentLinkedQueue<Message>> microServiceToMessageQueueHash;

	private ConcurrentHashMap<Event, Future> eventToFutureHash;		// todo use

	private static class SingletonHolder { private static MessageBusImpl instance = new MessageBusImpl();}

	private MessageBusImpl() {
		ConcurrentLinkedQueue<ConcurrentLinkedQueue<Message>> attackEventsQueue = new ConcurrentLinkedQueue<>();
		ConcurrentLinkedQueue<ConcurrentLinkedQueue<Message>> deactivationEventsQueue = new ConcurrentLinkedQueue<>();
		messageTypeToQueueHash = new ConcurrentHashMap();

		messageTypeToQueueHash.put(AttackEvent.class, attackEventsQueue);
		messageTypeToQueueHash.put(DeactivationEvent.class, deactivationEventsQueue);

		microServiceToMessageQueueHash = new ConcurrentHashMap<>();
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

	private  void subscribe(Class<? extends Message> type, MicroService m) {
		if(!messageTypeToQueueHash.containsKey(type))
			synchronized (messageTypeToQueueHash) {                                   //todo: reconsider usage of synchronized
				if (!messageTypeToQueueHash.containsKey(type))
					messageTypeToQueueHash.put(type, new ConcurrentLinkedQueue<>());
			}

		ConcurrentLinkedQueue<ConcurrentLinkedQueue<Message>> messageQueue = messageTypeToQueueHash.get(type);
		ConcurrentLinkedQueue<Message> msQueue = microServiceToMessageQueueHash.get(m);
		messageQueue.add(msQueue);
	}

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		//todo IMPL
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		//todo IMPL
	}


	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		//todo IMPL
		return null;
	}

	@Override
	public void register(MicroService m) {
		ConcurrentLinkedQueue<Message> messagesQueue = new ConcurrentLinkedQueue<>();
		microServiceToMessageQueueHash.put(m, messagesQueue);
	}

	@Override
	public void unregister(MicroService m) {
		//todo IMPL
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		ConcurrentLinkedQueue<Message> mQueue = microServiceToMessageQueueHash.get(m);
		while (mQueue.isEmpty()) {
			m.wait();
		}

		return mQueue.remove();
	}
}
