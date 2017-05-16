package com.phicomm.iot.library.discover;

import android.os.Parcel;

import com.phicomm.iot.library.device.BaseDevice;
import com.phicomm.iot.library.device.TYPE;

/**
 * Created by Johnson on 2017-04-13.
 */

public class DiscoveredDevice extends BaseDevice {
    static final int DEFAULT_TTL = 5;
    private int ttl;

    public DiscoveredDevice(String brand, TYPE type, String name, String bssid) {
        setBrandName(brand);
        try {
            setType(type);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        setName(name);
        setBssid(bssid);
        ttl = DEFAULT_TTL;
    }

    public DiscoveredDevice(PhiDiscoverPackage packet) {
        try {
            setTypeName(packet.type);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        setName(packet.name);
        setBssid(packet.bssid);
        ttl = DEFAULT_TTL;
        setAddress(packet.address.getHostAddress());
    }

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
