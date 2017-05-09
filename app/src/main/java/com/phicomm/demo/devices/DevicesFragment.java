package com.phicomm.demo.devices;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phicomm.demo.R;
import com.phicomm.demo.devices.DevicesContract.Presenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class DevicesFragment extends Fragment implements DevicesContract.View {
    private final static String TAG = DevicesFragment.class.getSimpleName();
    private DevicesContract.Presenter mPresenter;

    public DevicesFragment() {
        // Required empty public constructor
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


    }

    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void hello() {
        Log.d(TAG, "test 1111111111111111111");
    }
}
