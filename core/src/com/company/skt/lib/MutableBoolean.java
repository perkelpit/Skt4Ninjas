package com.company.skt.lib;

public class MutableBoolean {
    boolean value;

    public MutableBoolean() {
    }

    public MutableBoolean(boolean value) {
        this.value = value;
    }

    public void set(boolean value) {
        this.value = value;
    }

    public boolean isTrue() {
        return value;
    }
}
