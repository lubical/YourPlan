package com.lubical.android.yourplan.group;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lubical.android.yourplan.DBManager;
import com.lubical.android.yourplan.DateTimePickerFragment;
import com.lubical.android.yourplan.R;
import com.lubical.android.yourplan.account.Account;
import com.lubical.android.yourplan.plan.Plan;
import com.lubical.android.yourplan.share.Share;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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
        setHasOptionsMenu(true);
        if (NavUtils.getParentActivityName(getActivity()) != null ) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mDBManager = new DBManager(getActivity());
        groupId = UUID.fromString(getArguments().getString(EXTRA_GROUP_ID));
        Log.d(TAG, " gourpId="+groupId);
        mGroup = mDBManager.getGroup(groupId);
        mAccount = mDBManager.getAccount(mGroup.getGroupOwnerId());
        mPlan = mDBManager.getPlan(mGroup.getGroupPlanId());
        groupMemberMapList = mDBManager.getAccountMapList(groupId);
        for (HashMap<String,Object> map:groupMemberMapList) {
            Log.d(TAG,"groupUser "+map.get(Account.ACCOUNT_NAME));
        }
        Log.d(TAG, "groupMember"+groupMemberMapList.size());
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
                mywork.setText(Integer.toString(mAccount.getGroupTaskState()));
                Toast.makeText(getActivity(),"今日完成",Toast.LENGTH_SHORT);
                planTimes.setText(Integer.toString(mPlan.getPlanStatue())+"/"+Integer.toString(mPlan.getPlanRepeatFrequency()));
                mSimpleAdapter.notifyDataSetChanged();
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
        ListView listView = (ListView)v.findViewById(android.R.id.list);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            registerForContextMenu(listView);
        } else {
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater1 = mode.getMenuInflater();
                    inflater1.inflate(R.menu.plan_list_item_context, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_item_delete_groupMember:
                            for (int i=groupMemberMapList.size()-1; i>=0; i--) {
                                if (getListView().isItemChecked(i)) {
                                    HashMap<String, Object> map = groupMemberMapList.get(i);
                                    String userId = map.get(Account.ACCOUNT_USER_ID).toString();
                                    Account account = mDBManager.getAccount(userId);
                                    account.setGroupId(UUID.randomUUID());
                                    account.setGroupTaskState(0);
                                    account.setGroupTaskReward(0);
                                    mDBManager.updateAccount(account);
                                    groupMemberMapList.remove(i);
                                }
                            }
                            mode.finish();
                            mSimpleAdapter.notifyDataSetChanged();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }
        return v;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.groupmember_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = info.position;

        HashMap<String, Object> map = groupMemberMapList.get(position);
        switch (item.getItemId()) {
            case R.id.menu_item_delete_groupMember:
                String userId = map.get(Account.ACCOUNT_USER_ID).toString();
                Account account = mDBManager.getAccount(userId);
                account.setGroupId(UUID.randomUUID());
                account.setGroupTaskState(0);
                account.setGroupTaskReward(0);
                mDBManager.updateAccount(account);
                mSimpleAdapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
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
        HashMap<String, Object> map = groupMemberMapList.get(position);
        final String userId = map.get(Account.ACCOUNT_USER_ID).toString();
        if (mGroup.getGroupPlanRewardTimes() == 0) {
            Toast.makeText(getActivity(),"奖励次数已用完",Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("确认奖励该成员");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Account mAccount = mDBManager.getAccount(userId);
                mAccount.setGroupTaskReward(mAccount.getGroupTaskReward()+1);
                mAccount.setGroupTaskState(mAccount.getGroupTaskState()+1);
                mGroup.setGroupPlanRewardTimes(mGroup.getGroupPlanRewardTimes()-1);
                mDBManager.updateAccount(mAccount);
                mDBManager.updateGroup(mGroup);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    mDBManager.updatePlan(mPlan);
                    mDBManager.updateAccount(mAccount);
                    NavUtils.navigateUpFromSameTask(getActivity());
                    return true;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
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
