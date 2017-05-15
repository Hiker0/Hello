package com.phicomm.iot.library.message;

/**
 * Created by allen.z on 2017-05-03.
 */
public class PhiLightMessage extends PhiMessage{

    public final static byte CTR_SET_VALUE= CTR_START+1;
    public final static byte CTR_CHECK_STATUS = CTR_START+3;

    public final static byte ACK_STATUS = ACK_START | CTR_CHECK_STATUS;





    public static PhiMessage setBrightness(byte bright) {
        PhiMessage msg = new PhiMessage();
        msg.setFlag(CTR_SET_VALUE);
        byte[] info = new byte[1];
        info[0] = bright;
        msg.setInfo(info);
        return msg;
    }

    public static PhiMessage queryStatus(){
        PhiMessage msg = new PhiMessage();
        msg.setFlag(CTR_CHECK_STATUS);
        return msg;
    }

    public static PhiMessage reportStatus(byte bright) {
        PhiMessage msg = new PhiMessage();
        msg.setFlag(ACK_STATUS);
        byte[] info = new byte[1];
        info[0] = bright;
        msg.setInfo(info);
        return msg;
    }

}
