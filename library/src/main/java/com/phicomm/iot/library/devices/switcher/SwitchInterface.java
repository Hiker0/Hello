package com.phicomm.iot.library.devices.switcher;

import com.phicomm.iot.library.protocol.IProtocol;

/**
 * Author: allen.z
 * Date  : 2017-05-18
 * last modified: 2017-05-18
 */
public interface SwitchInterface {
    interface ISwitcher extends IProtocol {
        void turnOn();
        void turnOff();
        void qureyStatus();
    }

    interface IListener{
        void onStatusChange(boolean on);
    }
}
