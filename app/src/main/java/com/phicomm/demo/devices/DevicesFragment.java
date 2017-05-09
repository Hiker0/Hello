package com.phicomm.demo.devices;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phicomm.demo.R;
import com.phicomm.demo.data.Device;
import com.phicomm.demo.devices.DevicesContract.Presenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class DevicesFragment extends Fragment implements DevicesContract.View {
    private final static String TAG = DevicesFragment.class.getSimpleName();
    @BindView(R.id.list_devices)
    RecyclerView mListDevices;
    private DevicesContract.Presenter mPresenter;
    private DevicesAdapter mDevicesAdapter;


    public DevicesFragment() {

    }

    public static DevicesFragment newInstance() {
        return new DevicesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_devices, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Log.d("hzn", "222222222222222");
        mListDevices.setLayoutManager(new LinearLayoutManager(getContext()));
        mDevicesAdapter = new DevicesAdapter();
        mListDevices.setAdapter(mDevicesAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.start();
    }

    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void hello() {
        Log.d(TAG, "test 1111111111111111111");
    }

    @Override
    public void showDevices(List<Device> devices) {
        Log.d("hzn", "33333333333333333");
        mDevicesAdapter.replaceData(devices);
    }
}
