package com.phicomm.demo.discovery;


import android.os.Parcelable;

import java.net.InetAddress;

public interface IIotAddress extends Parcelable {
    String getBSSID();

    String getType();

    InetAddress getLocalAddress();
}
