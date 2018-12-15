package de.htw.f4.techmobsys.multihop.beans;


public enum MessageCode {

    /**
     * Code for neighbor discovery.
     */
    NeigbourDiscovery("DISC"),
    /**
     * Code for discovering the current Coordinator of the network
     */
    KoordinatorDiscovery("CDIS"),
    /**
     * Code for Setting the Node's address
     */
    Address("ADDR"),
    /**
     * Code for a simple message
     */
    Message("MSSG"),
    /**
     * Code for self-polling to detect address collision
     */
    POLL("POLL"),
    /**
     * Code for self-polling to detect address collision
     */
    ALIV("ALIV"),

    AACK("AACK"),

    /**
     * Network restart needed
     */
    NRST("NRST");

    public String code;

    MessageCode(String s) {
        this.code = s;
    }

    public static MessageCode fromString(String text) throws EnumConstantNotPresentException {
        for (MessageCode b : MessageCode.values()) {
            if (b.code.equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new EnumConstantNotPresentException(MessageCode.class, text);
    }

    public String getCode() {
        return code;
    }
}

