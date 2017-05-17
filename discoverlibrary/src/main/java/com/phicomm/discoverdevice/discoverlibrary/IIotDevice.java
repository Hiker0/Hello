package com.phicomm.discoverdevice.discoverlibrary;

/**
 * Created by chunya02.li on 2017/5/17.
 */

import android.os.Parcelable;

import java.net.InetAddress;

public interface IIotDevice extends Parcelable {
    String getBSSID();

    String getType();

    InetAddress getLocalAddress();
}
