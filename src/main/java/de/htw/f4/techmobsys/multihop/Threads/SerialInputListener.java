package de.htw.f4.techmobsys.multihop.Threads;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEventListener;
import com.pi4j.util.Console;
import de.htw.f4.techmobsys.multihop.Sender;
import de.htw.f4.techmobsys.multihop.Controller.MainController;
import de.htw.f4.techmobsys.multihop.beans.Message;
import de.htw.f4.techmobsys.multihop.beans.MessageCode;

import java.io.IOException;
import java.util.Arrays;


public class SerialInputListener implements Runnable {

    private Serial serial;
    private Console console;
    private MainController mainController;
    private Sender sender;

    public SerialInputListener(Serial serial, MainController mainController, Sender sender) {
        this.serial = serial;
        this.console = mainController.getConsole();
        this.mainController = mainController;
        this.sender = sender;
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
//        console.println(this.toString() + " started!");

        // create and register the serial data listener
        this.serial.addListener((SerialDataEventListener) event -> {
            try {
                String data = event.getAsciiString();
                console.println("[<-] " + data);

                if (stringContainsMessage(data)) {
                    Message msg = convertAsciiToMessage(data);
                    mainController.addToMessageQueue(msg);
                } else {
                    this.sender.addToQueue(data);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    private Message convertAsciiToMessage(String data) {
        String[] messageArr = data.split(",");
        if (messageArr.length >= 7) {
            String payload = String.join(" ", (String[]) Arrays.copyOfRange(messageArr, 6, messageArr.length - 1, String[].class));
            MessageCode mc;
            try {
                mc = MessageCode.fromString(messageArr[3]);
            } catch (EnumConstantNotPresentException e) {
                console.println(e.getMessage());
                mc = MessageCode.Message;
            }

            String messageID = messageArr[4];
            int ttl = Integer.parseInt(messageArr[5]);
            int hops = Integer.parseInt(messageArr[6]);
            String sender = messageArr[7];
            String receiver = messageArr[8];
            Message msg = new Message(mc, ttl, hops, sender, receiver, payload);
            msg.raw = data;
            msg.setMessageID(messageID);
            return msg;
        } else {
            return new Message(data);
        }
    }

    private boolean stringContainsMessage(String data) {
        String[] messageArr = data.split(",");
        if (messageArr.length >= 7) {
            return true;
        } else {
            return false;
        }
    }
}
