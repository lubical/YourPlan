package com.lubical.android.yourplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by lubical on 2016/11/9.
 */

public class RegisterFragment extends Fragment{
    private EditText mEditText_account;
    private EditText mEditText_password;
    private EditText mEditText_confirmpassword;
    private Button mButton_register;
    public static final String EXTRA_ACCOUNT = "yourplan.register_fragment.account";
    public static final String EXTRA_PASSWORD = "yourplan.register_fragment.password";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, viewGroup, false);

        mEditText_account = (EditText)v.findViewById(R.id.register_account);
        mEditText_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                User user = UserLab.get(getActivity()).getUser(s.toString());
                if (user != null) {
                    Toast.makeText(getActivity(), "账户已存在", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mEditText_password = (EditText) v.findViewById(R.id.register_passwordEditext);
        mEditText_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        mEditText_confirmpassword = (EditText)v.findViewById(R.id.register_passwordConfirmEditText);
        mEditText_confirmpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pw = mEditText_password.getText().toString();
                if (!pw.contains(s.toString())) {
                    Toast.makeText(getActivity(),"两次输入密码不一致", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEditText_confirmpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        mButton_register = (Button)v.findViewById(R.id.register_confirmButton);
        mButton_register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String account = mEditText_account.getText().toString().trim();
                String password = mEditText_password.getText().toString().trim();
                String confirmpassword = mEditText_confirmpassword.getText().toString().trim();
                if (account.length() == 0 || password.length() == 0 || confirmpassword.length() == 0) {
                    Toast.makeText(getActivity(), "信息不完整，请补充", Toast.LENGTH_SHORT).show();
                    return;
                }
                User user = new User(account, password);
                UserLab.get(getActivity()).addUser(user);
                Toast.makeText(getActivity(),"账户创建成功", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.putExtra(EXTRA_ACCOUNT, account);
                intent.putExtra(EXTRA_PASSWORD, password);
                getActivity().setResult(0, intent);
                getActivity().finish();

            }
        });
        return v;
    }

}
