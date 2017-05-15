package com.phicomm.demo.device.plug;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.phicomm.demo.R;
import com.phicomm.demo.util.ActivityUtils;
import com.phicomm.iot.library.device.BaseDevice;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlugActivity extends AppCompatActivity {
    private PlugPresenter mPlugPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plug);

        PlugFragment plugFragment = (PlugFragment) getSupportFragmentManager().findFragmentById(R.id.layout_plug_container);
        if (plugFragment == null) {
            plugFragment = PlugFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), plugFragment, R.id.layout_plug_container);
        }

        mPlugPresenter = new PlugPresenter(plugFragment);

        Intent intent = getIntent();
        BaseDevice device = (BaseDevice) intent.getSerializableExtra("device");
        mPlugPresenter.setPlug(device);
    }
}
