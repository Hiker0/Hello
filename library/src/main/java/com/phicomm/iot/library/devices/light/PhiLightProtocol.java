package com.phicomm.iot.library.devices.light;

import com.phicomm.iot.library.protocol.phi.PhiLightMessage;
import com.phicomm.iot.library.protocol.phi.PhiMessage;
import com.phicomm.iot.library.devices.switcher.PhiSwitcherMessage;
import com.phicomm.iot.library.protocol.phi.PhiProtocol;

/**
 * Created by allen.z on 2017-05-04.
 */
public class PhiLightProtocol extends PhiProtocol implements ILightProtocol{
    public final static String TAG = "PhiProtocol/Switcher";
    SmartLight mLight;

    public PhiLightProtocol(SmartLight device) {
        super(device);
        mLight = device;
    }

    public void qureyStatus(){
        PhiMessage msg = PhiLightMessage.queryStatus();
        sendMessage(msg);
    }

    @Override
    public void setBrightness(byte value) {
        PhiMessage msg = PhiLightMessage.setBrightness(value);
        sendMessage(msg);
    }


    @Override
    protected void handleDeviceMessage(PhiMessage msg) {
        if (msg.getFlag() == PhiSwitcherMessage.ACK_STATUS) {
            byte[] status = msg.getInfo();
            if(status.length >0){
                byte bright = status[0];
                mLight.onBrightChange(bright);
            }
        }
    }

}
