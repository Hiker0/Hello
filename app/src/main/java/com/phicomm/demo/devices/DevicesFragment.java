package com.phicomm.demo.devices;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phicomm.demo.R;
import com.phicomm.demo.data.Device;
import com.phicomm.demo.device.switcher.SwitcherActivity;
import com.phicomm.demo.devices.DevicesContract.Presenter;
import com.phicomm.demo.util.ItemClickSupport;
import com.phicomm.demo.util.ItemClickSupport.OnItemClickListener;

import java.io.Serializable;
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

        mListDevices.setLayoutManager(new LinearLayoutManager(getContext()));
        mListDevices.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mDevicesAdapter = new DevicesAdapter();
        mListDevices.setAdapter(mDevicesAdapter);

        ItemClickSupport.addTo(mListDevices).setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Device device = (Device) v.getTag();

                mPresenter.openDeviceDetails(device);
                Log.d(TAG, "position: " + position + " device: " + device.getBssid());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showDevices(@NonNull List<Device> devices) {
        checkNotNull(devices);
        mDevicesAdapter.replaceData(devices);
    }

    @Override
    public void showDeviceDetailsUI(@NonNull Device device) {
        checkNotNull(device);
        switch (device.getType()) {
            case PLUG:
            case SWITCHER: {
                Intent intent = new Intent(getContext(), SwitcherActivity.class);
                intent.putExtra("device", (Serializable) device);
                startActivity(intent);
            }
            break;
            default:
                throw new RuntimeException("can't found device type: " + device.getType());
        }
    }
}
