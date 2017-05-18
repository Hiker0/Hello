package com.phicomm.iot.library.remote.light;

import com.phicomm.iot.library.protocol.phi.PhiLightMessage;
import com.phicomm.iot.library.protocol.phi.PhiMessage;
import com.phicomm.iot.library.remote.PhiRemoteProtocol;

/**
 * Created by allen.z on 2017-05-05.
 */
public class PhiRemoteLightProtocol extends PhiRemoteProtocol implements IRemoteLight {
    RomateLight light;
    public PhiRemoteLightProtocol(RomateLight device) {
        super(device);
        light = device;
    }

    @Override
    protected void handleDeviceMessage(PhiMessage msg) {
        if (msg.getFlag() == PhiLightMessage.CTR_SET_VALUE) {
            byte[] info = msg.getInfo();
            if(info.length > 0){
                light.setBrightness(info[0], true);
            }
        }
    }

    @Override
    protected void onBind() {
        reportStatus(light.getBrightness());
    }

    @Override
    public void reportStatus(byte bright) {
        PhiMessage msg = PhiLightMessage.reportStatus(bright);
        sendMessage(msg);
    }
}
