package com.phicomm.smartconfig;

import com.espressif.iot.esptouch.IEsptouchListener;
import com.espressif.iot.esptouchB.IEsptouchListenerB;

/**
 * Created by hanyuan.chen on 2017/4/13.
 */

public interface ISmartConfigAPI {

    Boolean addDevicesSyn(ISmartConfigListener mSmartConfigListener);

    Boolean addDevicesSyn(ISmartConfigListener mSmartConfigListener, int waitUdpTotalMillisecond);

    Boolean addDevicesSynA(IEsptouchListener mEsptouchListener);

    Boolean addDevicesSynA(IEsptouchListener mEsptouchListener, int waitUdpTotalMillisecond);

    Boolean addDevicesSynB(IEsptouchListenerB mEsptouchListener);

    Boolean addDevicesSynB(IEsptouchListenerB mEsptouchListener, int waitUdpTotalMillisecond);

    void interrupt();

    void interruptA();

    void interruptB();

    void setEXTRA_LEN(int len);

    void setTargetPort(int port);
}
