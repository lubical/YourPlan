package com.lubical.android.yourplan.user;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lubical.android.yourplan.DBManager;
import com.lubical.android.yourplan.LoginFragment;
import com.lubical.android.yourplan.R;
import com.lubical.android.yourplan.user.User;

/**
 * Created by lubical on 2016/11/22.
 */

public class UserDetalChangePwFragment extends DialogFragment {
    private EditText mEditText_pw0;
    private EditText mEditText_pw1;
    private EditText mEditText_pw2;
    private DBManager mDBManager;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDBManager = new DBManager(getActivity());
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_user_detal_change_pw, null);
        mEditText_pw0 = (EditText)v.findViewById(R.id.user_changePw_pwEt0);
        mEditText_pw1 = (EditText)v.findViewById(R.id.user_changePw_pwEt1);
        mEditText_pw2 = (EditText)v.findViewById(R.id.user_changePw_pwEt2);
        mEditText_pw2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                  String pw1 = mEditText_pw1.getText().toString().trim();
                  if (pw1.length()<1)  {
                      Toast.makeText(getActivity(),"新密码未填写", Toast.LENGTH_SHORT).show();
                      return;
                  }
                 if (!pw1.contains(s)) {
                     Toast.makeText(getActivity(),"两次新密码不一致", Toast.LENGTH_SHORT).show();
                     return;
                 }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return new AlertDialog.Builder(getActivity())
                .setTitle("修改密码")
                .setView(v)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String userId = LoginFragment.loginAccount;
                        String pw = mEditText_pw0.getText().toString().trim();
                        User user = new User(userId, pw);
                        String newPw = mEditText_pw1.getText().toString().trim();
                        String newPw1 = mEditText_pw2.getText().toString().trim();
                        if (newPw.equals(newPw1)) {
                            boolean success = mDBManager.changePassword(user,newPw);
                            if (success) {
                                Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(),"密码错误，修改失败",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(),"两次密码不一致，修改失败",Toast.LENGTH_SHORT).show();
                        }
                        mDBManager.closeDB();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDBManager.closeDB();
                    }
                })
                .create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDBManager.closeDB();
    }
}
