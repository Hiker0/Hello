package com.phicomm.demo.device.plug;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phicomm.demo.R;
import com.phicomm.demo.device.plug.PlugContract.Presenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlugFragment extends Fragment implements PlugContract.View {
    private Presenter mPlugPresenter;
    @BindView(R.id.tv_plug_state)
    TextView mTvState;

    public PlugFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plug, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
    }

    public static PlugFragment newInstance() {
        return new PlugFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPlugPresenter.start();
    }

    @OnClick(R.id.bt_plug_switch)
    public void clickSwitch() {
        mPlugPresenter.switchState();
    }

    @Override
    public void setPresenter(Presenter presenter) {
        mPlugPresenter = presenter;
    }

    @Override
    public void showState(boolean isOn) {
        if (isOn) {
            mTvState.setText("开");
        } else {
            mTvState.setText("关");
        }
    }
}
