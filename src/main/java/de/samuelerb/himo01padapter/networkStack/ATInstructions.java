package de.samuelerb.himo01padapter.networkStack;


public class ATInstructions {

    final static private String AT = "AT";

    final static private String AT_RESET = "AT+RST";

    final static private String AT_VERSION = "AT+VER";

    final static private String AT_IDLE = "AT+IDLE";

    final static private String AT_SLEEP = "AT+SLEEP=1";

    final static private String AT_RECEIVE = "AT+RX";

    final static private String AT_RSSI = "AT+RSSI?";

    final static private String AT_ADDR = "AT+ADDR=";

    final static private String AT_GETADDR = "AT+ADDR?";

    final static private String AT_DEST = "AT+DEST=";

    final static private String AT_GETDST = "AT+DEST?";

    final static private String AT_CFG = "AT+CFG=";

    final static private String AT_SAVE = "AT+SAVE";

    final static private String AT_SEND = "AT+SEND=";

    public static String getAT() {
        return AT;
    }

    public static String getAT_RESET() {
        return AT_RESET;
    }

    public static String getAT_VERSION() {
        return AT_VERSION;
    }

    public static String getAT_IDLE() {
        return AT_IDLE;
    }

    public static String getAT_SLEEP() {
        return AT_SLEEP;
    }

    public static String getAT_RECEIVE() {
        return AT_RECEIVE;
    }

    public static String getAT_RSSI() {
        return AT_RSSI;
    }

    public static String getAT_ADDR(String addr) {
        return AT_ADDR + addr;
    }

    public static String getAT_GETADDR() {
        return AT_GETADDR;
    }

    public static String getAT_DEST(String dest) {
        return AT_DEST + dest;
    }

    public static String getAT_GETDST() {
        return AT_GETDST;
    }

    public static String getAT_CFG(String cfg) {
        return AT_CFG + cfg;
    }

    public static String getAT_SAVE() {
        return AT_SAVE;
    }

    public static String getAT_SEND(String msg) {
        return AT_SEND + msg;
    }
}
