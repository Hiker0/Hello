package com.phicomm.iot.library.message;

/**
 * Created by allen.z on 2017-05-03.
 */
public class PhiSystemMessage extends PhiMessage {

    public final static byte REQ_WHO = 1;
    public final static byte REQ_CONFIRM = 2;
    public final static byte REQ_BIND = 3;
    public final static byte REQ_CHECK_LIVE = 4;
    public final static byte REQ_END = 40;

    public final static byte ACK_WHO = ACK_START | REQ_WHO;
    public final static byte ACK_CONFIRM = ACK_START | REQ_CONFIRM;
    public final static byte ACK_BIND = ACK_START | REQ_BIND;
    public final static byte ACK_CHECK_LIVE = ACK_START | REQ_CHECK_LIVE;

    //**********************************************//
    public final static byte CONFIRM_SUCCESS = 0;
    public final static byte CONFIRM_TIMEOUT = -1;
    public final static byte CONFIRM_FAIL = -2;

    public final static byte BIND_SUCCESS = 0;
    public final static byte BIND_TIMEOUT = -1;
    public final static byte BIND_REFUSE = -2;
    //**********************************************//
    public final static byte STATE_ALIVE = 0;
    public final static byte STATE_UN_ALIVE = -1;
    //**********************************************//

    public static PhiMessage getBindMessage() {
        PhiMessage msg = new PhiMessage();
        msg.setFlag(REQ_BIND);
        return msg;
    }

    public static PhiMessage getCheckMessage(String ident) {
        PhiMessage msg = new PhiMessage();
        msg.setFlag(REQ_CONFIRM);
        byte[] data = ident.trim().getBytes();
        msg.setInfo(data);
        return msg;
    }

    public static PhiMessage getReAliveMessage(byte alive) {
        PhiMessage msg = new PhiMessage();
        msg.setFlag(ACK_CHECK_LIVE);
        byte[] data = new byte[1];
        data[0] = alive;
        msg.setInfo(data);
        return msg;
    }

    public static PhiMessage replyCheckMessage(boolean confirm) {
        PhiMessage outMessage = new PhiMessage();
        outMessage.setFlag(PhiSystemMessage.ACK_CONFIRM);
        byte[] info = new byte[1];
        info[0] = confirm ? CONFIRM_SUCCESS : CONFIRM_FAIL;
        outMessage.setInfo(info);
        return outMessage;
    }

    public static PhiMessage replyBindMessage(boolean bind) {
        PhiMessage outMessage = new PhiMessage();
        outMessage.setFlag(PhiSystemMessage.ACK_BIND);
        byte[] info = new byte[1];
        info[0] = bind ? BIND_SUCCESS: BIND_REFUSE;
        outMessage.setInfo(info);
        return outMessage;
    }
}
