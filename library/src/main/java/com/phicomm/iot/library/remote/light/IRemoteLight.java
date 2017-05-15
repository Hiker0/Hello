package com.phicomm.iot.library.remote.light;

import com.phicomm.iot.library.protocol.IProtocol;

/**
 * Created by allen.z on 2017-05-04.
 */
public interface IRemoteLight extends IProtocol {
    public void reportStatus(byte bright);
}
