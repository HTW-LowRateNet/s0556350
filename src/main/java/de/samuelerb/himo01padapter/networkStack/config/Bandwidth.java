package de.samuelerb.himo01padapter.networkStack.config;

/**
 * Created by samuelerb on 28.04.19.
 * Matr_nr: s0556350
 * Package: de.htw.f4.techmobsys.multihop.networkStack.config
 */
public enum Bandwidth {
    _7_8KHz(0),
    _10_4KHz(1),
    _15_6KHz(2),
    _20_8KHz(3),
    _31_2KHz(4),
    _41_6KHz(5),
    _62_5KHz(6),
    _125KHz(7),
    _250KHz(8),
    _500KHz(9);

    private int index = 0;

    private Bandwidth(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    public static Bandwidth getInstance(String bandwidth) {
        return valueOf(bandwidth.toUpperCase());
    }
}
