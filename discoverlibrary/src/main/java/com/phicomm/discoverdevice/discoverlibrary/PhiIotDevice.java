package com.phicomm.discoverdevice.discoverlibrary;

/**
 * Created by chunya02.li on 2017/5/17.
 */

import android.os.Parcel;
import android.support.annotation.NonNull;

import java.net.InetAddress;

/**
 * 以Sample开头的类,用来作示范
 */
public class PhiIotDevice implements IIotDevice {
    public static final Creator<PhiIotDevice> CREATOR = new Creator<PhiIotDevice>() {
        @Override
        public PhiIotDevice createFromParcel(Parcel in) {
            return new PhiIotDevice(in);
        }

        @Override
        public PhiIotDevice[] newArray(int size) {
            return new PhiIotDevice[size];
        }
    };
    private String mBSSID;
    private String mType;
    private InetAddress mLocalAddress;

    public PhiIotDevice(@NonNull String bssid, @NonNull String type, @NonNull InetAddress localAddress) {
        mBSSID = bssid;
        mType = type;
        mLocalAddress = localAddress;
    }

    protected PhiIotDevice(Parcel in) {
        String[] vals = new String[2];
        in.readStringArray(vals);
        this.mBSSID = vals[0];
        this.mType = vals[1];
        this.mLocalAddress = (InetAddress) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                mBSSID,
                mType
        });
        dest.writeSerializable(mLocalAddress);
    }

    @Override
    public String getBSSID() {
        return mBSSID;
    }

    @Override
    public String getType() {
        return mType;
    }

    @Override
    public InetAddress getLocalAddress() {
        return mLocalAddress;
    }
}