package com.phicomm.iot.library.device;

import android.os.Parcel;
import android.os.Parcelable;

import com.phicomm.iot.library.utils.JsonTool;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Johnson on 2017-04-25.
 */
public class BaseDevice implements Parcelable {
    protected String mName;
    protected TYPE mType;
    protected String mBrand;
    protected String mAccess;
    protected String mAddress;
    protected String mBssid;

    protected BaseDevice(Parcel in) {
        mName = in.readString();
        mType = TYPE.valueOf(in.readString());
        mBrand = in.readString();
        mAccess = in.readString();
        mAddress = in.readString();
        mBssid = in.readString();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mType.name());
        dest.writeString(mBrand);
        dest.writeString(mAccess);
        dest.writeString(mAddress);
        dest.writeString(mBssid);
    }

    public enum TYPE {
        UNKOWN("unkown",0),
        SWITCHER("switcher",1),
        SCOKET("socker",2),
        CURTAIN("curtain",3),
        ANDROID("android",4),
        LIGHT("Light",5);

        String name;
        int type;

        TYPE(String name,int type){
            this.name = name;
            this.type = type;
        }

//        @Override
//        public String toString() {
//            return name;
//        }

        public int getType(){
            return  type;
        }

    }

    public  BaseDevice (){

    }

    public BaseDevice(BaseDevice device){
        mName = device.getName();
        mAddress = device.getAddress();
        mType = device.getType();
        mBrand = device.getBrand();
        mAccess = device.getAccess();
        mBssid = device.getBssid();
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

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public TYPE getType() {
        return mType;
    }

    public void setType(TYPE type) {
        this.mType = type;
    }

    public void setTypeName(String type) {
        this.mType = TYPE.valueOf(type);
    }

    public String getBrand() {
        return mBrand;
    }

    public void setBrand(String brand) {
        this.mBrand = brand;
    }

    public String getAccess() {
        return mAccess;
    }

    public void setAccess(String access) {
        this.mAccess = access;
    }

    @Override
    public String toString() {
        return "BaseDevice{mName="+mName
                +" ,mType="+mType
                +" ,mAddress="+mAddress
                +" ,mBrand="+mBrand
                +" ,mAccess="+mAccess
                +" ,mBssid="+ mBssid
                +"}";
    }

    public String getJsonString(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name",mName);
            jsonObject.put("type",mType);
            jsonObject.put("address",mAddress);
            jsonObject.put("brand",mBrand);
            jsonObject.put("access",mAccess);
            jsonObject.put("bssid", mBssid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return JsonTool.getJsonString("BaseDevice",jsonObject);
    }

    public static BaseDevice fromJson(String json){
        BaseDevice dev = new BaseDevice();
        try {
            JSONObject jsonObject = new JSONObject(json);
            dev.setName(jsonObject.getString("name"));
            dev.setTypeName(jsonObject.getString("type"));
            dev.setAddress(jsonObject.getString("address"));
            dev.setBrand(jsonObject.getString("brand"));
            dev.setAccess(jsonObject.getString("access"));
            dev.setBssid(jsonObject.getString("bssid"));
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return dev;
    };
}
