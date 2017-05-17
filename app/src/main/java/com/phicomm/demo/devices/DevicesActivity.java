package com.phicomm.demo.devices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.phicomm.demo.R;
import com.phicomm.demo.data.DevicesRepository;
import com.phicomm.demo.discovery.DiscoveryService;
import com.phicomm.demo.util.ActivityUtils;
import com.phicomm.discoverdevice.discoverlibrary.IIotDevice;

import java.util.ArrayList;

public class DevicesActivity extends AppCompatActivity {
    private DevicesPresenter mPresenter;
    private static String TAG = "DevicesActivity";
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<IIotDevice> iotAddresses = intent.getParcelableArrayListExtra("result");
            mPresenter.handleIotAddress(iotAddresses);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        DevicesFragment devicesFragment = (DevicesFragment) getSupportFragmentManager().findFragmentById(R.id.layout_devices_container);
        if (devicesFragment == null) {
            devicesFragment = DevicesFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), devicesFragment, R.id.layout_devices_container);
        }

        mPresenter = new DevicesPresenter(devicesFragment, DevicesRepository.getInstance());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume() startActionUdpDiscovery and RegisterReceiver");
        DiscoveryService.startActionUdpDiscovery(this);

        IntentFilter filter = new IntentFilter(DiscoveryService.ACTION_DISCOVERY_RESULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onDestroy();
    }
}
