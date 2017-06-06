package com.phicomm.demo.user;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.phicomm.demo.R;
import com.phicomm.demo.user.UserContract.Presenter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginFragment extends Fragment implements UserContract.View {
    @BindView(R.id.et_login_phonenumber)
    EditText mEditPhoneNumber;
    @BindView(R.id.et_login_password)
    EditText mEditPassword;
    @BindView(R.id.bt_login_login)
    Button mButtonLogin;

    private Presenter mPresenter;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        mButtonLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = mEditPhoneNumber.getText().toString();
                String password = mEditPassword.getText().toString();

                mPresenter.login(phoneNumber, password);
            }
        });
    }

    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading() {
        mButtonLogin.setText("in login");
    }

    @Override
    public void showLoginSuccess() {
        Toast.makeText(getActivity(), "Login success", Toast.LENGTH_SHORT).show();
        mButtonLogin.setText("LOGIN");
        getActivity().finish();
    }

    @Override
    public void showLoginFail() {
        Toast.makeText(getActivity(), "Login fail", Toast.LENGTH_SHORT).show();
    }
}
