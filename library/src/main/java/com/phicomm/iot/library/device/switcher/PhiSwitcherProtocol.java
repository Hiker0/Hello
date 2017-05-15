package com.phicomm.iot.library.device.switcher;

import com.phicomm.iot.library.message.PhiMessage;
import com.phicomm.iot.library.protocol.PhiProtocol;

/**
 * Created by allen.z on 2017-05-04.
 */
public class PhiSwitcherProtocol extends PhiProtocol implements ISwitcherProtocol {
    public final static String TAG = "PhiProtocol/Switcher";
    SmartSwitcher switcher;
    public PhiSwitcherProtocol(SmartSwitcher device) {
        super(device);
        switcher = device;
    }

    public void qureyStatus(){
        PhiMessage msg = PhiSwitcherMessage.queryStatus();
        sendMessage(msg);
    }

    @Override
    public void turnOn() {
        PhiMessage msg = PhiSwitcherMessage.createOnMessage();
        sendMessage(msg);
    }

    @Override
    public void turnOff() {
        PhiMessage msg = PhiSwitcherMessage.createOffMessage();
        sendMessage(msg);
    }


    @Override
    protected void handleDeviceMessage(PhiMessage msg) {
        if (msg.getFlag() == PhiSwitcherMessage.ACK_STATUS) {
            byte[] status = msg.getInfo();
            if(status.length >0){
                if(status[0] == PhiSwitcherMessage.STATE_ON){
                    switcher.onStateChange(true);
                }else if(status[0] == PhiSwitcherMessage.STATE_OFF){
                    switcher.onStateChange(false);
                }
            }
        }
    }

}
