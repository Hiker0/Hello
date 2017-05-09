package com.phicomm.demo.devices;

import com.phicomm.demo.BasePresenter;
import com.phicomm.demo.BaseView;

/**
 * Created by hzn on 17-5-9.
 */

public interface DevicesContract {
    interface View extends BaseView<Presenter> {

        void hello();
    }

    interface Presenter extends BasePresenter {

    }
}
