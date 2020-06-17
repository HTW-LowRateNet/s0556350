package de.samuelerb.himo01padapter.serialAdapter.emulator;

import de.samuelerb.himo01padapter.serialAdapter.Message;
import de.samuelerb.himo01padapter.serialAdapter.SerialAdapter;

import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by samuelerb on 28.04.19.
 * Matr_nr: s0556350
 * Package: de.htw.f4.techmobsys.multihop.adapter.emulator
 */
public class EmuAdapter implements SerialAdapter {


    private LinkedBlockingQueue<Message> sendQueue;
    private LinkedBlockingQueue<String> replyQueue;
    private LinkedBlockingQueue<Message> sent;
    private static final int TIMEOUT = 2000;

    public EmuAdapter() {
        this.sendQueue = new LinkedBlockingQueue<>();
        this.replyQueue = new LinkedBlockingQueue<>();
        this.sent = new LinkedBlockingQueue<>();
    }

    /**
     * This method should implement a blocking
     * UART communication.
     *
     * @param ascii         The AT instruction sent via UART
     * @param expectedReply The received response
     * @return boolean true on success | false on failure
     * @throws Exception is thrown when something bad happened
     */
    @Override
    public boolean send(String ascii, String expectedReply) throws Exception {
        Message msg = new Message(ascii, expectedReply);
        sendQueue.put(msg);

        System.out.println(msg.toString());

        Date now = new Date();

        while (((now.getTime() - msg.getTimestamp().getTime()) < TIMEOUT)
                && !replyQueue.contains(msg.getExpectedReply())) {
            Thread.sleep(500);
            System.out.println("wait");
            System.out.println("TIMEOUT exceeded: " + ((now.getTime() - msg.getTimestamp().getTime()) < TIMEOUT));
            System.out.println("IN QUEUE: " + replyQueue.contains(msg.getExpectedReply()));
            now = new Date();
        }
        if (replyQueue.contains(msg.getExpectedReply())) {
            replyQueue.remove(msg.getExpectedReply());
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method should implement a non-blocking
     * UART communication.
     *
     * @param ascii         The AT instruction sent via UART
     * @param expectedReply The received response
     */
    @Override
    public void addToSendQueue(String ascii, String expectedReply) {
        sendQueue.add(new Message(ascii, expectedReply));
    }

    /**
     * This method should wait for a string
     *
     * @param expectedString The received string
     */
    @Override
    public boolean waitForReply(String expectedString) throws Exception {
        Thread.sleep(500);
        System.out.println("[->] " + expectedString);
        return true;
    }

    @Override
    public String getFirstReply() {
        try {
            return replyQueue.take();
        } catch (InterruptedException e) {
            return "";
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

    protected void addToReplyQueue(String string) {
        replyQueue.add(string);
    }

    protected LinkedBlockingQueue<Message> getSendQueue() {
        return sendQueue;
    }

    protected void addToSent(Message message) {
        this.sent.add(message);
    }

    protected Message getLastSentMessage() {
        try {
            return sent.take();
        } catch (InterruptedException e) {
            return null;
        }
    }

    public LinkedBlockingQueue<Message> getSent() {
        return sent;
    }

    @Override
    public String toString() {
        return this.getClass().getName() +
                "EmuAdapter{" +
                "sendQueue=" + sendQueue +
                ", replyQueue=" + replyQueue +
                ", sent=" + sent +
                '}';
    }
}
