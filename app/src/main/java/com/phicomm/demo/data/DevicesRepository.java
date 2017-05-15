package com.phicomm.demo.data;

import android.support.annotation.NonNull;

import com.phicomm.demo.discovery.IIotAddress;
import com.phicomm.iot.library.device.BaseDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hzn on 17-5-10.
 */

public class DevicesRepository implements DevicesDataSource {
    private static DevicesRepository INSTANCE = null;

    Map<String, IIotAddress> mCachedIotAddress;
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

    public void syncIotAddresses(ArrayList<IIotAddress> iotAddresses) {
        mCachedDevices.clear();
        for (IIotAddress address : iotAddresses) {
            mCachedIotAddress.put(address.getBSSID(), address);
        }
    }

    @Override
    public void getDevices(@NonNull LoadDevicesCallback callback) {
        for (IIotAddress address : mCachedIotAddress.values()) {
            if (mCachedDevices.containsKey(address.getBSSID())) {
                BaseDevice device = mCachedDevices.get(address.getBSSID());
                // TODO: 17-5-10 update device's status
            } else {
                Device device = Device.from(address);
                mCachedDevices.put(address.getBSSID(), device);
            }
        }

        callback.onDeviceLoaded(new ArrayList<Device>(mCachedDevices.values()));
    }
}
