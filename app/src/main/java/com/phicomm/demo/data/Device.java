package com.phicomm.demo.data;

import com.phicomm.iot.library.device.BaseDevice;
import com.phicomm.iot.library.device.IIotDevice;

/**
 * Created by allen.z on 2017-05-15.
 */
public class Device extends BaseDevice {
    Device(){

    }
    static Device from(IIotDevice address){
        Device device = new Device();
        device.setTypeName(address.getType().toString());
        device.setBssid(address.getBssid());
        device.setAddress(address.getAddress());
        device.setToken(address.getToken());
        return device;
    }
}
