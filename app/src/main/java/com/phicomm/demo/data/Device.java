package com.phicomm.demo.data;

import com.phicomm.demo.discovery.IIotAddress;
import com.phicomm.iot.library.device.BaseDevice;

/**
 * Created by allen.z on 2017-05-15.
 */
public class Device extends BaseDevice {
    Device(){

    }

    static Device from(IIotAddress address){
        Device device = new Device();
        device.setTypeName(address.getType());
        device.setBssid(address.getBSSID());
        device.setAddress(address.getLocalAddress().getHostAddress());
        return device;
    }
}
