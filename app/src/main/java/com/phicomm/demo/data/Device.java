package com.phicomm.demo.data;


import android.support.annotation.NonNull;

import com.phicomm.demo.discovery.IIotAddress;

import java.io.Serializable;
import java.net.InetAddress;

import static com.google.common.base.Preconditions.checkNotNull;

public class Device implements Serializable {

    private String mBssid;
    private String mType;
    private InetAddress mLocalAddress;

    public Device(@NonNull String bssid, @NonNull String type) {
        this(bssid, type, null);
    }

    public Device(@NonNull String bssid, @NonNull String type, InetAddress localAddress) {
        mBssid = bssid;
        mType = type;
        mLocalAddress = localAddress;
    }

    public static Device from(@NonNull IIotAddress iotAddress) {
        checkNotNull(iotAddress);
        return new Device(
                iotAddress.getBSSID(),
                iotAddress.getType(),
                iotAddress.getLocalAddress());
    }

    public String getType() {
        return mType;
    }

    public InetAddress getLocalAddress() {
        return mLocalAddress;
    }

    public void setLocalAddress(InetAddress localAddress) {
        mLocalAddress = localAddress;
    }

    public String getBssid() {
        return mBssid;
    }
}
