package com.phicomm.iot.library.message;

/**
 * Created by allen.z on 2017-04-26.
 */
public abstract class BaseMessage {
    private String addr;
    private int port;
    BaseMessage(){

    }

    public String getAddress() {
        return addr;
    }

    public void setAddress(String addr) {
        this.addr = addr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public abstract byte[] toByte();
    public abstract int dataLength();
}
