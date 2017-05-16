package com.phicomm.iot.library.discover.broadcast;

import com.phicomm.iot.library.discover.PhiConstants;
import com.phicomm.iot.library.discover.PhiDiscoverPackage;
import com.phicomm.iot.library.discover.multicast.PhiDiscoverMuticastImpl;

import java.net.DatagramPacket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Johnson on 2017-04-14.
 */

public class Anouncer extends TimerTask {
    private final PhiDiscoverBroadcastImpl mDiscoverImpl;
    private int mCount = 0;
    Anouncer(PhiDiscoverBroadcastImpl discover){
        mDiscoverImpl = discover;
    }

    public void start(Timer timer){
        timer.schedule(this, 0, PhiConstants.ANOUNCER_INTERVAL);
    }

    @Override
    public void run() {
        byte[] data = EspressMessage.getQureyData().getBytes();
//        mDiscoverImpl.send(data);

        try {
            synchronized(this) {
                this.wait(10);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mDiscoverImpl.handleQurey();
        mCount++;
        if(mCount > PhiConstants.MAX_ANOUNCE_NUM){
            mDiscoverImpl.setState(PhiConstants.STATE_ANOUNCED);
            this.mDiscoverImpl.startRenewer();
        }
    }
}
