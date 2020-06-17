package de.samuelerb.himo01padapter.networkStack;

/**
 * Created by samuelerb on 28.04.19.
 * Matr_nr: s0556350
 * Package: de.htw.f4.techmobsys.multihop.networkStack
 */
public class NetworkPacket {
    private String sourceAddress;
    private String destinationAddress;
    private String message;

    public NetworkPacket(String sourceAddress, String destinationAddress, String message) {
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.message = message;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public NetworkPacket setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
        return this;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public String getMessage() {
        return message;
    }

    public NetworkPacket setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        return "NetworkPacket{" +
                "destinationAddress='" + destinationAddress + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
