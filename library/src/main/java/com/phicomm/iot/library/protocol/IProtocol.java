package com.phicomm.iot.library.protocol;

import com.phicomm.iot.library.message.BaseMessage;

/**
 * Created by allen.z on 2017-04-25.
 */
public interface IProtocol {
    public void sendMessage(BaseMessage msg);
    public void start();
    public void stop();
}
