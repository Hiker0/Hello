package com.phicomm.iot.library.devices.light;

import com.phicomm.iot.library.protocol.IProtocol;

/**
 * Created by allen.z on 2017-05-04.
 */
public interface ILightProtocol extends IProtocol {

    public void setBrightness(byte value);

    public void qureyStatus();
}
