package com.samgauck;

public class Utilities {
    private Utilities() {}
    public static String capitailze(String original) {
        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
    }
}
