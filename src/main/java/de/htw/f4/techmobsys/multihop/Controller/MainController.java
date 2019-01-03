package de.htw.f4.techmobsys.multihop.Controller;

import com.pi4j.io.serial.Serial;
import com.pi4j.util.Console;
import de.htw.f4.techmobsys.multihop.Sender;
import de.htw.f4.techmobsys.multihop.beans.Message;
import de.htw.f4.techmobsys.multihop.beans.MessageCode;
import de.htw.f4.techmobsys.multihop.beans.NodeState;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;


public class MainController {
    public static String myAddress;
    public static String destinationAddress;
    /**
     * coordinator is false if the state is node
     * else we are the coordinator
     */
    public static NodeState coordinator = NodeState.NEW;
    //    Config variables
    private final String coordinatorAddress = "0000";
    private final String broadcastAddress = "FFFF";
    private final int standardTTL = 5;


    private Console console;
    private Serial serial;
    private Sender sender;

    private LinkedList<Message> messageQueue = new LinkedList<>();
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
        this.sender.writeSAVE();
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
            if (!messageQueue.isEmpty()) {
                SerialInputProcessor sip =
                        new SerialInputProcessor(this, messageQueue.removeFirst());
                Thread thread = new Thread(sip);
                thread.start();
            }
        }
    }

    public void addToMessageQueue(Message msg) {
        this.messageQueue.add(msg);
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
                                myAddress,
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

        myAddress = this.getRandomAddress(0x0011, 0x00FF);
        setSrcAddr(myAddress);

        AtomicBoolean reply = new AtomicBoolean(false);
        for (int iteration = 0; iteration < 3; iteration++) {
            sendMessage(broadcastAddress,
                    new Message(
                            MessageCode.KoordinatorDiscovery,
                            standardTTL,
                            0,
                            myAddress,
                            broadcastAddress,
                            "Hallo i bims"));
            Thread.sleep(200);
            this.messageQueue.stream()
                    .filter(msg -> msg.mc == MessageCode.ALIV && msg.sender.equals(coordinatorAddress))
                    .findFirst()
                    .ifPresent(msg -> {
                        reply.set(processALIV(msg));

                    });
            try {
                Thread.sleep(20 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (reply.get()) {
                break;
            }
        }

        if (!reply.get()) {
            if (coordinator != NodeState.COORDINATOR) {
                coordinator = NodeState.COORDINATOR;
                addressList = new LinkedList<>();
            }
        }
        console.title("Coordinator: " + coordinator);
    }

    public boolean doADDR() throws InterruptedException {
//        console.box("ADDR");

        AtomicBoolean reply = new AtomicBoolean(false);
        for (int iteration = 0; iteration < 3; iteration++) {
            sendMessage(coordinatorAddress,
                    new Message(
                            MessageCode.Address,
                            standardTTL,
                            0,
                            myAddress,
                            destinationAddress,
                            "Mach ma ne Adresse klar!"));
            Thread.sleep(200);
            this.messageQueue.stream()
                    .filter(msg -> msg.mc == MessageCode.Address && msg.sender.equals(coordinatorAddress))
                    .findFirst()
                    .ifPresent(msg -> {
//                        setSrcAddr(msg.payload);
                        messageQueue.remove(msg);
                        reply.set(true);
                        doACKK();
                    });
            if (reply.get()) {
                break;
            }
            Thread.sleep(20 * 1000);
        }

        if (!reply.get()) {
            doCDIS();
        }

        return reply.get();
    }

    public void doACKK() {
        sendMessage(coordinatorAddress,
                new Message(
                        MessageCode.AACK,
                        standardTTL,
                        0,
                        myAddress,
                        coordinatorAddress,
                        "Danke fuer die Addresse"));
        coordinator = NodeState.CLIENT;
    }

    public boolean processALIV(Message msg) {
//        TODO set State to node and generate a random address 0010 - 00FF
        //                        Im not the coordinator
        if (coordinator == NodeState.NEW) {
            myAddress = getRandomAddress(0x0010, 0x00FF);
        }

        if (coordinator == NodeState.COORDINATOR) {
            addressList = null;
        }
        boolean gotReply = false;
        try {
            gotReply = doADDR();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        messageQueue.remove(msg);
        return gotReply;
    }

    public void processPOLL(Message msg) {
//        TODO
    }

    public void processMSG(Message msg) {
        this.console.title("Message Received",
                "Sender: " + msg.sender, "Content:",
                msg.payload, "", "", " RAW: " + msg.toString());
    }

    public void sendALIV() {
//        console.box("ALIV");
        sendMessage(broadcastAddress,
                new Message(
                        MessageCode.ALIV,
                        standardTTL,
                        0,
                        myAddress,
                        broadcastAddress,
                        "Hallo i bims noch da"));
    }


    /**
     * Some handy helper functions.
     */
    private void setSrcAddr(String addr) {
        myAddress = addr;
//        serialWrite(ATInstructions.getAT_ADDR(myAddress));
    }

    private void setDestinationAddress(String addr) {
        destinationAddress = addr;
    }

    void sendMessage(String dstAddress, Message s) {
        setDestinationAddress(dstAddress);
        s.receiver = dstAddress;
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
