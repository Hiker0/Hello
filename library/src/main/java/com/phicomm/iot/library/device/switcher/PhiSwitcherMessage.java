package com.phicomm.iot.library.device.switcher;

import com.phicomm.iot.library.message.PhiMessage;

/**
 * Created by allen.z on 2017-05-03.
 */
public class PhiSwitcherMessage extends PhiMessage {

    public final static byte CTR_ON = CTR_START+1;
    public final static byte CTR_OFF =CTR_START+2;;
    public final static byte CTR_CHECK_STATUS = CTR_START+3;

    public final static byte ACK_STATUS = ACK_START | CTR_CHECK_STATUS;

    public final static byte STATE_ON = 1;
    public final static byte STATE_OFF = 2;




    public static PhiMessage createOnMessage() {
        PhiMessage msg = new PhiMessage();
        msg.setFlag(CTR_ON);
        return msg;
    }

    public static PhiMessage createOffMessage() {
        PhiMessage msg = new PhiMessage();
        msg.setFlag(CTR_OFF);
        return msg;
    }

    public static PhiMessage queryStatus(){
        PhiMessage msg = new PhiMessage();
        msg.setFlag(CTR_CHECK_STATUS);
        return msg;
    }

    public static PhiMessage reportStatus(boolean on) {
        PhiMessage msg = new PhiMessage();
        msg.setFlag(ACK_STATUS);
        byte[] info = new byte[1];
        info[0] = on ? PhiSwitcherMessage.STATE_ON : PhiSwitcherMessage.STATE_OFF;
        msg.setInfo(info);
        return msg;
    }

}
