package de.samuelerb.himo01padapter.networkStack.config;

/**
 * Created by samuelerb on 28.04.19.
 * Matr_nr: s0556350
 * Package: de.htw.f4.techmobsys.multihop.networkStack.config
 */
public enum SpreadFactor {
    /*
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

    _64(6),
    _128(7),
    _256(8),
    _512(9),
    _1024(10),
    _2048(11),
    _4096(12);

    private int index = 6;

    private SpreadFactor(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static SpreadFactor getInstance(String spreadFactor) {
        return valueOf(spreadFactor.toUpperCase());
    }
}
