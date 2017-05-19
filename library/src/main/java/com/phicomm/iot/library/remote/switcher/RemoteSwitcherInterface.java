package com.phicomm.iot.library.remote.switcher;

import com.phicomm.iot.library.protocol.IProtocol;

/**
 * Created by allen.z on 2017-05-04.
 */
interface RemoteSwitcherInterface {

    interface ISwitcher {
        void turnOn();
        void turnOff();
        boolean isOn();
    }

    interface IReporter extends IProtocol{
        public void reportStatus(boolean on);
    }
}
