package org.pacemaker.utils;

/**
 * Created by colmcarew on 06/04/16.
 */

/**
 * ENUMs used throughout this app
 */
public enum PacemakerENUMs {
    NOTHING("nothing"), FRIENDS("friends"), PENDING("pending"), FRIENDSORNOT("friendsornot"), MYFRIEND("myfriend"),
    INCORRECTCHANGE("IncorrectChange"), SELECTEDACTIVITY("selectedactivity");

    private String s;

    private PacemakerENUMs(String s) {
        this.s = s;
    }

    public String getS() {
        return this.s;
    }
}

