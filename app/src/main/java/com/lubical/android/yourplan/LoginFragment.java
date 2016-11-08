package com.lubical.android.yourplan;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by lubical on 2016/11/7.
 */

public class LoginFragment extends Fragment{
    private String account;
    private String key;
    private static final String TAG ="LoginFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, viewGroup, false);
        Button mlogin = (Button)v.findViewById(R.id.login_button);
        final EditText acEditText = (EditText)v.findViewById(R.id.login_accountEditText);
        final EditText pwEdiText = (EditText)v.findViewById(R.id.login_passwordEditText);

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = acEditText.getText().toString();
                key = pwEdiText.getText().toString();
                //Toast.makeText(getActivity(),"login"+account+key,Toast.LENGTH_SHORT).show();
                Log.d(TAG,"onClick"+account+key);
            }
        });

        TextView register = (TextView)v.findViewById(R.id.login_registTextView);
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "注册", Toast.LENGTH_SHORT).show();
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
}
