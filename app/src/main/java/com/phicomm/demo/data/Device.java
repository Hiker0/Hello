package com.phicomm.demo.data;


public class Device {
    private String mBssid;

    public Device(String bssid) {
        mBssid = bssid;
    }

    public String getBssid() {
        return mBssid;
    }
}
