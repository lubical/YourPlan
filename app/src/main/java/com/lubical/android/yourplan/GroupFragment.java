package com.lubical.android.yourplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by lubical on 2016/11/21.
 */

public class GroupFragment extends Fragment {
    private static final String TAG = "groupfragment";
    private Button createGroupBt;
    private Button joinGroupBt;
    private Account mAccount;
    private String userId;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        userId = AccountLab.get(getActivity()).getmUserId();
        mAccount = AccountLab.get(getActivity()).getAccount(userId);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group, null);
        createGroupBt = (Button)v.findViewById(R.id.fragment_group_createBt);
        createGroupBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked();
                Log.d(TAG, "create a group"+userId);
                Group group = new Group(userId);
                Plan plan = new Plan(userId);
                group.setGroupPlanId(plan.getPlanID());
                Account account = AccountLab.get(getActivity()).getAccount(userId);
                account.setGroupId(group.getGroupId());
                AccountLab.get(getActivity()).saveAccount();
                GroupLab.get(getActivity()).addGroup(group);
                PlanLab.get(getActivity()).addPlan(plan);
                GroupLab.get(getActivity()).saveGroup();
                PlanLab.get(getActivity()).savePlan();
                Intent i = new Intent(getActivity(),GroupOwnerActivity.class);
                i.putExtra(GroupOwnerFragment.EXTRA_GROUP_ID,group.getGroupId());
                startActivity(i);
            }
        });
        joinGroupBt = (Button)v.findViewById(R.id.fragment_group_joinBt);
        joinGroupBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked();
                Log.d(TAG, "search group to join in"+userId);
                Intent i = new Intent(getActivity(),GroupSearchActivity.class);
                i.putExtra(GroupSearchFragment.USER_ID, userId);
                startActivity(i);
            }
        });
        return v;
    }
    private void checked() {
        if (!mAccount.getGroupId().equals(mAccount.getUserId())) {
            Group group = GroupLab.get(getActivity()).getGroup(mAccount.getGroupId());
            if (group.getGroupOwnerId().equals(mAccount.getUserId())) {
                Log.d(TAG, "own a group"+userId);
                Intent i = new Intent(getActivity(),GroupOwnerActivity.class);
                startActivity(i);
            } else {
                Log.d(TAG, "joined a group"+userId);
                Intent i = new Intent(getActivity(),GroupMemberActivity.class);
                startActivity(i);
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }
}
