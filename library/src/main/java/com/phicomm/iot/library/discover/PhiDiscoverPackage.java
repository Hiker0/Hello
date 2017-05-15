package com.phicomm.iot.library.discover;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * Created by Johnson on 2017-04-13.
 */

public class PhiDiscoverPackage {
    public final static String IDENTIFY = "PHICOM";
    public final static String FLAG_Q = "querey";
    public final static String FLAG_R = "response";
    public final static String SPLIT= ":";

    String flag;
    String brand;
    String type;
    String name;
    String sn;

    InetAddress address;

    /**
     * format:flag:type:name:sn
     * @param type
     * @param name
     * @param sn
     */
    public PhiDiscoverPackage(String brand, String type, String name, String sn){
        this.brand = brand;
        this.type = type;
        this.name = name;
        this.sn = sn;
        this.flag = FLAG_R;
    }

    public PhiDiscoverPackage(){
        this.flag = FLAG_Q;
    }


    public PhiDiscoverPackage(DatagramPacket packet) throws IOException {
        address = packet.getAddress();
        byte[] data = packet.getData();
        int len = packet.getLength();
        int off = packet.getOffset();
        String strMsg = new String(data, off, len).trim();
        String[] msgs = strMsg.split("\\:");
        if(IDENTIFY.equals(msgs[0])) {
            if (msgs[1].equals(FLAG_Q)) {
                flag = FLAG_Q;
            } else if (msgs[1].equals(FLAG_R) && msgs.length >= 6) {
                flag = FLAG_R;
                brand = msgs[2];
                type = msgs[3];
                name = msgs[4];
                sn = msgs[5];
            }
        }
    }

    public byte[] finish() {
        byte[] data = new byte[PhiConstants.MAX_MSG_ABSOLUTE];
        String msg = IDENTIFY+SPLIT;
        if (flag.equals(FLAG_Q)) {
            msg = msg + flag + SPLIT;
        } else if (flag.equals(FLAG_R)) {
            msg = msg + flag + SPLIT + brand + SPLIT + type + SPLIT + name + SPLIT + sn + SPLIT;
        }
        return msg.getBytes();
    }

    public String getFlag(){
        return flag;
    }

    public void  setFlag(String flag){
        this.flag = flag;
    }

    @Override
    public String toString() {

       return "PhiDiscoverPackage{"
               + "flag=" + flag
               + ",brand="+brand
               + ",type="+type
               +", name=" + name
               +", sn=" +sn+",}";
    }
}
