package com.phicomm.iot.library.remote.light;

import com.phicomm.iot.library.device.BaseDevice;
import com.phicomm.iot.library.device.SmartDevice;
import com.phicomm.iot.library.remote.RemoteDevice;
import com.phicomm.iot.library.remote.light.IRemoteLight;
import com.phicomm.iot.library.remote.light.PhiRemoteLightProtocol;

/**
 * Created by allen.z on 2017-05-05.
 */
public class RomateLight extends RemoteDevice {
    IRemoteLight lightProtocol;
    RemoteLightListener mListener;
    byte brightness = 50;

    public RomateLight(BaseDevice dev) {
        super(dev, "");
        lightProtocol = new PhiRemoteLightProtocol(this);
    }


    public void open() {
        lightProtocol.start();
    }

    public void close() {
        lightProtocol.stop();
    }

    public void setBrightness(byte bright, boolean remote){
        brightness = bright;
        if(mListener != null && remote) {
            mListener.setBrightness(bright);
        }

        lightProtocol.reportStatus(bright);

    }

    public byte getBrightness(){
        return  brightness;
    }

    public void setListener(RemoteLightListener listener){
        mListener = listener;
        mListener.setBrightness(brightness);
    }
    public interface  RemoteLightListener {
        void setBrightness(int bright);
    }

}
