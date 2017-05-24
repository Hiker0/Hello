package com.phicomm.iot.library.device;

/**
 * Created by chunya02.li on 2017/5/24.
 */

import android.os.Parcelable;

public interface IIotDevice extends Parcelable {
    String getBssid();

    TYPE getType();

    String getAddress();
}