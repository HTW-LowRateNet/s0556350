package de.samuelerb.himo01padapter.serialAdapter.emulator;

import de.samuelerb.himo01padapter.serialAdapter.Message;

/**
 * Created by samuelerb on 29.04.19.
 * Matr_nr: s0556350
 * Package: de.htw.f4.techmobsys.multihop.serialAdapter.emulator
 */
public class RX implements Runnable {

    private EmuAdapter emuAdapter;

    public RX(EmuAdapter emuAdapter) {
        this.emuAdapter = emuAdapter;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        System.out.println("RX awake");

        while (true) {
            if (!emuAdapter.getSent().isEmpty()) {
                Message sent = emuAdapter.getLastSentMessage();
                emuAdapter.addToReplyQueue(sent.getExpectedReply());
                System.out.println("[->] " + sent.getExpectedReply());
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
