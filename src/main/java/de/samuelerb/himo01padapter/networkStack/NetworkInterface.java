package de.samuelerb.himo01padapter.networkStack;

import de.samuelerb.himo01padapter.networkStack.config.Config;
import de.samuelerb.himo01padapter.protocol.Protocol;
import de.samuelerb.himo01padapter.serialAdapter.SerialAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by samuelerb on 28.04.19.
 * Matr_nr: s0556350
 * Package: de.htw.f4.techmobsys.multihop.networkStack
 */
public class NetworkInterface {
    private Config config;
    private SerialAdapter serialAdapter;
    private String ownAddress;
    private String destinationAddress;

    public NetworkInterface(Config config, SerialAdapter serialAdapter) {
        this.config = config;
        this.serialAdapter = serialAdapter;
        this.setDestinationAddress("FFFF");
        this.writeConfig();
        this.setOwnAddress("0001");
        Thread protocol = new Thread(new Protocol(this));
        protocol.start();
        System.out.println("Initialized NetworkInterface");
    }


    public void sendNetworkPacket(NetworkPacket networkPacket) {
        System.out.println(networkPacket);
        networkPacket.setMessage(networkPacket.getMessage().replace("\n", "").replace("\r", ""));
        this.setDestinationAddress(networkPacket.getDestinationAddress());
        if (networkPacket.getMessage().length() != 0) {
            try {
//                if (!this.destinationAddress.equals(networkPacket.getDestinationAddress())) {
//                    serialAdapter.send(ATInstructions.getAT_DEST(networkPacket.getDestinationAddress()), "AT,OK");
//                }
                serialAdapter.send(ATInstructions.getAT_SEND(String.valueOf(networkPacket.getMessage().length())), "AT,OK");
                serialAdapter.send(networkPacket.getMessage(), "AT,SENDING");
                serialAdapter.waitForReply("AT,SENDED");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setDestinationAddress(String dstAddress) {
        this.destinationAddress = dstAddress;
        try {
            serialAdapter.send(ATInstructions.getAT_DEST(dstAddress), "AT,OK");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NetworkInterface setOwnAddress(String address) {
        try {
            this.ownAddress = address;
            serialAdapter.send(ATInstructions.getAT_ADDR(this.ownAddress), "AT,OK");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public NetworkPacket getLastMessage() {
        String rawMessage = serialAdapter.getFirstReply();
        System.out.println("getLastMessage: " + rawMessage);
        if (rawMessage.contains("LR,")) {
            List<String> messageFragments = Arrays.asList(rawMessage.split(","));
            String sourceAddress = messageFragments.get(1);
            String message = rawMessage.substring(
                    messageFragments.get(0).length() + messageFragments.get(1).length() + messageFragments.get(2).length() + 3
                    , rawMessage.length());
            NetworkPacket np = new NetworkPacket(sourceAddress, this.ownAddress, message);
            return np;
        }
        return null;
    }

    public String getOwnAddress() {
        return ownAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    private void writeConfig() {
        try {
            serialAdapter.send(ATInstructions.getAT_CFG(this.config.getConfigString()), "AT,OK");
            Thread.sleep(1000);
            serialAdapter.send(ATInstructions.getAT_SAVE(), "AT,OK");
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
