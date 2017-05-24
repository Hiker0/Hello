package com.phicomm.smartconfig.protocol;

import android.util.Log;

import com.phicomm.smartconfig.task.ISmartConfigGenerator;
import com.phicomm.smartconfig.util.ByteUtil;

import java.net.InetAddress;

public class SmartConfigGenerator implements ISmartConfigGenerator {

	private static final String TAG = "EsptouchGenerator";

	private final byte[][] mGcBytes2;
	private final byte[][] mDcBytes2;

	/**
	 * Constructor of EsptouchGenerator, it will cost some time(maybe a bit
	 * much)
	 * 
	 * @param apSsid
	 *            the Ap's ssid
	 * @param apBssid
	 *            the Ap's bssid
	 * @param apPassword
	 *            the Ap's password
	 * @param inetAddress
	 *            the phone's or pad's local ip address allocated by Ap
	 * @param isSsidHidden
	 *            whether the Ap's ssid is hidden
	 */
	public SmartConfigGenerator(String apSsid, String apBssid, String apPassword,
                                InetAddress inetAddress, boolean isSsidHiden) {


		// generate guide code
		GuideCode gc = new GuideCode();
		char[] gcU81 = gc.getU8s();
		mGcBytes2 = new byte[gcU81.length][];
		Log.d(TAG, "apSsid =" + apSsid + " apBssid="+apBssid + "apPassword="+apPassword + "inetAddress="+inetAddress + "gcU81.length="+gcU81.length);
		for (int i = 0; i < mGcBytes2.length; i++) {
			Log.d(TAG,"genSpecBytes gcU81[i]="+gcU81[i]);
			mGcBytes2[i] = ByteUtil.genSpecBytes(gcU81[i]);
		}

		// generate data code
		DatumCode dc = new DatumCode(apSsid, apBssid, apPassword, inetAddress,
				isSsidHiden);
		char[] dcU81 = dc.getU8s();
		mDcBytes2 = new byte[dcU81.length][];
        Log.d(TAG," mDcBytes2.length="+mDcBytes2.length);
		for (int i = 0; i < mDcBytes2.length; i++) {
			Log.d(TAG," dcU81[i]="+dcU81[i]);
			mDcBytes2[i] = ByteUtil.genSpecBytes(dcU81[i]);
		}
	}

	@Override
	public byte[][] getGCBytes2() {
		return mGcBytes2;
	}

	@Override
	public byte[][] getDCBytes2() {
		return mDcBytes2;
	}

}
