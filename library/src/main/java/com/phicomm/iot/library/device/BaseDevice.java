package com.phicomm.iot.library.device;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.phicomm.iot.library.utils.JsonTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Johnson on 2017-04-25.
 */
public class BaseDevice implements Serializable {
    protected String mName;
    protected TYPE mType;
    protected BRAND mBrand;
    protected String mAddress;
    protected String mBssid;

    public BaseDevice() {

    }

    public BaseDevice(@NonNull BaseDevice device) {
        mAddress = device.getAddress();
        mType = device.getType();
        mBrand = device.getBrand();
        mBssid = device.getBssid();
    }

    public BaseDevice(@NonNull TYPE type, @NonNull String addr, @NonNull String bssid) {
        this(type, addr, bssid, BRAND.PHICOMM);
    }

    public BaseDevice(@NonNull TYPE type, @NonNull String addr, @NonNull String bssid, @NonNull BRAND brand) {
        mAddress = addr;
        mType = type;
        mBrand = brand;
        mBssid = bssid;
        mName = "phicomm";
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
        ;
    }

    @Override
    public String toString() {
        return "BaseDevice{mType=" + mType
                + " ,mAddress=" + mAddress
                + " ,mBrand=" + mBrand
                + " ,mBssid=" + mBssid
                + "}";
    }
}
