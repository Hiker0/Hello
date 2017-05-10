package com.phicomm.demo.discovery;

import android.os.Parcel;

/**
 * 以Sample开头的类,用来作示范
 */
public class SampleIotAddress implements IIotAddress {
    public static final Creator<SampleIotAddress> CREATOR = new Creator<SampleIotAddress>() {
        @Override
        public SampleIotAddress createFromParcel(Parcel in) {
            return new SampleIotAddress(in);
        }

        @Override
        public SampleIotAddress[] newArray(int size) {
            return new SampleIotAddress[size];
        }
    };
    private String mBSSID;

    public SampleIotAddress(String bssid) {
        this.mBSSID = bssid;
    }

    protected SampleIotAddress(Parcel in) {
        this.mBSSID = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mBSSID);
    }

    @Override
    public String getBSSID() {
        return mBSSID;
    }
}
