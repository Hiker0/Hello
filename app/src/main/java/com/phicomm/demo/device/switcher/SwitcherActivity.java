package com.phicomm.demo.device.switcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.phicomm.demo.R;
import com.phicomm.demo.util.ActivityUtils;
import com.phicomm.iot.library.device.BaseDevice;

public class SwitcherActivity extends AppCompatActivity {
    private SwitcherPresenter mSwitcherPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plug);

        SwitcherFragment switcherFragment = (SwitcherFragment) getSupportFragmentManager().findFragmentById(R.id.layout_plug_container);
        if (switcherFragment == null) {
            switcherFragment = SwitcherFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), switcherFragment, R.id.layout_plug_container);
        }


        Intent intent = getIntent();
        BaseDevice device = (BaseDevice) intent.getSerializableExtra("device");

        mSwitcherPresenter = new SwitcherPresenter(switcherFragment,device);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
