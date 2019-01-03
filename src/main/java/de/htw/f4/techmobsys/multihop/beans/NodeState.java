package de.htw.f4.techmobsys.multihop.beans;

/**
 * Created by samuelerb on 03.01.19.
 * Matr_nr: s0556350
 * Package: de.htw.f4.techmobsys.multihop.beans
 */
public enum NodeState {
    NEW(0),
    COORDINATOR(1),
    CLIENT(2);


    int code;

    NodeState(int i) {
        this.code = i;
    }
}
