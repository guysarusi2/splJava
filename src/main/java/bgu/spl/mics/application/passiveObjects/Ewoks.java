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
    private ConcurrentHashMap<Integer, Ewok> ewoksList;

    private static class SingletonHolder {
        private static Ewoks instance = new Ewoks();
    }

    private Ewoks() {
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
            }
        }
    }

    public void releaseEwoks(List<Integer> serials) {
        Collections.sort(serials);
        Iterator<Integer> iterator = serials.iterator();
        while (iterator.hasNext()) {
            Integer ewokSerial = iterator.next();
            Ewok nextRelease = ewoksList.get(ewokSerial);
            synchronized (nextRelease) {
                nextRelease.release();
                nextRelease.notifyAll();
            }
        }
    }
}
