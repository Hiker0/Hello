package com.phicomm.iot.library.remote.switcher;

import com.phicomm.iot.library.protocol.IProtocol;

/**
 * Created by allen.z on 2017-05-04.
 */
public interface IRemoteSwitcher extends IProtocol {
    public void reportStatus(boolean on);
}
