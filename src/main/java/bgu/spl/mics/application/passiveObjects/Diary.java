package bgu.spl.mics.application.passiveObjects;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {

    private AtomicInteger totalAttacks;
    private long HanSoloFinish;
    private long C3POFinish;
    private long R2D2Deactivate;
    private long LeiaTerminate;
    private long HanSoloTerminate;
    private long C3POTerminate;
    private long LandoTerminate;
    private long R2D2Terminate;

    private static class SingletonHolder {
        private static Diary instance = new Diary();
    }

    private Diary() {
        totalAttacks = new AtomicInteger(0);
    }

    public static Diary getInstance() {
        return SingletonHolder.instance;
    }

    public void attack() {
        totalAttacks.incrementAndGet();
    }

    public int getTotalAttacks() {
        return totalAttacks.get();
    }

    public void HanSolo_Finish(long timeStampInMillis) {
        HanSoloFinish = timeStampInMillis;
    }

    public void C3PO_Finish(long timeStampInMillis) {
        C3POFinish = timeStampInMillis;
    }

    public void R2D2_Deactivate(long timeStampInMillis) {
        R2D2Deactivate = timeStampInMillis;
    }

    public void Leia_Terminate(long timeStampInMillis) {
        LeiaTerminate = timeStampInMillis;
    }

    public void HanSolo_Terminate(long timeStampInMillis) {
        HanSoloTerminate = timeStampInMillis;
    }

    public void C3PO_Terminate(long timeStampInMillis) {
        C3POTerminate = timeStampInMillis;
    }

    public void Lando_Terminate(long timeStampInMillis) {
        LandoTerminate = timeStampInMillis;
    }

    public void R2D2_Terminate(long timeStampInMillis) {
        R2D2Terminate = timeStampInMillis;
    }


}
