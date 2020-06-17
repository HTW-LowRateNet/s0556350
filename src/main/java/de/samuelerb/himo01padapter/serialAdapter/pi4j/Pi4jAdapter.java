package de.samuelerb.himo01padapter.serialAdapter.pi4j;

import de.samuelerb.himo01padapter.serialAdapter.Message;
import de.samuelerb.himo01padapter.serialAdapter.SerialAdapter;

import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by samuelerb on 28.04.19.
 * Matr_nr: s0556350
 * Package: de.htw.f4.techmobsys.multihop.adapter.pi4j
 */
public class Pi4jAdapter implements SerialAdapter {

    private LinkedBlockingQueue<Message> sendQueue;
    private LinkedBlockingQueue<String> replyQueue;
    private static final int TIMEOUT = 1000;

    public Pi4jAdapter() {
        this.sendQueue = new LinkedBlockingQueue<>();
        this.replyQueue = new LinkedBlockingQueue<>();
        System.out.println("Initialized Pi4JAdapter");
    }


    @Override
    public boolean send(String ascii, String expectedReply) throws InterruptedException {
        System.out.println(ascii + " " + expectedReply);
        Thread.sleep(500);
        Message msg = new Message(ascii, expectedReply);
        sendQueue.put(msg);

        System.out.println(msg.toString());
        return waitForReply(msg.getExpectedReply());
    }

    @Override
    public void addToSendQueue(String ascii, String expectedReply) {
        Message msg = new Message(ascii, expectedReply);
        sendQueue.add(msg);
    }

    @Override
    public boolean waitForReply(String expectedString) throws InterruptedException {
        Date start = new Date();
        Date now = new Date();
        while (((now.getTime() - start.getTime()) < TIMEOUT)
                && !replyQueue.contains(expectedString)) {
            Thread.sleep(500);
            now = new Date();
        }
        if (replyQueue.contains(expectedString)) {
            replyQueue.remove(expectedString);
            System.out.println("Got expected reply: " + expectedString);
            return true;
        } else {
            System.out.println("Did not got expected reply: " + expectedString);
            return false;
        }
    }

    @Override
    public String getFirstReply() {
        try {
            return replyQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void flushOutgoingQueue() {
        this.sendQueue.clear();
    }

    @Override
    public void flushIncommingQueue() {
        this.replyQueue.clear();
    }

    protected void addToReplyQueue(String reply) {
        replyQueue.add(reply);
    }

    protected LinkedBlockingQueue<Message> getSendQueue() {
        return sendQueue;
    }
}
