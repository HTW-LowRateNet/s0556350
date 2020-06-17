package de.samuelerb.himo01padapter.networkStack.config;

/**
 * Created by samuelerb on 28.04.19.
 * Matr_nr: s0556350
 * Package: de.htw.f4.techmobsys.multihop.networkStack
 */
public class Config {
    private String configString = "433000000,20,6,10,1,1,0,0,0,0,3000,8,4";

    /*
     * Trägerfrequenz, wenn das Modul arbeitet, in Dezimalzahlen, ausgedrückt in 9 Zeichen
     *
     * Bereich:
     * 410_000_000 MHz - 470_000_000 MHz
     */
    private int frequency;

    /*
     * Sendeleistung, dezimal, ausgedrückt in 2 Zeichen
     * Bereich:
     * 5 dBm - 20 dBm
     */
    private int power;

    /*
     * Die Bandbreite des belegten Kanals wird übertragen:
     * Je größer die Bandbreite, desto schneller werden die Daten übertragen,
     * desto geringer ist jedoch die Empfindlichkeit.
     *
     * Bereich:
     * Im Konfigurationsbefehl wird nur der Bandbreitencode verwendet,
     * und die tatsächliche Bandbreite wird nicht angezeigt.
     * 0: 7,8 KHz
     * 1: 10,4 KHz
     * 2: 15,6 KHz
     * 3: 20,8 KHz
     * 4: 31,2 KHz
     * 5: 41,6 KHz
     * 6: 62,5 KHz
     * 7: 125 KHz
     * 8: 250 KHz
     * 9: 500 KHz
     */
    private Bandwidth bandwidth;

    /*
     * Die Schlüsselparameter der Spread-Spectrum-Kommunikation sind,
     * je größer der Spreading-Faktor ist,
     * desto langsamer werden die Daten gesendet,
     * desto höher ist jedoch die Empfindlichkeit.
     * Im Konfigurationsbefehl wird nur der Code des Spreizfaktors verwendet,
     * und der tatsächliche Spreizfaktor wird nicht angezeigt.
     *
     * Bereich:
     * 64-4096 ist die Entsprechung zwischen Code und Spreizfaktor wie folgt:
     * 6: 64
     * 7: 128
     * 8: 256
     * 9: 512
     * 10: 1024
     * 11: 2048
     * 12: 4096
     */
    private SpreadFactor spreadFactor;

    /*
     * Für die Schlüsselparameter der Spread-Spectrum-Kommunikation
     * wird im Konfigurationsbefehl nur der Code des Fehlerkorrekturcodes
     * verwendet, und der eigentliche Fehlerkorrekturcode wird nicht angezeigt.
     *
     * Bereich:
     * In 4/5 - 4/8 ist die Entsprechung zwischen dem Code und dem
     * Fehlerkorrekturcode wie folgt:
     * 1: 4/5
     * 2: 4/6
     * 3: 4/7
     * 4: 4/8
     */
    private ErrorCorectionCode errorCorrectionCode;

    /*
     * CRC-Prüfung der Benutzerdaten
     * Bereich:
     * 0: Aus
     * 1: Ein
     */
    private int crc;

    /*
     * Bereich:
     * 0: Explizit
     * 1: Implizit
     */
    private int implicitHeader;

    /*
     * Empfangsmoduseinstellung
     * Bereich:
     * 0: kontinuierlich
     * 1: einzeln
     */
    private int singleReceive;

    /*
     * Bereich:
     * 0: Nicht unterstützt
     * 1: Unterstützung
     */
    private int frequencyModulation;

    /*
     * Keine Infos vorhanden
     *
     * Standardwert: 0
     */
    private int frequencyModulationPeriod;

    /*
     * Timeout-Zeit für Datenempfang:
     * Wenn im Einzelempfangsmodus die Datensoftware nicht über diese Zeit hinaus empfangen wurde,
     * meldet das Modul einen Timeout-Fehler und wechselt automatisch
     * in Dezimal- Schreibweise in Millisekunden in den SLEEP-Modus.
     *
     * Bereich:
     * 1-65535
     */
    private int timeout;

    /*
     *Benutzerdatenlänge, Dezimaldarstellung:
     * Anwendung im impliziten Header- Modus,
     * gibt die Länge der vom Modul gesendeten und
     * empfangenen Daten an
     * (diese Länge = tatsächliche Benutzerdatenlänge +4).
     * Der Anzeigekopf ist ungültig.
     *
     * Bereich:
     * 5-255
     */
    private int length;

    /*
     * Präambellänge, Dezimaldarstellung
     * Bereich:
     * 4 - 65535
     */
    private int preampleLength;


    public Config() {
        this.frequency = 0;
        this.power = 20;
        this.bandwidth = Bandwidth._62_5KHz;
        this.spreadFactor = SpreadFactor._1024;
        this.errorCorrectionCode = ErrorCorectionCode._4_5;
        this.crc = 0;
        this.implicitHeader = 0;
        this.singleReceive = 0;
        this.frequencyModulation = 0;
        this.timeout = 0;
        this.length = 0;
        this.preampleLength = 0;
    }


    public Config setFrequency(int frequency) {
        if (frequency >= 410_000_000 && frequency < 470_000_000)
            this.frequency = frequency;
        else
            throw new IndexOutOfBoundsException("frequency is out of bounds: 410_000_000 < frequency < 470_000_000");
        return this;
    }

    public Config setPower(int power) {
        if (power >= 5 && power <= 20)
            this.power = power;
        else
            throw new IndexOutOfBoundsException();
        return this;
    }

    public Config setBandwidth(Bandwidth bandwidth) {
        this.bandwidth = bandwidth;
        return this;
    }

    public Config setSpreadFactor(SpreadFactor spreadFactor) {
        this.spreadFactor = spreadFactor;
        return this;
    }

    public Config setErrorCorrectionCode(ErrorCorectionCode errorCorrectionCode) {
        this.errorCorrectionCode = errorCorrectionCode;
        return this;
    }

    public Config setCrc(int crc) {
        this.crc = crc;
        return this;
    }

    public Config setImplicitHeader(int implicitHeader) {
        this.implicitHeader = implicitHeader;
        return this;
    }

    public Config setSingleReceive(int singleReceive) {
        this.singleReceive = singleReceive;
        return this;
    }

    public Config setFrequencyModulation(int frequencyModulation) {
        this.frequencyModulation = frequencyModulation;
        return this;
    }

    public Config setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public Config setLength(int length) {
        this.length = length;
        return this;
    }

    public Config setPreampleLength(int preampleLength) {
        this.preampleLength = preampleLength;
        return this;
    }

    public Config setFrequencyModulationPeriod(int frequencyModulationPeriod) {
        this.frequencyModulationPeriod = frequencyModulationPeriod;
        return this;
    }

    public Config setConfigString(String configString) {
        this.configString = configString;
        return this;
    }

    public String getConfigString() {
        this.configString = String.valueOf(frequency) + "," +
                String.valueOf(power) + "," +
                String.valueOf(bandwidth.getIndex()) + "," +
                String.valueOf(spreadFactor.getIndex()) + "," +
                String.valueOf(errorCorrectionCode.getIndex()) + "," +
                String.valueOf(crc) + "," +
                String.valueOf(implicitHeader) + "," +
                String.valueOf(singleReceive) + "," +
                String.valueOf(frequencyModulation) + "," +
                String.valueOf(frequencyModulationPeriod) + "," +
                String.valueOf(timeout) + "," +
                String.valueOf(length) + "," +
                String.valueOf(preampleLength);
        return configString;
    }
}
