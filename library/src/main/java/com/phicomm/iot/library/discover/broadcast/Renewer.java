package com.phicomm.iot.library.discover.broadcast;

import com.phicomm.iot.library.discover.PhiConstants;
import com.phicomm.iot.library.discover.DiscoveredDevice;
import com.phicomm.iot.library.discover.PhiDiscoverPackage;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Johnson on 2017-04-14.
 */

public class Renewer extends TimerTask {
    static final String TAG = Renewer.class.getName();
    private final PhiDiscoverBroadcastImpl mDiscoverImpl;

    Renewer(PhiDiscoverBroadcastImpl discover){
        mDiscoverImpl = discover;
    }

    void start(Timer timer){
        timer.schedule(this, PhiConstants.RENEWAL_TTL_INTERVAL, PhiConstants.RENEWAL_TTL_INTERVAL);
    }

    @Override
    public void run() {
        ArrayList<DiscoveredDevice> devs = new ArrayList(mDiscoverImpl.getDevices());
        for(DiscoveredDevice dev: devs){
            dev.discreaseTTL();
            if(dev.isExpired()){
                mDiscoverImpl.getDevices().remove(dev);
                mDiscoverImpl.notifyDeviceRemove(dev);
            }
        }
        byte[] data = EspressMessage.getQureyData().getBytes();

//        mDiscoverImpl.send(data);
    }
}
