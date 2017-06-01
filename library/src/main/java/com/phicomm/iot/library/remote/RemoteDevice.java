package com.phicomm.iot.library.remote;

import com.phicomm.iot.library.device.BaseDevice;

/**
 * Author: allen.z
 * Date  : 2017-05-31
 * last modified: 2017-05-31
 */

public class RemoteDevice extends BaseDevice {
    String mToken;

    public RemoteDevice(RemoteDevice dev) {
        super(dev);
        mToken = dev.getToken();
    }

    public RemoteDevice(BaseDevice dev, String token) {
        super(dev);
        mToken = token;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String mToken) {
        this.mToken = mToken;
    }
}
