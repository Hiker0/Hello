package com.phicomm.demo.data;

import android.support.annotation.NonNull;

import com.phicomm.discoverdevice.discoverlibrary.IIotDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hzn on 17-5-10.
 */

public class DevicesRepository implements DevicesDataSource {
    private static DevicesRepository INSTANCE = null;

    Map<String, IIotDevice> mCachedIotAddress;
    Map<String, Device> mCachedDevices;


    private DevicesRepository() {
        mCachedIotAddress = new HashMap<>(0);
        mCachedDevices = new HashMap<>(0);
    }

    public static DevicesRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DevicesRepository();
        }
        return INSTANCE;
    }

    public void syncIotAddresses(ArrayList<IIotDevice> iotAddresses) {
        mCachedDevices.clear();
        mCachedIotAddress.clear();
        for (IIotDevice address : iotAddresses) {
            mCachedIotAddress.put(address.getBSSID(), address);
        }
    }

    @Override
    public void getDevices(@NonNull LoadDevicesCallback callback) {
        for (IIotDevice address : mCachedIotAddress.values()) {
            if (mCachedDevices.containsKey(address.getBSSID())) {
                Device device = mCachedDevices.get(address.getBSSID());
                // TODO: 17-5-10 update device's status
            } else {
                Device device = Device.from(address);
                mCachedDevices.put(address.getBSSID(), device);
            }
        }

        callback.onDeviceLoaded(new ArrayList<Device>(mCachedDevices.values()));
    }
}
