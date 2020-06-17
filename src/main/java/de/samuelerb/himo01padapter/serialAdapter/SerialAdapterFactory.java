package de.samuelerb.himo01padapter.serialAdapter;

import com.pi4j.io.serial.*;
import com.pi4j.system.SystemInfo;
import de.samuelerb.himo01padapter.serialAdapter.emulator.EmuAdapter;
import de.samuelerb.himo01padapter.serialAdapter.pi4j.Pi4jAdapter;
import de.samuelerb.himo01padapter.serialAdapter.pi4j.RX;
import de.samuelerb.himo01padapter.serialAdapter.pi4j.TX;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by samuelerb on 28.04.19.
 * Matr_nr: s0556350
 * Package: de.htw.f4.techmobsys.multihop.adapter
 */

// TODO add Logger
public class SerialAdapterFactory {
    private static final int BAUDRATE = 115200;
    private static final int DATABITS = 8;
    private static final String PARITY = "NONE";
    private static final int STOPBITS = 1;
    private static final String FLOWCONTROL = "NONE";


    public static SerialAdapter createInstance(Type type) throws Exception {
        switch (type) {
            case USB:
                return createUSB();
            case EMULATOR:
                return createEmulator();
            case RASPBERRY:
                throw new Exception("Please use the other method to instantiate a SerialAdapter for Raspberry Pi");
            default:
                throw new Exception("Could not find this type");
        }
    }

    public static SerialAdapter createInstance(Type type, SystemInfo.BoardType boardType) throws Exception {
        switch (type) {
            case USB:
                throw new Exception("Please use the other method to instantiate a SerialAdapter for USB");
            case EMULATOR:
                return createEmulator();
            case RASPBERRY:
                return createPi4J(boardType);
            default:
                throw new Exception("Could not find this type");
        }
    }



    private static SerialAdapter createPi4J(SystemInfo.BoardType boardType) throws UnsatisfiedLinkError {
        Logger logger = Logger.getLogger(SerialAdapterFactory.class.getName());
        logger.setLevel(Level.ALL);
        final Serial serial = SerialFactory.createInstance();
        SerialConfig config = new SerialConfig();
        config.device(SerialPort.getDefaultPort(boardType))
                .baud(Baud.getInstance(BAUDRATE))
                .dataBits(DataBits.getInstance(DATABITS))
                .parity(Parity.getInstance(PARITY))
                .stopBits(StopBits.getInstance(STOPBITS))
                .flowControl(FlowControl.getInstance(FLOWCONTROL));
        try {
            serial.open(config);
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.WARNING, e.getMessage());
        }
        Pi4jAdapter pi4jAdapter = new Pi4jAdapter();
        RX rx = new RX(pi4jAdapter, serial, logger);
        TX tx = new TX(pi4jAdapter, serial, logger);
        Thread ttx = new Thread(tx);
        Thread trx = new Thread(rx);
        trx.start();
        ttx.start();
        return pi4jAdapter;
    }

    private static SerialAdapter createUSB() throws Exception {
        throw new Exception("Not yet implemented");
    }

    private static SerialAdapter createEmulator() {
        EmuAdapter emuAdapter = new EmuAdapter();

        System.out.println(emuAdapter);
        de.samuelerb.himo01padapter.serialAdapter.emulator.RX rx = new de.samuelerb.himo01padapter.serialAdapter.emulator.RX(emuAdapter);
        de.samuelerb.himo01padapter.serialAdapter.emulator.TX tx = new de.samuelerb.himo01padapter.serialAdapter.emulator.TX(emuAdapter);

        Thread trx = new Thread(rx);
        Thread ttx = new Thread(tx);

        trx.start();
        ttx.start();
        return emuAdapter;
    }

    public enum Type {
        RASPBERRY(0),
        USB(1),
        EMULATOR(2);

        private int index = 0;

        Type(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }


        public Type getInstance(String type) {
            return valueOf(type.toUpperCase());
        }

    }
}
