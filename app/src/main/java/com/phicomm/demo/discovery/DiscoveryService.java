package com.phicomm.demo.discovery;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.phicomm.discoverdevice.discoverlibrary.IDiscoverResultListener;
import com.phicomm.discoverdevice.discoverlibrary.MeshDiscoveryUtil;
import com.phicomm.discoverdevice.discoverlibrary.PhiIotDevice;

import java.util.ArrayList;
import java.util.List;

public class DiscoveryService extends IntentService {

    public static final String ACTION_UDP_DISCOVERY = "com.phicomm.demo.discovery.action.UDP_DISCOVERY";
    public static final String ACTION_DISCOVERY_RESULT = "com.phicomm.demo.discovery.action.DISCOVERY_RESULT";
    private static String TAG = "DiscoveryService";

    private static Context mContext;

    public DiscoveryService() {
        super("DiscoveryService");
    }

    public static void startActionUdpDiscovery(Context context) {
        mContext = context;
        Intent intent = new Intent(context, DiscoveryService.class);
        intent.setAction(ACTION_UDP_DISCOVERY);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UDP_DISCOVERY.equals(action)) {
                try {
                    handleActionUdpDiscovery();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleActionUdpDiscovery() throws Exception {
        // TODO: 17-5-10 add udp discovery
        MeshDiscoveryUtil mMeshDiscoveryUtil = new MeshDiscoveryUtil();
        mMeshDiscoveryUtil.setMeshDiscoverResultListener(mMeshDiscoverResultListener);
        Log.d(TAG, "handleActionUdpDiscovery new MeshDiscoveryUtil and start begin");
        try {
            mMeshDiscoveryUtil.discoverIOTDevices();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private IDiscoverResultListener mMeshDiscoverResultListener = new IDiscoverResultListener() {
        @Override
        public void onDeviceResultAdd(List<PhiIotDevice> resultList) {
            Log.d(TAG, "Mesh onDeviceResultAdd resultList.size()=" + resultList.size() + "send ACTION_DISCOVERY_RESULT broadcast");
            Intent i = new Intent(ACTION_DISCOVERY_RESULT);
            i.putExtra("resultType", "2222");
            i.putParcelableArrayListExtra("result", (ArrayList<? extends Parcelable>) resultList);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(i);
        }
    };
}
