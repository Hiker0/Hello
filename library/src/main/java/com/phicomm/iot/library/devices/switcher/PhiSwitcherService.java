package com.phicomm.iot.library.devices.switcher;

import com.phicomm.iot.library.protocol.phi.PhiMessage;
import com.phicomm.iot.library.protocol.phi.PhiProtocol;

/**
 * Created by allen.z on 2017-05-04.
 */
public class PhiSwitcherService extends PhiProtocol implements SwitchInterface.ISwitcher {
    public final static String TAG = "PhiProtocol/Switcher";
    SmartSwitcher switcher;
    public PhiSwitcherService(SmartSwitcher device) {
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
                    switcher.onStatusChange(true);
                }else if(status[0] == PhiSwitcherMessage.STATE_OFF){
                    switcher.onStatusChange(false);
                }
            }
        }
    }

}
