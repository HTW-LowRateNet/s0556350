package de.htw.f4.techmobsys.multihop.beans;

import de.htw.f4.techmobsys.multihop.Controller.MainController;

import java.util.Random;


public class Message {
    public MessageCode mc;
    public String messageID;
    public int ttl;
    public int hops;
    public String sender;
    public String receiver;
    public String payload;
    public String srcAddress;
    public String dstAddress;
    public String raw;

    public Message(MessageCode mc, int ttl, int hops, String sender, String receiver, String payload) {
        this.mc = mc;
        this.messageID = String.valueOf(new Random().nextInt(255));
        this.ttl = ttl;
        this.hops = hops;
        this.sender = sender;
        this.receiver = receiver;
        this.payload = payload;
        this.srcAddress = MainController.sourceAddress;
        this.dstAddress = MainController.destinationAddress;
    }

    public Message(String raw) {
        this.raw = raw;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    @Override
    public String toString() {
        String sp = ",";
        return mc.getCode() + sp + messageID + sp + String.valueOf(ttl) + sp + String.valueOf(hops) + sp + srcAddress + sp + dstAddress + sp + payload + sp;
    }
}
