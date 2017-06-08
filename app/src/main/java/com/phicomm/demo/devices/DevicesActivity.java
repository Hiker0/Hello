package com.phicomm.demo.devices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.phicomm.demo.DemoApp;
import com.phicomm.demo.R;
import com.phicomm.demo.data.DevicesRepository;
import com.phicomm.demo.discovery.DiscoveryService;
import com.phicomm.demo.smartconfig.EsptouchDemoActivity;
import com.phicomm.demo.user.LoginActivity;
import com.phicomm.demo.util.ActivityUtils;
import com.phicomm.iot.library.device.IIotDevice;
import com.phicomm.iot.library.discover.internetDiscover.InternetDevicesDiscovery;
import com.phicomm.iot.library.discover.internetDiscover.InternetDevicesDiscovery.IInternetDiscoverResultListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DevicesActivity extends AppCompatActivity {
    private static String TAG = "DevicesActivity";
    private static final int MENU_OPTION_DEVICE_CONFIG = 1;
    private static final int REFRESH_BUTTON_SATTUS = 1;
    @BindView(R.id.layout_devices_login1)
    LinearLayout mLayoutLogin1;
    @BindView(R.id.layout_devices_login2)
    LinearLayout mLayoutLogin2;
    @BindView(R.id.tv_login_msg)
    TextView mTextLoginMsg;
    private DevicesPresenter mPresenter;
    private RadioGroup mDeviceGroup;
    private RadioButton mInternetDevices;
    private RadioButton mLocalDevices;
    private Button mRefreshButton;
    private ArrayList<IIotDevice> mLocalList = new ArrayList<>();
    private ArrayList<IIotDevice> mInternetList = new ArrayList<>();
    private InternetDevicesDiscovery mInternetDiscoveryInstance =null;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DiscoveryService.ACTION_DISCOVERY_RESULT)) {
                mLocalList = intent.getParcelableArrayListExtra("result");
                if (mLocalList != null) {
                    Log.d(TAG,"mLocalList.size()="+mLocalList.size());
                    mPresenter.handleIotAddress(mLocalList);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        ButterKnife.bind(this);

        DevicesFragment devicesFragment = (DevicesFragment) getSupportFragmentManager().findFragmentById(R.id.layout_devices_container);
        if (devicesFragment == null) {
            devicesFragment = DevicesFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), devicesFragment, R.id.layout_devices_container);
        }
        mPresenter = new DevicesPresenter(devicesFragment, DevicesRepository.getInstance());
        initView();
        setListener();
    }

    private void initView() {
        mDeviceGroup = (RadioGroup) findViewById(R.id.deviceGroup);
        mInternetDevices = (RadioButton) findViewById(R.id.internetDevice);
        mLocalDevices = (RadioButton) findViewById(R.id.localDevice);
        mRefreshButton = (Button) findViewById(R.id.refreshList);
    }

    private void setListener() {
        mDeviceGroup.setOnCheckedChangeListener(mDevicelistener);
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshDeviceList();
                mRefreshButton.setEnabled(false);
                mHandler.sendEmptyMessageDelayed(REFRESH_BUTTON_SATTUS,3000);

            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH_BUTTON_SATTUS:
                    mRefreshButton.setEnabled(true);
            }

        }
    };

    RadioGroup.OnCheckedChangeListener mDevicelistener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup Group, int Checkid) {
            //refreshDeviceList();
            if (R.id.internetDevice == Checkid) {
                if (mInternetList != null && mInternetDiscoveryInstance != null) {
                    mPresenter.handleIotAddress(mInternetList);
                } else {
                    startActionInternetDiscovery();
                }
            } else if (R.id.localDevice == Checkid) {
                if (DiscoveryService.bServiceRunning) {
                    mPresenter.handleIotAddress(mLocalList);
                } else {
                    DiscoveryService.startActionUdpDiscovery(getApplicationContext());
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        loadLoginPanel();
        refreshDeviceList();
        IntentFilter filter = new IntentFilter(DiscoveryService.ACTION_DISCOVERY_RESULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }

    public void refreshDeviceList() {
        int mOrigncheckId= mDeviceGroup.getCheckedRadioButtonId();
        Log.d(TAG,"Token="+getLoginOnToken());
        if (!getLoginOnToken().isEmpty()) {
            if (mInternetDevices.getVisibility() == View.GONE) {
                mInternetDevices.setVisibility(View.VISIBLE);
                mInternetDevices.setChecked(true);
                startActionInternetDiscovery();
            } else if (mOrigncheckId == R.id.localDevice) {
                DiscoveryService.startActionUdpDiscovery(this);
            } else if (mOrigncheckId == R.id.internetDevice) {
                startActionInternetDiscovery();
            }
        } else {
            mInternetDevices.setVisibility(View.GONE);
            mLocalDevices.setChecked(true);
            DiscoveryService.startActionUdpDiscovery(this);
        }
    }

    // This is for the internet Discovery
    private void startActionInternetDiscovery() {
        mInternetList.clear();
        mInternetDiscoveryInstance = InternetDevicesDiscovery.getInstance();
        mInternetDiscoveryInstance.setInternetDiscoverResultListener(mInternetDiscoverResultListener);
        mInternetDiscoveryInstance.startInternetDiscovery(getLoginOnToken());
    }

    IInternetDiscoverResultListener mInternetDiscoverResultListener = new IInternetDiscoverResultListener() {
        @Override
        public void onDeviceResultAdd(List<IIotDevice> devlist) {
            mInternetList = (ArrayList<IIotDevice>) devlist;
            mPresenter.handleIotAddress((ArrayList<IIotDevice>) devlist);
        }
    };

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
        DiscoveryService.stopActionUdpDiscovery(this);
        super.onDestroy();
    }

    @OnClick(R.id.bt_login_logout)
    public void clickLogout() {
        ((DemoApp) getApplication()).store().setIotToken("");
        loadLoginPanel();
        refreshDeviceList();
    }

    @OnClick(R.id.bt_login_login)
    public void clickLogin() {
        LoginActivity.start(this);
    }

    private void loadLoginPanel() {
        if (getLoginOnToken().isEmpty()) {
            mLayoutLogin1.setVisibility(View.VISIBLE);
            mLayoutLogin2.setVisibility(View.GONE);
        } else {
            mLayoutLogin1.setVisibility(View.GONE);
            mLayoutLogin2.setVisibility(View.VISIBLE);
            mTextLoginMsg.setText("登陆成功. token: " + getLoginOnToken());
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, DevicesActivity.class);
        context.startActivity(starter);
    }


    private String getLoginOnToken(){
        return ((DemoApp) getApplication()).store().getIotToken();
    }
}
