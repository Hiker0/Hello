package com.phicomm.iot.library.discover.multicast;

import android.util.Log;

import com.phicomm.iot.library.discover.PhiConstants;
import com.phicomm.iot.library.discover.PhiDiscoverPackage;
import com.phicomm.iot.library.discover.multicast.PhiDiscoverMuticastImpl;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Johnson on 2017-04-14.
 */

public class Anouncer extends TimerTask {
    static final String TAG = "Discover/Anouncer";
    private final PhiDiscoverMuticastImpl mDiscoverImpl;
    private int mCount = 0;
    Anouncer(PhiDiscoverMuticastImpl discover){
        mDiscoverImpl = discover;
    }

    public void start(Timer timer){
        timer.schedule(this, 0, PhiConstants.ANOUNCER_INTERVAL);
    }

    @Override
    public void run() {
        Log.d(TAG,"Anouncer once");
        PhiDiscoverPackage packet = new PhiDiscoverPackage();
        mDiscoverImpl.send(packet);

        try {
            synchronized(this) {
                this.wait(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mDiscoverImpl.handleQurey(packet);
        mCount++;
        if(mCount > PhiConstants.MAX_ANOUNCE_NUM){
            mDiscoverImpl.setState(PhiConstants.STATE_ANOUNCED);
            this.mDiscoverImpl.startRenewer();
        }
    }
}
