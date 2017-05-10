package com.phicomm.demo.device;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.phicomm.demo.R;
import com.phicomm.demo.data.Device;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlugActivity extends AppCompatActivity {
    @BindView(R.id.tv_plug_bssid)
    TextView mTvBssid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plug);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Device device = (Device) intent.getSerializableExtra("device");

        mTvBssid.setText(device.getBssid());
    }
}
