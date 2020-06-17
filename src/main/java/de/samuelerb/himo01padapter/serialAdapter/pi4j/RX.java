package de.samuelerb.himo01padapter.serialAdapter.pi4j;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEventListener;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by samuelerb on 28.04.19.
 * Matr_nr: s0556350
 * Package: de.htw.f4.techmobsys.multihop.adapter.pi4j
 */
public class RX implements Runnable {

    private Pi4jAdapter pi4jAdapter;
    private Serial serial;
    private Logger logger;

    public RX(Pi4jAdapter pi4jAdapter, Serial serial, Logger logger) {
        this.pi4jAdapter = pi4jAdapter;
        this.serial = serial;
        this.logger = logger;
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
        System.out.println("Started RX Thread for Pi4JAdapter...");
        this.serial.addListener((SerialDataEventListener) event -> {
            try {
                String ascii = event.getAsciiString();
                ascii = ascii.replace("\n", "").replace("\r", "");
                System.out.println("[->] " + ascii);
                pi4jAdapter.addToReplyQueue(ascii);
            } catch (IOException e) {
                e.printStackTrace();
                logger.log(Level.SEVERE, e.getMessage());
            }
        });

    }
}
