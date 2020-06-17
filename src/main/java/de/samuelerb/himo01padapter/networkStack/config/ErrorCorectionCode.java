package de.samuelerb.himo01padapter.networkStack.config;

/**
 * Created by samuelerb on 28.04.19.
 * Matr_nr: s0556350
 * Package: de.htw.f4.techmobsys.multihop.networkStack.config
 */
public enum ErrorCorectionCode {
    /* 1: 4/5
     * 2: 4/6
     * 3: 4/7
     * 4: 4/8
     */

    _4_5(1),
    _4_6(2),
    _4_7(3),
    _4_8(4);

    private int index = 1;

    private ErrorCorectionCode(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static ErrorCorectionCode getInstance(String errorCorrectionCode) {
        return valueOf(errorCorrectionCode.toUpperCase());
    }
}
