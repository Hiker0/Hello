package com.phicomm.iot.library.devices.switcher;

/**
 * Author: allen.z
 * Date  : 2017-05-18
 * last modified: 2017-05-18
 */
public interface SwitchInterface {
    interface ISwitcher{
        void turnOn();
        void turnOff();
        void qureyStatus();
    }

    interface IListener{
        void onStatusChange(boolean on);
    }
}
