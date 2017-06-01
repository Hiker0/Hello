package com.phicomm.iot.library.device;

import android.os.Parcel;
import android.support.annotation.NonNull;

/**
 * Created by Johnson on 2017-04-25.
 */
public class BaseDevice implements IIotDevice {
    protected String mName;
    protected TYPE mType;
    protected BRAND mBrand;
    protected String mAddress;
    protected String mBssid;
    protected String mToken;
    protected boolean bIsLocalAddress;

    public static final Creator<BaseDevice> CREATOR = new Creator<BaseDevice>() {
        @Override
        public BaseDevice createFromParcel(Parcel in) {
            return new BaseDevice(in);
        }

        @Override
        public BaseDevice[] newArray(int size) {
            return new BaseDevice[size];
        }
    };

    public BaseDevice() {

    }
    protected BaseDevice(Parcel in) {
        String[] vals = new String[2];
        in.readStringArray(vals);
        this.mBssid = vals[0];
        this.mType = TYPE.getTypeEnumByString(vals[1]);
        this.mAddress = (String) in.readSerializable();
    }

    public BaseDevice(@NonNull BaseDevice device) {
        mAddress = device.getAddress();
        mType = device.getType();
        mBrand = device.getBrand();
        mBssid = device.getBssid();
        bIsLocalAddress =device.getIsLocalAddress();
        mToken = device.getToken();
    }

    public BaseDevice(@NonNull TYPE type, String addr, @NonNull String bssid) {
        this(type, addr, bssid, BRAND.PHICOMM,true);
    }

    public BaseDevice(@NonNull TYPE type, String addr, @NonNull String bssid, @NonNull BRAND brand, @NonNull boolean IsLocalAddress) {
        mAddress = addr;
        mType = type;
        mBrand = brand;
        mBssid = bssid;
        mName = "phicomm";
        bIsLocalAddress = IsLocalAddress;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public String getBssid() {
        return mBssid;
    }

    public void setBssid(String bssid) {
        this.mBssid = bssid;
    }

    public TYPE getType() {
        return mType;
    }

    public void setType(TYPE type) {
        this.mType = type;
    }

    public void setTypeName(String type) {
        this.mType = TYPE.getTypeEnumByString(type);
    }

    public BRAND getBrand() {
        return mBrand;
    }

    public void setBrand(BRAND brand) {
        this.mBrand = brand;
    }

    public void setBrandName(String brand) {
        this.mBrand = BRAND.valueOf(brand);
    }

    public void setIsLocalAddress(boolean bIsLocalAddress) {
        this.bIsLocalAddress = bIsLocalAddress;
    }
    public boolean getIsLocalAddress() {
        return bIsLocalAddress;
    }

    public void setToken(String token) {
        this.mToken = token;
    }
    public String getToken() {
        return mToken;
    }

    @Override
    public String toString() {
        return "BaseDevice{mType=" + mType
                + " ,mAddress=" + mAddress
                + " ,mBrand=" + mBrand
                + " ,mBssid=" + mBssid
                + "}";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                mBssid,
                mType.toString()
        });
        dest.writeSerializable(mAddress);
    }
}
