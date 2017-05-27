package com.phicomm.demo.discovery;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.phicomm.iot.library.device.BaseDevice;
import com.phicomm.iot.library.device.IIotDevice;
import com.phicomm.iot.library.discover.IDiscoverResultListener;
import com.phicomm.iot.library.discover.MeshDiscoveryUtil;
import com.phicomm.iot.library.discover.internetDiscover.ICommandDeviceSynchronizeInternet;
import com.phicomm.iot.library.discover.internetDiscover.PhiCommandDeviceSynchronizeInternet;

import java.util.ArrayList;
import java.util.List;

import static com.phicomm.iot.library.discover.PhiConstants.bIsUserLogin;

public class DiscoveryService extends IntentService {

    public static final String ACTION_UDP_DISCOVERY = "com.phicomm.demo.discovery.action.UDP_DISCOVERY";
    public static final String ACTION_DISCOVERY_RESULT = "com.phicomm.demo.discovery.action.DISCOVERY_RESULT";
    private static String TAG = "DiscoveryService";

    private static Context mContext;
    Thread mMeshDiscoveryThread;

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
                    handleActionUdpDiscovery(bIsUserLogin,true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void handleActionUdpDiscovery(boolean serverRequired, boolean localRequired ) throws Exception {
        // TODO: 17-5-10 add udp discovery
        if(serverRequired){
            doCommandSynchronizeInternet();
        }
        if(localRequired) {
            MeshDiscoveryUtil mMeshDiscovery = new MeshDiscoveryUtil();
            mMeshDiscovery.setMeshDiscoverResultListener(mMeshDiscoverResultListener);
            mMeshDiscoveryThread = new Thread(mMeshDiscovery, "UdpDiscover");
            mMeshDiscoveryThread.start();
            Log.d(TAG, "handleActionUdpDiscovery new MeshDiscoveryUtil and start begin");
        }

    }

    private List<IIotDevice> doCommandSynchronizeInternet() {
        ICommandDeviceSynchronizeInternet action = new PhiCommandDeviceSynchronizeInternet();
        return action.doCommandSynchronizeInternet("userkey");
    }

    private IDiscoverResultListener mMeshDiscoverResultListener = new IDiscoverResultListener() {
        @Override
        public void onDeviceResultAdd(List<BaseDevice> resultList) {
            Log.d(TAG, "Mesh onDeviceResultAdd resultList.size()=" + resultList.size() + "send ACTION_DISCOVERY_RESULT broadcast");
            Intent i = new Intent(ACTION_DISCOVERY_RESULT);
            i.putExtra("resultType", "2222");
            i.putParcelableArrayListExtra("result", (ArrayList<? extends Parcelable>) resultList);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(i);
        }
    };
}
