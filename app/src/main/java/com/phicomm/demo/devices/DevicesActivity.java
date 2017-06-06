package com.phicomm.demo.devices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.phicomm.iot.library.discover.PhiConstants;
import com.phicomm.iot.library.discover.internetDiscover.ICommandDeviceSynchronizeInternet;
import com.phicomm.iot.library.discover.internetDiscover.PhiCommandDeviceSynchronizeInternet;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DevicesActivity extends AppCompatActivity {
    private static String TAG = "DevicesActivity";
    private static final int MENU_OPTION_DEVICE_CONFIG = 1;
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
    private ArrayList<IIotDevice> mLocalList = new ArrayList<>();
    private ArrayList<IIotDevice> mInternetList;
    private ICommandDeviceSynchronizeInternet mSynchronizeAction = null;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DiscoveryService.ACTION_DISCOVERY_RESULT)) {
                mLocalList = intent.getParcelableArrayListExtra("result");
                if (mLocalList != null) {
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
    }

    private void setListener() {
        mDeviceGroup.setOnCheckedChangeListener(mDevicelistener);
    }

    RadioGroup.OnCheckedChangeListener mDevicelistener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup Group, int Checkid) {
            if (R.id.internetDevice == Checkid) {
                if (mSynchronizeAction != null && mSynchronizeAction.getInternetDeviceList() != null) {
                    mPresenter.handleIotAddress((ArrayList<IIotDevice>) mSynchronizeAction.getInternetDeviceList());
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
        if (R.id.localDevice == mDeviceGroup.getCheckedRadioButtonId()) {
            DiscoveryService.startActionUdpDiscovery(this);
        } else if (R.id.internetDevice == mDeviceGroup.getCheckedRadioButtonId()) {
            startActionInternetDiscovery();
        }
        IntentFilter filter = new IntentFilter(DiscoveryService.ACTION_DISCOVERY_RESULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }

    // This is for the internet Discovery
    private void startActionInternetDiscovery() {
        Callable<ArrayList<IIotDevice>> taskInternet = null;
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<ArrayList<IIotDevice>> futureInternet = null;
        taskInternet = new Callable<ArrayList<IIotDevice>>() {
            @Override
            public ArrayList<IIotDevice> call() throws Exception {
                return doCommandSynchronizeInternet(PhiConstants.UserKey);
            }
        };
        futureInternet = executor.submit(taskInternet);
        try {
            mInternetList = futureInternet.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (mInternetList != null) {
            mPresenter.handleIotAddress(mInternetList);
        }
        executor.shutdown();
    }

    private ArrayList<IIotDevice> doCommandSynchronizeInternet(String userKey) {
        mSynchronizeAction = new PhiCommandDeviceSynchronizeInternet();
        return mSynchronizeAction.doCommandSynchronizeInternet(userKey);
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
        DiscoveryService.stopActionUdpDiscovery(this);
        super.onDestroy();
    }

    @OnClick(R.id.bt_login_logout)
    public void clickLogout() {
        ((DemoApp) getApplication()).store().setIotToken("");
        loadLoginPanel();
    }

    @OnClick(R.id.bt_login_login)
    public void clickLogin() {
        LoginActivity.start(this);
    }

    private void loadLoginPanel() {
        if (((DemoApp) getApplication()).store().getIotToken().isEmpty()) {
            mLayoutLogin1.setVisibility(View.VISIBLE);
            mLayoutLogin2.setVisibility(View.GONE);
        } else {
            mLayoutLogin1.setVisibility(View.GONE);
            mLayoutLogin2.setVisibility(View.VISIBLE);
            mTextLoginMsg.setText("登陆成功. token: " + ((DemoApp) getApplication()).store().getIotToken());
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, DevicesActivity.class);
        context.startActivity(starter);
    }
}
