package de.htw.f4.techmobsys.multihop.Threads;

import com.pi4j.io.serial.Serial;
import de.htw.f4.techmobsys.multihop.Controller.MainController;

import java.io.IOException;

public class SendThread implements Runnable {
    private Serial s;
    private String msg;

    public SendThread(Serial s, String msg) {
        this.s = s;
        this.msg = msg;
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
        synchronized (MainController.serialLock) {
            try {
                MainController.serialLock.wait();
                this.s.write(this.msg);
                this.s.write('\r');
                this.s.write('\n');
                MainController.serialLock.notifyAll();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
