package de.htw.f4.techmobsys.multihop;

import com.pi4j.io.serial.Serial;
import com.pi4j.util.Console;
import de.htw.f4.techmobsys.multihop.beans.ATInstructions;

import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by samuelerb on 25.12.18.
 * Matr_nr: s0556350
 * Package: de.htw.f4.multihop
 */
public class Sender {
    private Serial serial;
    private Console console;

    private LinkedList<String> replyQueue = new LinkedList<>();

    public final Boolean sendLock = Boolean.FALSE;

    public Sender(Serial serial, Console console) {
        this.serial = serial;
        this.console = console;
    }

    public void waitForReset() {
        console.box("Waiting for RESET","" ,"Please connect the CRST-Pin to the GND-Pin");
        String waitString = "MODULE:HIMO-01M(V0.4)\n" +
                "Vendor:Himalaya";
        while (replyQueue.isEmpty()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!replyQueue.getFirst().equals(waitString)) {
            console.box("RESET SUCCESFUL!");
        }
    }

    public void writeADDR(String addr) {
        String writeToBoard = ATInstructions.getAT_ADDR(addr);
        send(writeToBoard, "OK");


    }

    public void writeDEST(String dest) {
        String writeToBoard = ATInstructions.getAT_DEST(dest);
        send(writeToBoard, "OK");
    }

    public void writeCFG(String cfg) {
        this.console.println("CFG");
        String writeToBoard = ATInstructions.getAT_CFG(cfg);
        send(writeToBoard, "OK");

    }

    public void writeSAVE() {
        String writeToBoard = ATInstructions.getAT_SAVE();
        send(writeToBoard,"OK");

    }

    public void sendMessage(String message) {
        writeSEND(message.length());

        synchronized (sendLock) {
            this.console.box(31, "Lock");
//            this.console.println("++++++++++++++++Lock++++++++++++++++");
            try {
                this.serial.write(message);
                this.console.println("[->] " + message);
                this.serial.write('\r');
                this.serial.write('\n');
                Thread.sleep(50);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            boolean sendFinished = false;
            while (!sendFinished) {
                this.console.println("Wait for : " + "SENDING");
                while (replyQueue.isEmpty()) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (replyQueue.getFirst().contains("SENDING")) {
                    replyQueue.removeFirst();
                    sendFinished = true;
                    this.console.println("Found: " + "SENDING");
                } else {
                    replyQueue.removeFirst();
                }
            }

            sendFinished = false;
            while (!sendFinished) {
                this.console.println("Wait for : " + "SENDED");

//                this.console.println("->");
                while (replyQueue.isEmpty()) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (replyQueue.getFirst().contains("SENDED")) {
                    replyQueue.removeFirst();
                    sendFinished = true;
                    this.console.println("Found: " + "SENDED");
                } else {
                    this.console.println("-> " + replyQueue.getFirst());
                    replyQueue.removeFirst();
                }

//                replyQueue.stream().forEach((e) -> {
//                    this.console.println("+" + e + "-");
//                    this.console.println("\n");
//                });
            }
            this.console.box(30, "UnLock");
//            this.console.println("++++++++++++++++UnLock++++++++++++++++");
        }
    }

    private void writeSEND(int length) {
        String writeToBoard = ATInstructions.getAT_SEND(String.valueOf(length));
        send(writeToBoard, "OK");
    }

    public  void writeRaw(String s) {
        synchronized (sendLock) {
            try {
                this.serial.write(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void send(String message, String reply) {
        this.console.println("[->] " + message);
        boolean sendFinished = false;
        synchronized (sendLock) {
            this.console.box(31, "Lock");
//            this.console.println("++++++++++++++++Lock++++++++++++++++");
            try {
                this.serial.write(message);
                this.serial.write('\r');
                this.serial.write('\n');
                Thread.sleep(50);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            this.console.println("Wait for : " + reply);
            while (!sendFinished) {

                while (replyQueue.isEmpty()) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


                if (replyQueue.getFirst().contains(reply)) {
                    replyQueue.removeFirst();
                    sendFinished = true;
                    this.console.println("Found: " + reply);
                } else {
                    replyQueue.removeFirst();
                }
            }
            this.console.box(30, "UnLock");
//            this.console.println("++++++++++++++++UnLock++++++++++++++++");
        }
    }

    public void addToQueue(String reply) {
        replyQueue.add(reply);
    }
}
