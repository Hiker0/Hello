package com.phicomm.iot.library.protocol.phi;

import com.phicomm.iot.library.protocol.BaseMessage;

import java.net.DatagramPacket;

/**
 * Created by allen.z on 2017-04-26.
 */
public class PhiMessage extends BaseMessage {
    final static char START_A = 'P';
    final static char START_B = 'H';
    final static char START_C = 'I';

    //*****************************************//
    public final static byte FLAG_NOP = 0;

    public final static byte ACK_START = (byte) 0x80;

    public final static byte CTR_START = 40;
    //*******************************************//

    private byte[] mInfo;
    private byte mFlag = FLAG_NOP;

    public PhiMessage(){
    }

    public byte getFlag() {
        return mFlag;
    }
    public void setFlag(byte flag) {
        mFlag = flag;
    }

    public void setInfo(byte[] info) {
        mInfo = info;
    }

    public byte[] getInfo() {
        return mInfo;
    }
    public static PhiMessage fromDatagram(DatagramPacket datagramPacket){

        int len = datagramPacket.getLength();
        int offset = datagramPacket.getOffset();
        byte[]data = datagramPacket.getData();
        if(len - offset < 6){
            return null;
        }

        if(data[offset]==START_A
                && data[offset+1] == START_B
                && data[offset+2]==START_C){
            PhiMessage msg = new PhiMessage();
            msg.setAddress(datagramPacket.getAddress().getHostAddress());
            msg.setPort(datagramPacket.getPort());
            msg.setFlag(data[offset+3]);

            short size = (short)((data[offset+4] << 8) | (data[offset+5] & 0xff));
            byte[] dd = new byte[size];
            for(int i=0; i< size; i++){
                dd[i] =  data[offset+6+i];
            }
            msg.setInfo(dd);
            return msg;
        }

        return null;
    }

    public static boolean isPhiMessage(byte[] data){
        if(data.length < 6){
            return false;
        }

        if(data[0]==START_A
                && data[1] == START_B
                && data[3]==START_C){
            return true;
        }
        return  false;
    }

    @Override
    public String toString() {
        return "PhiMessage{"+"mFlag ="+mFlag+"}";
    }

    @Override
    public byte[] toByte() {
        int len = 0;
        if(mInfo != null){
            len =  getInfo().length;
        }
        byte[] data = new byte[6 + len];
        data[0] = START_A;
        data[1] = START_B;
        data[2] = START_C;
        data[3] = mFlag;
        data[4] = (byte) (len >> 8);
        data[5] = (byte) len;

        for (int i = 0; i < len; i++) {
            data[i + 6] = mInfo[i];
        }
        return data;
    }

    @Override
    public int dataLength() {
        int len = 0;
        if(mInfo != null){
            len =  getInfo().length;
        }
        return len + 6;
    }
}
