package com.phicomm.iot.library.discover;

import android.os.Parcel;

import com.phicomm.iot.library.device.BaseDevice;

/**
 * Created by Johnson on 2017-04-13.
 */

public class DiscoveredDevice extends BaseDevice {
    static final int DEFAULT_TTL = 5;
    private int ttl;

    protected DiscoveredDevice(Parcel in) {
        super(in);
    }

    public DiscoveredDevice(String brand, TYPE type, String name, String sn) {
        setBrand(brand);
        try {
            setType(type);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        setName(name);
        setBssid(sn);
        ttl = DEFAULT_TTL;
    }

    public DiscoveredDevice(PhiDiscoverPackage packet) {
        try {
            setTypeName(packet.type);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        setName(packet.name);
        setBssid(packet.sn);
        ttl = DEFAULT_TTL;
        setAddress(packet.address.getHostAddress());
    }

    public static final Creator<BaseDevice> CREATOR = new Creator<BaseDevice>() {
        @Override
        public DiscoveredDevice createFromParcel(Parcel in) {
            return new DiscoveredDevice(in);
        }

        @Override
        public DiscoveredDevice[] newArray(int size) {
            return new DiscoveredDevice[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DiscoveredDevice) {
            DiscoveredDevice old = (DiscoveredDevice) obj;
            if (mBssid.equals(old.mBssid)) {
                return true;
            }
        }
        return super.equals(obj);
    }

    public void resetTTL() {
        ttl = DEFAULT_TTL;
    }

    public void increaseTTL() {
        ttl++;
    }

    public void discreaseTTL() {
        ttl--;
    }

    public boolean isExpired() {
        return ttl < 0;
    }
}