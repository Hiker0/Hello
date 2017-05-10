package com.phicomm.demo.discovery;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class DiscoveryService extends IntentService {
    public static final String ACTION_UDP_DISCOVERY = "com.phicomm.demo.discovery.action.UDP_DISCOVERY";
    public static final String ACTION_DISCOVERY_RESULT = "com.phicomm.demo.discovery.action.DISCOVERY_RESULT";

    public DiscoveryService() {
        super("DiscoveryService");
    }

    public static void startActionUdpDiscovery(Context context) {
        Intent intent = new Intent(context, DiscoveryService.class);
        intent.setAction(ACTION_UDP_DISCOVERY);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UDP_DISCOVERY.equals(action)) {
                handleActionUdpDiscovery();
            }
        }
    }

    private void handleActionUdpDiscovery() {
        // TODO: 17-5-10 add udp discovery
        ArrayList<SampleIotAddress> iotAddresses = new ArrayList<>();
        try {
            iotAddresses.add(new SampleIotAddress("XX:XX:UU:UU", "Plug", InetAddress.getByName("192.168.1.2")));
            iotAddresses.add(new SampleIotAddress("XX:XX:UU:FF", "Plug", InetAddress.getByName("192.168.1.3")));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        Intent i = new Intent(ACTION_DISCOVERY_RESULT);
        i.putExtra("resultType", "2222");
        i.putParcelableArrayListExtra("result", iotAddresses);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }
}
