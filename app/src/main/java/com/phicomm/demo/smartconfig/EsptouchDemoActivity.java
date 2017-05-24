package com.phicomm.demo.smartconfig;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.espressif.iot.esptouch.IEsptouchListener;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouchB.IEsptouchListenerB;
import com.espressif.iot.esptouchB.IEsptouchResultB;
import com.phicomm.demo.R;
import com.phicomm.smartconfig.ISmartConfigListener;
import com.phicomm.smartconfig.ISmartConfigResult;
import com.phicomm.smartconfig.SmartConfigAPI;


public class EsptouchDemoActivity extends Activity implements OnClickListener {

	private TextView mTvApSsid;
	private EditText mEdtApPassword;
	private Button mBtnConfirm;
	private Switch mSwitchIsSsidHidden;
	private EspWifiAdminSimple mWifiAdmin;
	private Spinner mSpinnerTaskResultCount;
	private Spinner mSpinnerTaskCount;

	private int type;
	private final int ESP_NO_SDK = 0x10;
	private final int A_ESP_SDK = 0x11;
	private final int B_ESP_SDK = 0x12;
	private SmartConfigAPI mSmartConfigAPI;
	private ProgressDialog mProgressDialog;
    private String bssid;
    private String address;
    private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.esptouch_demo_activity);

		mWifiAdmin = new EspWifiAdminSimple(this);
		mTvApSsid = (TextView) findViewById(R.id.tvApSssidConnected);
		mEdtApPassword = (EditText) findViewById(R.id.edtApPassword);
		mBtnConfirm = (Button) findViewById(R.id.btnConfirm);
		mSwitchIsSsidHidden = (Switch) findViewById(R.id.switchIsSsidHidden);
		mBtnConfirm.setOnClickListener(this);
		initSpinner();
	}
	
	private void initSpinner() {
		mSpinnerTaskResultCount = (Spinner) findViewById(R.id.spinnerTaskResultCount);
		int[] spinnerItemsInt = getResources().getIntArray(R.array.taskResultCount);
		int length = spinnerItemsInt.length;
		Integer[] spinnerItemsInteger = new Integer[length];
		for(int i=0;i<length;i++)
		{
			spinnerItemsInteger[i] = spinnerItemsInt[i];
		}
		ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,
				android.R.layout.simple_list_item_1, spinnerItemsInteger);
		mSpinnerTaskResultCount.setAdapter(adapter);
		mSpinnerTaskResultCount.setSelection(1);

		mSpinnerTaskCount = (Spinner) findViewById(R.id.spinnerTaskCount);
		int[] spinnerItemsInt2 = getResources().getIntArray(R.array.taskCount);
		int length2 = spinnerItemsInt2.length;
		Integer[] spinnerItemsInteger2 = new Integer[length2];
		for(int i=0;i<length2;i++)
		{
			spinnerItemsInteger2[i] = spinnerItemsInt2[i];
		}
		ArrayAdapter<Integer> adapter2 = new ArrayAdapter<Integer>(this,
				android.R.layout.simple_list_item_1, spinnerItemsInteger2);
		mSpinnerTaskCount.setAdapter(adapter2);
		mSpinnerTaskCount.setSelection(0);
	}

	@Override
	protected void onResume() {
		super.onResume();
		String apSsid = mWifiAdmin.getWifiConnectedSsid();
		if (apSsid != null) {
			mTvApSsid.setText(apSsid);
		} else {
			mTvApSsid.setText("");
		}
	}

	@Override
	public void onClick(View v) {
		if (v == mBtnConfirm) {
			String apSsid = mTvApSsid.getText().toString();
			String apPassword = mEdtApPassword.getText().toString();
			String apBssid = mWifiAdmin.getWifiConnectedBssid();
			Boolean isSsidHidden = mSwitchIsSsidHidden.isChecked();
			int taskResultCount = mSpinnerTaskCount.getSelectedItemPosition();
			type = (int) mSpinnerTaskCount.getSelectedItem();
            mSmartConfigAPI = new SmartConfigAPI(apSsid, apBssid, apPassword,
					isSsidHidden, taskResultCount, EsptouchDemoActivity.this);
			new EsptouchAsyncTask().execute();
		}
	}

	private class EsptouchAsyncTask extends AsyncTask<String, Void, Boolean> {

		private final Object mLock = new Object();

		@Override
		protected void onPreExecute() {
			mProgressDialog = new ProgressDialog(EsptouchDemoActivity.this);
			mProgressDialog.setMessage("Esptouch is configuring, please wait for a moment...");
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					synchronized (mLock) {
						switch (type){
							case ESP_NO_SDK:
							    mSmartConfigAPI.interrupt();
								break;
							case A_ESP_SDK:
                                mSmartConfigAPI.interruptA();
								break;
							case B_ESP_SDK:
                                mSmartConfigAPI.interruptB();
								break;
						}
					}
				}
			});
			mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE,
					"Waiting...", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			mProgressDialog.show();
			mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
		}

		@Override
		protected Boolean doInBackground(String... params) {
			switch (type){
				case ESP_NO_SDK:
					return mSmartConfigAPI.addDevicesSyn(mSmartConfigListener);
				case A_ESP_SDK:
					return mSmartConfigAPI.addDevicesSynA(mEsptouchListenerA);
				case B_ESP_SDK:
					return mSmartConfigAPI.addDevicesSynB(mEsptouchListenerB);
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
			mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText("Confirm");
			if (!result) {
				mProgressDialog.setMessage("Esptouch fail");
			} else {
				mProgressDialog.setMessage("Esptouch success");
			}
		}
	}

	private ISmartConfigListener mSmartConfigListener = new ISmartConfigListener() {
		@Override
		public void onEsptouchResultAdded(ISmartConfigResult result) {
			bssid = result.getBssid();
			address = result.getInetAddress().getHostAddress();
			name = result.getInetAddress().getHostName();
			resultToast();
		}
	};

	private IEsptouchListener mEsptouchListenerA = new IEsptouchListener() {
		@Override
		public void onEsptouchResultAdded(IEsptouchResult result) {
			bssid = result.getBssid();
			address = result.getInetAddress().getHostAddress();
			name = result.getInetAddress().getHostName();
            resultToast();
		}
	};

	private IEsptouchListenerB mEsptouchListenerB = new IEsptouchListenerB() {
		@Override
		public void onEsptouchResultAdded(IEsptouchResultB result) {
			bssid = result.getBssid();
			address = result.getInetAddress().getHostAddress();
			name = result.getInetAddress().getHostName();
            resultToast();
		}
	};

	private void resultToast() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String text = "Esptouch success, bssid = " + bssid + ",InetAddress = " + address + ",name = " +name;
                Toast.makeText(EsptouchDemoActivity.this, text, Toast.LENGTH_LONG).show();
            }
        });
    }
}
