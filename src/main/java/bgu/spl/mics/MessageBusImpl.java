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
	private ConcurrentHashMap<Class<? extends Message>,ConcurrentLinkedQueue<ConcurrentLinkedQueue<Message>>> eventsQueuesHashMap;//this hash is matching keys of Event type to its proper queue.
	private ConcurrentHashMap<MicroService,ConcurrentLinkedQueue<Message>> microServicesHash;			//todo RWL


	private static class SingletonHolder { private static MessageBusImpl instance = new MessageBusImpl();}

	private MessageBusImpl() {
		ConcurrentLinkedQueue<ConcurrentLinkedQueue<Message>> attackEventsQueue = new ConcurrentLinkedQueue<>();
		ConcurrentLinkedQueue<ConcurrentLinkedQueue<Message>> deactivationEventsQueue = new ConcurrentLinkedQueue<>();
		eventsQueuesHashMap = new ConcurrentHashMap();

		eventsQueuesHashMap.put(AttackEvent.class, attackEventsQueue);
		eventsQueuesHashMap.put(DeactivationEvent.class, deactivationEventsQueue);

		microServicesHash = new ConcurrentHashMap<>();
	}
	public static MessageBusImpl getInstance() {
		return SingletonHolder.instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
/*		ConcurrentLinkedQueue relevantEventQueue = eventsQueuesHashMap.get(type);
		ConcurrentLinkedQueue<Event> mMessagesQueue = new ConcurrentLinkedQueue<>();

		relevantEventQueue.add(m);
		microServicesHash.put(m,mMessagesQueue);*/
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {

	}

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {

	}

	@Override
	public void sendBroadcast(Broadcast b) {

	}


	@Override
	public <T> Future<T> sendEvent(Event<T> e) {

		return null;
	}

	@Override
	public void register(MicroService m) {
		ConcurrentLinkedQueue<Message> messagesQueue = new ConcurrentLinkedQueue<>();

	}

	@Override
	public void unregister(MicroService m) {

	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {

		return null;
	}
}
