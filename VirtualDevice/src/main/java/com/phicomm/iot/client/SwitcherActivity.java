package com.phicomm.iot.client;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.phicomm.iot.library.device.BaseDevice;
import com.phicomm.iot.library.device.TYPE;
import com.phicomm.iot.library.discover.broadcast.PhiDiscoverBroadcastImpl;
import com.phicomm.iot.library.remote.switcher.RomateSwitcher;
import com.phicomm.iot.library.discover.DiscoveredDevice;
import com.phicomm.iot.library.discover.PhiDiscover;
import com.phicomm.iot.library.discover.multicast.PhiDiscoverMuticastImpl;
import com.phicomm.iot.library.utils.Utils;
import com.phicomm.iot.library.widgets.LightView;
import com.phicomm.iot.library.widgets.SwitcherView;

import java.io.IOException;

/**
 * Created by allen.z on 2017-04-27.
 */
public class SwitcherActivity extends Activity {
    boolean isOn = false;
    RomateSwitcher remoteSwitcher;
    LightView lightView;
    TextView infoView;
    SwitcherView mSwitcherView;
    private PhiDiscover mDiscover;
    WifiManager.MulticastLock multicastLock;
    public static final int IOT_DEVICE_PORT = 1025 ;
    public static final int IOT_APP_PORT = 4025;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        |WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        |WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        |WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        |WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_switcher);

        WifiManager wifiManager=(WifiManager)getSystemService(Context.WIFI_SERVICE);
        multicastLock=wifiManager.createMulticastLock("multicast.test");
        multicastLock.acquire();

        mSwitcherView = (SwitcherView) findViewById(R.id.switcher);
        lightView = (LightView) findViewById(R.id.light);
        infoView = (TextView) findViewById(R.id.info);

        mSwitcherView.setListener(new SwitcherView.Listener() {
            @Override
            public void onTurnOn() {
                if (remoteSwitcher != null) {
                    remoteSwitcher.reportStatus(true);
                }
                lightView.turnOn();
            }

            @Override
            public void onTurnOff() {
                if (remoteSwitcher != null) {
                    remoteSwitcher.reportStatus(false);
                }
                lightView.turnOff();
            }
        });
        regester();
    }

    void regester() {
        DiscoveredDevice host = new DiscoveredDevice("PHICOMM", TYPE.SWITCHER, Build.DEVICE, "18:fe:34:9a:b1:4c");
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int mIpAddress = wifiInfo.getIpAddress();
        String ip = Utils.intToIp(mIpAddress);
        host.setAddress(ip);
        try {
            mDiscover = new PhiDiscoverBroadcastImpl(IOT_APP_PORT, IOT_DEVICE_PORT);
            mDiscover.setHost(host);
        } catch (IOException e) {
            mDiscover = null;
            e.printStackTrace();
        }

        try {
            if (mDiscover != null) {
                mDiscover.startResearchDevice();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DeviceListActivity", "Discover start fail", e);
        }

        infoView.setText(host.toString());
        remoteSwitcher = new RomateSwitcher(host);
        remoteSwitcher.open();
        remoteSwitcher.setListener(new RomateSwitcher.RemoteSwitchListener() {
            @Override
            public void turnOn() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwitcherView.turnOn();
                        lightView.turnOn();
                    }
                });
            }

            @Override
            public void turnOff() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lightView.turnOff();
                        mSwitcherView.turnOff();
                    }
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mDiscover.cancel();
        remoteSwitcher.close();
        multicastLock.release();
        super.onDestroy();
    }

}
