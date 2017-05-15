package com.phicomm.iot.library.remote.switcher;

import com.phicomm.iot.library.message.PhiMessage;
import com.phicomm.iot.library.devices.switcher.PhiSwitcherMessage;
import com.phicomm.iot.library.remote.PhiRemoteProtocol;

/**
 * Created by allen.z on 2017-05-05.
 */
public class PhiRemoteSwitcherProtocol extends PhiRemoteProtocol implements IRemoteSwitcher {
    RomateSwitcher switcher;
    public PhiRemoteSwitcherProtocol(RomateSwitcher device) {
        super(device);
        switcher = device;
    }

    @Override
    protected void handleDeviceMessage(PhiMessage msg) {
        if (msg.getFlag() == PhiSwitcherMessage.CTR_ON) {
            switcher.turnOn();
        } else if (msg.getFlag() == PhiSwitcherMessage.CTR_OFF) {
            switcher.turnOff();
        }
    }

    @Override
    protected void onBind() {
        reportStatus(switcher.isOn());
    }

    @Override
    public void reportStatus(boolean on) {

        PhiMessage msg = PhiSwitcherMessage.reportStatus(on);
       sendMessage(msg);
    }
}
