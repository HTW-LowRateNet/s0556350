package de.samuelerb.himo01padapter.serialAdapter;

import java.util.Date;

/**
 * Created by samuelerb on 28.04.19.
 * Matr_nr: s0556350
 * Package: de.htw.f4.techmobsys.multihop.adapter
 */
public class Message {
    private String msg;
    private String expectedReply;
    private Date timestamp;

    public Message(String msg, String expectedReply) {
        this.msg = msg;
        this.expectedReply = expectedReply;
        this.timestamp = new Date();
    }

    public String getMsg() {
        return msg;
    }

    public String getExpectedReply() {
        return expectedReply;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "msg='" + msg + '\'' +
                ", expectedReply='" + expectedReply + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
