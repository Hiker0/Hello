package com.phicomm.demo.devices;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.phicomm.demo.R;
import com.phicomm.demo.util.ActivityUtils;

public class DevicesActivity extends AppCompatActivity {
    private DevicesPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        DevicesFragment devicesFragment = (DevicesFragment) getSupportFragmentManager().findFragmentById(R.id.layout_devices_container);
        if (devicesFragment == null) {
            devicesFragment = DevicesFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), devicesFragment, R.id.layout_devices_container);
        }

        mPresenter = new DevicesPresenter(devicesFragment);
        mPresenter.start();
    }

}
