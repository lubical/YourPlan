package com.lubical.android.yourplan.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lubical.android.yourplan.DBManager;
import com.lubical.android.yourplan.LoginFragment;
import com.lubical.android.yourplan.R;
import com.lubical.android.yourplan.account.Account;

/**
 * Created by lubical on 2016/11/21.
 */

public class UserDetalFragment extends Fragment{
    private static final String TAG = "UserDetalFragment";
    private EditText nameEt;
    private EditText ageEt;
    private EditText weightEt;
    private EditText heightEt;
    private RadioGroup sexRg;
    private RadioButton sexM;
    private RadioButton sexW;
    private Button saveBt;
    private Button changePwBt;
    private Account mAccount;
    private String userId;
    private DBManager mDBManager;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBManager = new DBManager(getActivity());
        userId = LoginFragment.loginAccount;
        mAccount = mDBManager.getAccount(userId);
        if (mAccount == null) {
            Log.d(TAG,"not account"+userId);
            mAccount = new Account(userId);
            mDBManager.addAccount(mAccount);
        }
        setHasOptionsMenu(false);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_detal, null);

        Log.d(TAG,"userid"+userId);

        nameEt = (EditText)v.findViewById(R.id.user_detal_name);
        nameEt.setText(mAccount.getName());
        ageEt = (EditText)v.findViewById(R.id.user_detal_age);
        ageEt.setText(Integer.toString(mAccount.getAge()));
        weightEt = (EditText)v.findViewById(R.id.user_detal_weight);
        weightEt.setText(Integer.toString(mAccount.getWeight()));
        heightEt = (EditText)v.findViewById(R.id.user_detal_height);
        heightEt.setText(Integer.toString(mAccount.getHeight()));
        sexRg = (RadioGroup)v.findViewById(R.id.user_detal_usersexRG);
        sexM = (RadioButton)v.findViewById(R.id.user_detal_usersexM);
        sexW = (RadioButton)v.findViewById(R.id.user_detal_usersexW);
        if (mAccount.getSex() == 1)
            sexM.setChecked(true);
        saveBt = (Button)v.findViewById(R.id.user_detal_save);
        changePwBt = (Button)v.findViewById(R.id.user_detal_changePW);
        saveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEt.getText().toString();
                String ageS = ageEt.getText().toString();
                String weightS = weightEt.getText().toString();
                String heightS = heightEt.getText().toString();
                int sex = 0;
                int checkedId = sexRg.getCheckedRadioButtonId();
                if (checkedId == R.id.user_detal_usersexW) {
                    sex = 1;
                }
                mAccount.setName(name);
                if (ageS.length()>0) {
                   mAccount.setAge(Integer.parseInt(ageS));
                }
                if (heightS.length()>0) {
                    mAccount.setHeight(Integer.parseInt(heightS));
                }
                if (weightS.length()>0) {
                    mAccount.setWeight(Integer.parseInt(weightS));
                }
                mAccount.setSex(sex);
                mDBManager.updateAccount(mAccount);
                Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_SHORT).show();
            }
        });

        changePwBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                UserDetalChangePwFragment dialog = new UserDetalChangePwFragment();
                dialog.setTargetFragment(UserDetalFragment.this,0);
                dialog.show(fm,"DialogChangePw");
            }
        });
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDBManager.closeDB();
    }
}
