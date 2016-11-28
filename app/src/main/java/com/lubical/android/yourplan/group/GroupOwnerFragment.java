package com.lubical.android.yourplan.group;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lubical.android.yourplan.DBManager;
import com.lubical.android.yourplan.DateTimePickerFragment;
import com.lubical.android.yourplan.R;
import com.lubical.android.yourplan.account.Account;
import com.lubical.android.yourplan.plan.Plan;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by lubical on 2016/11/22.
 */

public class GroupOwnerFragment extends ListFragment {
    public static final String EXTRA_GROUP_ID = "groupowner.groupId";
    private static final String TAG = "GroupOwner";
    private Plan mPlan;
    private Group mGroup;
    private Account mAccount;
    private UUID groupId;
    private Button planEditBtn;
    private Button tickBtn;
    private Button applyDealBtn;
    private TextView planName;
    private TextView planTimes;
    private TextView planDuring;
    private TextView mywork;
    private TextView groupName;
    private DBManager mDBManager;
    private List<HashMap<String, Object>> groupMemberMapList;
    private SimpleAdapter mSimpleAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBManager = new DBManager(getActivity());

        groupId = UUID.fromString(getArguments().getString(EXTRA_GROUP_ID));
        mGroup = mDBManager.getGroup(groupId);
        mAccount = mDBManager.getAccount(mGroup.getGroupOwnerId());
        mPlan = mDBManager.getPlan(mGroup.getGroupPlanId());
        groupMemberMapList = mDBManager.getAccountMapList(groupId);
        mSimpleAdapter = new SimpleAdapter(
                getActivity(),
                groupMemberMapList,
                R.layout.list_item_group_member,
                new String[] {"name","groupTaskState","groupTaskReward"},
                new int[] {R.id.list_item_group_member_id, R.id.list_item_group_member_number,
                           R.id.list_item_group_member_rewardNumber}
        );
        setListAdapter(mSimpleAdapter);
        if (mPlan == null) {
            mPlan = new Plan(groupId.toString());
            mGroup.setGroupPlanId(mPlan.getPlanID());
            mDBManager.updateGroup(mGroup);
            mDBManager.addPlan(mPlan);
        }
        Log.d(TAG, "groupId"+mGroup.getGroupId());
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
                intent.putExtra(GroupPlanFragment.EXTRA_GROUP_PLAN_ID,mPlan.getPlanID().toString());
                startActivity(intent);
            }
        });
        applyDealBtn = (Button)v.findViewById(R.id.group_owner_applyDeal);
        applyDealBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),GroupApplyDealActivity.class);
                intent.putExtra(GroupApplyDealFragment.GRUOP_ID, mGroup.getGroupId().toString());
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
    public void onListItemClick(ListView l, View v, int position, long id) {


    }
    @Override
    public void onResume() {
        super.onResume();
        mDBManager.updatePlan(mPlan);
        mDBManager.updateAccount(mAccount);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDBManager.closeDB();
    }
}
