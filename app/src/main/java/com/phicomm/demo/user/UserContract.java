package com.phicomm.demo.user;

import com.phicomm.demo.BasePresenter;
import com.phicomm.demo.BaseView;

/**
 * Created by hzn on 17-5-26.
 */

public class UserContract {
    interface View extends BaseView<Presenter> {

        void showLoading();

        void showLoginSuccess();

        void showLoginFail();
    }

    interface Presenter extends BasePresenter {
        void login(String phoneNumber, String password);
    }
}
