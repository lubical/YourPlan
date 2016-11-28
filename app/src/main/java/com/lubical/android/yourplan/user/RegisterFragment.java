package com.lubical.android.yourplan.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lubical.android.yourplan.DBManager;
import com.lubical.android.yourplan.R;
import com.lubical.android.yourplan.user.User;

/**
 * Created by lubical on 2016/11/9.
 */

public class RegisterFragment extends Fragment{
    private EditText mEditText_account;
    private EditText mEditText_password;
    private EditText mEditText_confirmpassword;
    private Button mButton_register;
    private DBManager mDBManager;
    public static final String EXTRA_ACCOUNT = "yourplan.register_fragment.account";
    public static final String EXTRA_PASSWORD = "yourplan.register_fragment.password";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBManager = new DBManager(getActivity());
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, viewGroup, false);

        mEditText_account = (EditText)v.findViewById(R.id.register_account);
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
                User user1 = mDBManager.getUser(account);
                if (user1 != null) {
                    Toast.makeText(getActivity(),"账户创建失败,已存在",Toast.LENGTH_SHORT).show();
                    return;
                }
                mDBManager.addUser(user);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }
    public void onDestroy() {
        super.onDestroy();
        mDBManager.closeDB();
    }
}
