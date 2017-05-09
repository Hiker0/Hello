package com.phicomm.demo.devices;

import com.phicomm.demo.BasePresenter;
import com.phicomm.demo.BaseView;
import com.phicomm.demo.data.Device;

import java.util.List;

/**
 * Created by hzn on 17-5-9.
 */

public interface DevicesContract {
    interface View extends BaseView<Presenter> {

        void hello();

        void showDevices(List<Device> devices);
    }

    interface Presenter extends BasePresenter {
        void loadDevices();
    }
}
