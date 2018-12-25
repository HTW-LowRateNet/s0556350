package de.htw.f4.techmobsys.multihop.Controller;

import com.pi4j.io.serial.Serial;
import com.pi4j.util.Console;
import de.htw.f4.techmobsys.multihop.Sender;
import de.htw.f4.techmobsys.multihop.beans.Message;
import de.htw.f4.techmobsys.multihop.beans.MessageCode;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;


public class MainController {
    public static String sourceAddress;
    public static String destinationAddress;
    /**
     * coordinator is false if the state is node
     * else we are the coordinator
     */
    public static boolean coordinator = false;
    //    Config variables
    private final String coordinatorAddress = "0000";
    private final String broadcastAddress = "FFFF";
    private final int standardTTL = 5;


    private Console console;
    private Serial serial;
    private Sender sender;

    private LinkedList<Message> queue = new LinkedList<>();
    private LinkedList<String> addressList;


    public MainController(Console console, Serial serial, Sender sender) {
        this.console = console;
        this.serial = serial;
        this.sender = sender;
    }

    public void init() {
        String configString = "433000000,20,9,10,1,1,0,0,0,0,3000,8,4";
        System.out.println("init");
        this.sender.writeCFG(configString);
//        serialWrite(ATInstructions.getAT_CFG(configString));
        this.sender.writeSAVE();
//        serialWrite(ATInstructions.getAT_SAVE());
        try {
            this.doCDIS();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Console getConsole() {
        return console;
    }

    public void run() {
        while (console.isRunning()) {
            if (!queue.isEmpty()) {
                SerialInputProcessor sip =
                        new SerialInputProcessor(this, queue.removeFirst());
                Thread thread = new Thread(sip);
                thread.start();
            }
        }
    }

    public void addToMessageQueue(Message msg) {
        this.queue.add(msg);
    }

    public void processCoordinatorWork(Message msg) {
//        console.box("COORDINATORSTUFF");

        switch (msg.mc) {
            case KoordinatorDiscovery:
                sendALIV();
                break;
            case Address:
                //            Get random address
                String addr;
                do {
                    addr = getRandomAddress(0x0100, 0xFFFE);

                } while (!this.addressList.contains(addr));
//            end
                sendMessage(msg.sender,
                        new Message(MessageCode.Address,
                                standardTTL,
                                0,
                                sourceAddress,
                                msg.sender,
                                addr));
                break;
            case AACK:
                this.addressList.add(msg.sender);
                break;
            default:
                console.box("Received wrong MessageCode for CoordinatorProcessing:", msg.mc.code);
                break;
        }
    }

    public void doCDIS() throws InterruptedException {
//        console.println("CDIS");

        sourceAddress = this.getRandomAddress(0x0011, 0x00FF);
        setSrcAddr(sourceAddress);

        AtomicBoolean reply = new AtomicBoolean(false);
        for (int iteration = 0; iteration < 3; iteration++) {
            sendMessage(broadcastAddress,
                    new Message(
                            MessageCode.KoordinatorDiscovery,
                            standardTTL,
                            0,
                            sourceAddress,
                            broadcastAddress,
                            "Hallo i bims"));
            Thread.sleep(200);
            this.queue.stream()
                    .filter(msg -> msg.mc == MessageCode.ALIV && msg.sender.equals(coordinatorAddress))
                    .findFirst()
                    .ifPresent(msg -> {
//                        Im not the coordinator
                        coordinator = false;
                        addressList = null;
                        try {
                            processADDR();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        queue.remove(msg);
                        reply.set(true);
                    });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (reply.get()) {
                break;
            }
        }

        if (!reply.get()) {
            if (!coordinator) {
                coordinator = true;
                addressList = new LinkedList<>();
            }
        }
        console.title("Coordinator: " + coordinator);
    }

    public void processADDR() throws InterruptedException {
//        console.box("ADDR");

        AtomicBoolean reply = new AtomicBoolean(false);
        for (int iteration = 0; iteration < 3; iteration++) {
            sendMessage(coordinatorAddress,
                    new Message(
                            MessageCode.Address,
                            standardTTL,
                            0,
                            sourceAddress,
                            destinationAddress,
                            "Mach ma ne Adresse klar!"));
            Thread.sleep(200);
            this.queue.stream()
                    .filter(msg -> msg.mc == MessageCode.Address && msg.sender.equals(coordinatorAddress))
                    .findFirst()
                    .ifPresent(msg -> {
//                        setSrcAddr(msg.payload);
                        queue.remove(msg);
                        reply.set(true);
                        sendMessage(coordinatorAddress,
                                new Message(
                                        MessageCode.AACK,
                                        standardTTL,
                                        0,
                                        sourceAddress,
                                        coordinatorAddress,
                                        "Danke fuer die Addresse"));
                    });
            if (reply.get()) {
                break;
            }
        }

        if (!reply.get()) {
            doCDIS();
        }

    }

    public void processALIV(Message msg) {
//        TODO
    }

    public void processPOLL(Message msg) {
//        TODO
    }

    public void processMSG(Message msg) {
//        TODO
    }

    public void sendALIV() {
//        console.box("ALIV");
        sendMessage(broadcastAddress,
                new Message(
                        MessageCode.ALIV,
                        standardTTL,
                        0,
                        sourceAddress,
                        broadcastAddress,
                        "Hallo i bims noch da"));
    }


    /**
     * Some handy helper functions.
     */
    private void setSrcAddr(String addr) {
        sourceAddress = addr;
//        serialWrite(ATInstructions.getAT_ADDR(sourceAddress));
    }

    private void setDestinationAddress(String addr) {
        destinationAddress = addr;
    }

    void sendMessage(String dstAddress, Message s) {
        setDestinationAddress(dstAddress);
        s.dstAddress = dstAddress;
        this.sender.sendMessage(s.toString());
//        serialWrite(ATInstructions.getAT_SEND(String.valueOf(s.length())));
//        serialWrite(s);
    }

    private String getRandomAddress(int min, int max) {
        Random r = new Random();
        int randInt = r.nextInt((max - min) + 1) + min;
        String addr = Integer.toHexString(randInt);
        addr = addr.toUpperCase();
        while (addr.length() < 4) {
            addr = "0" + addr;
        }
        return addr;
    }

    public Serial getSerial() {
        return serial;
    }
}
