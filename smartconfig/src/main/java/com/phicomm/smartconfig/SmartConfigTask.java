package com.phicomm.smartconfig;

import android.content.Context;
import android.util.Log;

import com.phicomm.smartconfig.task.SmartConfigTaskParameter;
import com.phicomm.smartconfig.task.ISmartConfigTaskParameter;
import com.phicomm.smartconfig.task.__SmartConfigTask;

import java.util.List;

public class SmartConfigTask implements ISmartConfigTask {
	private static final String TAG = "IEsptouchTask";

	public __SmartConfigTask _mEsptouchTask;
	private ISmartConfigTaskParameter _mParameter;

	/**
	 * Constructor of EsptouchTask
	 * 
	 * @param apSsid
	 *            the Ap's ssid
	 * @param apBssid
	 *            the Ap's bssid
	 * @param apPassword
	 *            the Ap's password
	 * @param isSsidHidden
	 *            whether the Ap's ssid is hidden
	 * @param context
	 *            the Context of the Application
	 */
	public SmartConfigTask(String apSsid, String apBssid, String apPassword,
                           boolean isSsidHidden, Context context) {
		Log.d(TAG,"EsptouchTask here is init parameter and begin touchTask");
		_mParameter = new SmartConfigTaskParameter();
		_mEsptouchTask = new __SmartConfigTask(apSsid, apBssid, apPassword,
				context, _mParameter, isSsidHidden);
	}

	/**
	 * Constructor of EsptouchTask
	 * 
	 * @param apSsid
	 *            the Ap's ssid
	 * @param apBssid
	 *            the Ap's bssid
	 * @param apPassword
	 *            the Ap's password
	 * @param isSsidHidden
	 *            whether the Ap's ssid is hidden
	 * @param timeoutMillisecond
	 *            (it should be >= 15000+6000) millisecond of total timeout
	 * @param context
	 *            the Context of the Application
	 */
	public SmartConfigTask(String apSsid, String apBssid, String apPassword,
                           boolean isSsidHidden, int timeoutMillisecond, Context context) {
		_mParameter = new SmartConfigTaskParameter();
		_mParameter.setWaitUdpTotalMillisecond(timeoutMillisecond);
		_mEsptouchTask = new __SmartConfigTask(apSsid, apBssid, apPassword,
				context, _mParameter, isSsidHidden);
	}

	@Override
	public void interrupt() {
		_mEsptouchTask.interrupt();
	}

	@Override
	public ISmartConfigResult executeForResult() throws RuntimeException {
		return _mEsptouchTask.executeForResult();
	}

	@Override
	public boolean isCancelled() {
		return _mEsptouchTask.isCancelled();
	}

	@Override
	public List<ISmartConfigResult> executeForResults(int expectTaskResultCount)
			throws RuntimeException {
		if (expectTaskResultCount <= 0) {
			expectTaskResultCount = Integer.MAX_VALUE;
		}
		return _mEsptouchTask.executeForResults(expectTaskResultCount);
	}

	@Override
	public void setEsptouchListener(ISmartConfigListener esptouchListener) {
		_mEsptouchTask.setEsptouchListener(esptouchListener);
	}
}
