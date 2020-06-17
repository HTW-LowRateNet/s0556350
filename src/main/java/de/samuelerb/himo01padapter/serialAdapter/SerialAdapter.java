package de.samuelerb.himo01padapter.serialAdapter;

/**
 * Created by samuelerb on 28.04.19.
 * Matr_nr: s0556350
 * Package: de.htw.f4.techmobsys.multihop.adapter.pi4j
 */
public interface SerialAdapter {

    /**
     * This method should implement a blocking
     * UART communication.
     *
     * @param ascii         The AT instruction sent via UART
     * @param expectedReply The received response
     * @return boolean true on success | false on failure
     * @throws Exception is thrown when something bad happened
     */
    boolean send(String ascii, String expectedReply) throws Exception;


    /**
     * This method should implement a non-blocking
     * UART communication.
     *
     * @param ascii         The AT instruction sent via UART
     * @param expectedReply The received response
     */
    void addToSendQueue(String ascii, String expectedReply);


    /**
     * This method should wait for a string
     *
     * @param expectedString The received string
     */
    boolean waitForReply(String expectedString) throws Exception;

    String getFirstReply();

    void flushOutgoingQueue();

    void flushIncommingQueue();

}
