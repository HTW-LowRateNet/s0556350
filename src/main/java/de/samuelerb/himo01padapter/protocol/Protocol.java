package de.samuelerb.himo01padapter.protocol;

import de.samuelerb.himo01padapter.networkStack.NetworkInterface;
import de.samuelerb.himo01padapter.networkStack.NetworkPacket;

/**
 * Created by samuelerb on 2019-06-20.
 * Matr_nr: s0556350
 * Package: de.samuelerb.himo01padapter.protocol
 */
public class Protocol implements Runnable {
    int minSleepMillis = 30_000;
    int maxSleepMillis = 60_000;
    private NetworkInterface networkInterface;

    public Protocol(NetworkInterface networkInterface) {
        this.networkInterface = networkInterface;
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
        System.out.println("Started Protocol...");
        while (true) {
            System.out.println("Sending RTI...");
            networkInterface.sendNetworkPacket(new NetworkPacket(networkInterface.getOwnAddress(), "FFFF", "RTI"));
            try {
                Thread.sleep(getRandomIntInBounds(minSleepMillis, maxSleepMillis));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void

    private int getRandomIntInBounds(int min, int max) {
        return minSleepMillis + (int) (Math.random() * ((max - min) + 1));
    }
}
