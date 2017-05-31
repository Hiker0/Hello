package com.phicomm.demo.devices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.phicomm.demo.R;
import com.phicomm.demo.data.DevicesRepository;
import com.phicomm.demo.discovery.DiscoveryService;
import com.phicomm.demo.smartconfig.EsptouchDemoActivity;
import com.phicomm.demo.util.ActivityUtils;
import com.phicomm.iot.library.device.IIotDevice;

import java.util.ArrayList;

public class DevicesActivity extends AppCompatActivity {
    private DevicesPresenter mPresenter;
    private static String TAG = "DevicesActivity";
    private static final int MENU_OPTION_DEVICE_CONFIG = 1;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DiscoveryService.ACTION_DISCOVERY_RESULT)) {
                ArrayList<IIotDevice> iotAddresses = intent.getParcelableArrayListExtra("result");
                if(iotAddresses!= null) {
                    Log.d(TAG, "iotAddresses.size=" + iotAddresses.size());
                    mPresenter.handleIotAddress(iotAddresses);
                }
            }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_OPTION_DEVICE_CONFIG, Menu.NONE, R.string.deviceconfig);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case MENU_OPTION_DEVICE_CONFIG:
                Intent intent = new Intent();
                intent.setClass(this, EsptouchDemoActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
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
