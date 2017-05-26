package com.phicomm.demo.device.light;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.phicomm.demo.R;
import com.phicomm.demo.util.ActivityUtils;
import com.phicomm.iot.library.device.BaseDevice;

public class LightActivity extends AppCompatActivity {
    private LightPresenter mLightPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plug);

        LightFragment LightFragment = (LightFragment) getSupportFragmentManager().findFragmentById(R.id.layout_plug_container);
        if (LightFragment == null) {
            LightFragment = LightFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), LightFragment, R.id.layout_plug_container);
        }


        Intent intent = getIntent();
        BaseDevice device = (BaseDevice) intent.getParcelableExtra("device");

        mLightPresenter = new LightPresenter(LightFragment,device);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
