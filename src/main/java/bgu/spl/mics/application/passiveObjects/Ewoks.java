package bgu.spl.mics.application.passiveObjects;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
    // TODO: 13/12/2020 delete isAvailable
    private AtomicBoolean isAvailable;
    private ConcurrentHashMap<Integer, Ewok> ewoksList;

    private static class SingletonHolder {
        private static Ewoks instance = new Ewoks();
    }

    private Ewoks() {
        //todo
        //ewoksList = new ConcurrentHashMap<>();
        isAvailable = new AtomicBoolean(true);
    }

    public static Ewoks getInstance() {
        return SingletonHolder.instance;
    }

    public void setEwoksList(int numOfEwoks) {
        ewoksList = new ConcurrentHashMap<>();
        for (int i = 1; i <= numOfEwoks; i++)
            ewoksList.put(i, new Ewok(i));
    }

    public void aquireEwoks(List<Integer> serials) {
        Collections.sort(serials);
        Iterator<Integer> iterator = serials.iterator();
//        synchronized (isAvailable) {
//            while (!isAvailable.get()) {
//                try {
//                    isAvailable.wait();
//                } catch (InterruptedException e) {
//                }
//            }
//            isAvailable.compareAndSet(true, false);
//        }
//        while (iterator.hasNext()) {
//            Integer ewokSerial = iterator.next();
//            if (ewoksList.get(ewokSerial).isAvailable())
//                ewoksList.get(ewokSerial).acquire();
//        }

        while (iterator.hasNext()) {
            Integer ewokSerial = iterator.next();
            Ewok nextRequired = ewoksList.get(ewokSerial);
            synchronized (nextRequired) {
                while (!nextRequired.isAvailable()) {
                    try {
                        nextRequired.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                nextRequired.acquire();
                //System.out.println("aquire" + ewokSerial);
            }
        }


    }

//    public boolean isAvailable() {
//        return isAvailable.get();
//    }

    public void releaseEwoks(List<Integer> serials) {
        //todo consider sort again?
        Collections.sort(serials);
        Iterator<Integer> iterator = serials.iterator();
//        while (iterator.hasNext())
//            ewoksList.get(iterator.next()).release();
//        synchronized (isAvailable) {
//            isAvailable.compareAndSet(false, true);
//            isAvailable.notifyAll();
//        }

        while (iterator.hasNext()) {
            Integer ewokSerial = iterator.next();
            Ewok nextRelease = ewoksList.get(ewokSerial);
            synchronized (nextRelease) {
                nextRelease.release();
                //System.out.println("release" + ewokSerial);
                nextRelease.notifyAll();
            }
        }
    }

}
