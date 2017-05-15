package com.phicomm.iot.library.devices.switcher;

import com.phicomm.iot.library.protocol.IProtocol;

/**
 * Created by allen.z on 2017-05-04.
 */
public interface ISwitcherProtocol extends IProtocol {
    public void turnOn();

    public void turnOff();

    public void qureyStatus();
}
