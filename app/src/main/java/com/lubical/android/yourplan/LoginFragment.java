package com.lubical.android.yourplan;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by lubical on 2016/11/7.
 */

public class LoginFragment extends Fragment{
    private String account;
    private String key;
    private User mUser;
    private static final String TAG ="LoginFragment";
    private static final short NOEXIST = 0;
    private static final short UNMATCH = 1;
    private static final short MATCH = 2;
    public static final String EXTRA_USER_ID = "userID";
    private static final int REQUEST_ACCOUNT = 0;

    private EditText acEditText;
    private EditText pwEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, viewGroup, false);
        Button mlogin = (Button)v.findViewById(R.id.login_button);
        acEditText = (EditText)v.findViewById(R.id.login_accountEditText);
        pwEditText = (EditText)v.findViewById(R.id.login_passwordEditText);
        pwEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = acEditText.getText().toString();
                key = pwEditText.getText().toString();
                User user = new User(account, key);
                short state = UserLab.get(getActivity()).findUser(user);
                switch (state) {
                    case NOEXIST:
                        Toast.makeText(getActivity(), "账户不存在",Toast.LENGTH_SHORT).show();
                        break;
                    case UNMATCH:
                        Toast.makeText(getActivity(), "密码错误", Toast.LENGTH_SHORT).show();
                        break;
                    case MATCH:
                        mUser = UserLab.get(getActivity()).getUser(account);
                        Toast.makeText(getActivity(), "登陆成功", Toast.LENGTH_SHORT).show();
                        UserLab.get(getActivity()).saveUser();
                        PlanLab.get(getActivity(), account);
                        Log.d(TAG,"onClick"+account+key);
                        Intent intent = new Intent(getActivity(), PlanListPagerActivity.class);
                        Bundle args = new Bundle();
                        args.putSerializable(EXTRA_USER_ID, account);
                        intent.putExtras(args);
                        startActivity(intent);
                        break;
                    default:
                        Log.e(TAG, "Login failure");
                }
                Log.d(TAG,"onClick"+account+key);
            }
        });

        TextView register = (TextView)v.findViewById(R.id.login_registTextView);
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // Toast.makeText(getActivity(), "注册", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(),RegisterActivity.class);
                startActivityForResult(i, REQUEST_ACCOUNT);

            }
        });
        TextView forgetPassword = (TextView)v.findViewById(R.id.login_forgotPassword);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "找回密码", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (resultCode == REQUEST_ACCOUNT) {
            String account = data.getStringExtra(RegisterFragment.EXTRA_ACCOUNT);
            String password = data.getStringExtra(RegisterFragment.EXTRA_PASSWORD);
            acEditText.setText(account);
            pwEditText.setText(password);
            Toast.makeText(getActivity(), account + password, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        UserLab.get(getActivity()).saveUser();
    }
}
