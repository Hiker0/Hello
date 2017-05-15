package com.phicomm.iot.library.discover.multicast;

import android.util.Log;

import com.phicomm.iot.library.discover.PhiConstants;
import com.phicomm.iot.library.discover.PhiDiscoverPackage;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 * Created by Johnson on 2017-04-13.
 */

public class PhiSocketListener implements Runnable {
    public final static String TAG = "Discover/Listener";

    /**
     *
     */
    private final PhiDiscoverMuticastImpl mDiscoverImpl;

    /**
     * @param discver
     */
    PhiSocketListener(PhiDiscoverMuticastImpl discver)
    {
        this.mDiscoverImpl = discver;
    }
    @Override
    public void run() {

        try
        {
            byte buf[] = new byte[PhiConstants.MAX_MSG_ABSOLUTE];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            while (mDiscoverImpl.getState() != PhiConstants.STATE_CANCELED)
            {
                packet.setLength(buf.length);
                mDiscoverImpl.getSocket().receive(packet);

                if (mDiscoverImpl.getState() == PhiConstants.STATE_CANCELED)
                {
                    Log.d(TAG, "receive:STATE_CANCELED");
                    continue;
                }
                if(mDiscoverImpl.shouldIgnorePacket(packet)){
                    Log.d(TAG, "receive:ignore");
                    continue;
                }

                PhiDiscoverPackage phipackage = new PhiDiscoverPackage(packet);
                Log.d(TAG, "receive:" + phipackage.toString());
                if(phipackage.getFlag() == null) {
                    break;
                }else if(phipackage.getFlag().equals(PhiDiscoverPackage.FLAG_Q)){
                    mDiscoverImpl.handleQurey(phipackage);
                }else if(phipackage.getFlag().equals(PhiDiscoverPackage.FLAG_R)){
                    mDiscoverImpl.handleResponse(phipackage);
                }

//                try
//                {
////                    if (this.jmDNSImpl.getLocalHost().shouldIgnorePacket(packet))
////                    {
//////                    	Log.d(TAG, String.format("someone told me to ignore=%s", packet));
////                        continue;
////                    }
//                    throw new IOException();
//
//                }
//                catch (IOException e)
//                {
//                    Log.w(TAG, "run() exception ", e);
//                }
            }
        }
        catch (IOException e)
        {
            if (mDiscoverImpl.getState() != PhiConstants.STATE_CANCELED)
            {
                Log.w(TAG, "run() exception ", e);
               mDiscoverImpl.reStart();
            }
        }
    }
}
