package org.pacemaker.utils;

/**
 * Created by colmcarew on 06/04/16.
 */
public enum PacemakerENUMs {
    NOTHING("nothing"), FRIENDS("friends"), PENDING("pending");

    private String s;

    private PacemakerENUMs(String s) {
        this.s = s;
    }

    public String getS() {
        return this.s;
    }
}

