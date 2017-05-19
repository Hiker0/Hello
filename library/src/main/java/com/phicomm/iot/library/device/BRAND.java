package com.phicomm.iot.library.device;

/**
 * Created by allen.z on 2017-05-15.
 */
public enum BRAND {
    PHICOMM("phicomm");

    String name;
    BRAND(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
