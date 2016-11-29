package com.lubical.android.yourplan.group;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lubical.android.yourplan.DBManager;
import com.lubical.android.yourplan.LoginFragment;
import com.lubical.android.yourplan.R;
import com.lubical.android.yourplan.account.Account;
import com.lubical.android.yourplan.plan.Plan;

/**
 * Created by lubical on 2016/11/21.
 */

public class GroupFragment extends Fragment {
    private static final String TAG = "groupfragment";
    private Button createGroupBt;
    private Button joinGroupBt;
    private Account mAccount;
    private String userId;
    private DBManager mDBManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBManager = new DBManager(getActivity());
        userId = LoginFragment.loginAccount;
        mAccount = mDBManager.getAccount(userId);
        if (mAccount == null) {
            Log.d(TAG,"not account"+userId);
            mAccount = new Account(userId);
            mDBManager.addAccount(mAccount);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group, null);
        createGroupBt = (Button)v.findViewById(R.id.fragment_group_createBt);
        createGroupBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!haveGroup()) {
                    Log.d(TAG, "create a group" + userId);
                    Group group = new Group(userId);
                    Plan plan = new Plan(userId);
                    plan.setUserID(group.getGroupId().toString());
                    group.setGroupPlanId(plan.getPlanID());
                    Account account = mDBManager.getAccount(userId);
                    account.setGroupId(group.getGroupId());
                    mDBManager.addGroup(group);
                    mDBManager.addPlan(plan);
                    mDBManager.updateAccount(account);

                    Intent i = new Intent(getActivity(), GroupOwnerActivity.class);
                    i.putExtra(GroupOwnerFragment.EXTRA_GROUP_ID, group.getGroupId().toString());
                    startActivity(i);
                }
            }
        });
        joinGroupBt = (Button)v.findViewById(R.id.fragment_group_joinBt);
        joinGroupBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!haveGroup()) {
                    Log.d(TAG, "search group to join in" + userId);
                    Intent i = new Intent(getActivity(), GroupSearchActivity.class);
                    i.putExtra(GroupSearchFragment.USER_ID, userId);
                    startActivity(i);
                }
            }
        });
        return v;
    }
    private boolean haveGroup() {
        if (! mDBManager.isExistGroup(mAccount.getGroupId())) {
            return false;
        }
        if (!mAccount.getGroupId().equals(mAccount.getUserId())) {
            Group group = mDBManager.getGroup(mAccount.getGroupId());
            if (group.getGroupOwnerId().equals(mAccount.getUserId())) {
                Log.d(TAG, "own a group"+userId);
                Intent i = new Intent(getActivity(),GroupOwnerActivity.class);
                i.putExtra(GroupOwnerFragment.EXTRA_GROUP_ID,group.getGroupId().toString());
                startActivity(i);
            } else {
                Log.d(TAG, "joined a group"+userId);
                Intent i = new Intent(getActivity(),GroupMemberActivity.class);
                startActivity(i);
            }
        }
        return true;
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDBManager.closeDB();
    }
}
