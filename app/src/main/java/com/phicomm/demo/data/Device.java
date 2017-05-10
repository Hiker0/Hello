package com.phicomm.demo.data;


import com.phicomm.demo.discovery.IIotAddress;

public class Device {
    private String mBssid;

    public Device(String bssid) {
        mBssid = bssid;
    }

    public static Device from(IIotAddress address) {
        return new Device(address.getBSSID());
    }

    public String getBssid() {
        return mBssid;
    }
}
