package com.lubical.android.yourplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by lubical on 2016/11/22.
 */

public class GroupOwnerFragment extends ListFragment {
    public static final String EXTRA_GROUP_ID = "groupowner.groupId";
    private Plan mPlan;
    private Group mGroup;
    private Account mAccount;
    private String groupId;
    private Button planEditBtn;
    private Button tickBtn;
    private Button applyDealBtn;
    private TextView planName;
    private TextView planTimes;
    private TextView planDuring;
    private TextView mywork;
    private TextView groupName;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        groupId = getArguments().getString(EXTRA_GROUP_ID);
        mGroup = GroupLab.get(getActivity()).getGroup(groupId);
        mAccount = AccountLab.get(getActivity()).getAccount(mGroup.getGroupOwnerId());
        mPlan = PlanLab.get(getActivity()).getPlan(groupId);
        if (mPlan == null) {
            mPlan = new Plan(groupId);
            PlanLab.get(getActivity()).addPlan(mPlan);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group_owner, null);
        planName = (TextView)v.findViewById(R.id.group_owner_planName);
        planName.setText(mPlan.getPlanName());
        groupName = (TextView)v.findViewById(R.id.group_owner_groupName);
        groupName.setText(mGroup.getGroupName());
        planTimes = (TextView)v.findViewById(R.id.group_owner_planTimes);
        planTimes.setText(Integer.toString(mPlan.getPlanStatue())+"/"+Integer.toString(mPlan.getPlanRepeatFrequency()));
        planDuring = (TextView)v.findViewById(R.id.group_owner_planDuring);
        String startDate = DateTimePickerFragment.format.format(mPlan.getPlanStartTime());
        String endDate = DateTimePickerFragment.format.format(mPlan.getPlanEndTime());
        planDuring.setText(startDate+">"+ endDate);
        mywork = (TextView)v.findViewById(R.id.group_owner_myWork);
        mywork.setText(Integer.toString(mAccount.getGroupTaskState()));
        tickBtn = (Button)v.findViewById(R.id.group_owner_tick);
        tickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAccount.setGroupTaskState(mAccount.getGroupTaskState()+1);
                mPlan.setPlanStatue(mPlan.getPlanStatue()+1);
                Toast.makeText(getActivity(),"今日完成",Toast.LENGTH_SHORT);
                planTimes.setText(Integer.toString(mPlan.getPlanStatue())+"/"+Integer.toString(mPlan.getPlanRepeatFrequency()));
            }
        });
        planEditBtn =(Button)v.findViewById(R.id.group_owner_planEdit);
        planEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GroupPlanActivity.class);
                intent.putExtra(GroupPlanFragment.EXTRA_GROUP_PLAN_ID,mPlan.getPlanID());
                startActivity(intent);
            }
        });
        applyDealBtn = (Button)v.findViewById(R.id.group_owner_applyDeal);
        applyDealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),GroupApplyDealActivity.class);
                intent.putExtra(GroupApplyDealFragment.GRUOP_ID, mGroup.getGroupId());
                startActivity(intent);
            }
        });
        return v;
    }

    public static GroupOwnerFragment newInstance(String groupId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_GROUP_ID, groupId);
        GroupOwnerFragment fragment = new GroupOwnerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        GroupLab.get(getActivity()).saveGroup();
        PlanLab.get(getActivity()).savePlan();
        AccountLab.get(getActivity()).saveAccount();
    }
}
