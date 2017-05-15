package com.phicomm.iot.library.device;

import com.phicomm.iot.library.message.BaseMessage;
import com.phicomm.iot.library.protocol.IProtocol;

import java.net.InetAddress;

/**
 * Created by Johnson on 2017-04-25.
 */
public abstract class SmartDevice extends BaseDevice {

    IProtocol mProtocol;

    public SmartDevice(BaseDevice dev){
        super(dev);
    }

    public void setProtocol(IProtocol protocol){
        if(mProtocol!=null && mProtocol != protocol){
            mProtocol.stop();
        }
        mProtocol = protocol;
    }

    public abstract void  onConnectSuccess();

    public abstract void open();

    public abstract void close();
}
