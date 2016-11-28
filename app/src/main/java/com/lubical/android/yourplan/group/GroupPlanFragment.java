package com.lubical.android.yourplan.group;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.lubical.android.yourplan.DBManager;
import com.lubical.android.yourplan.DateTimePickerFragment;
import com.lubical.android.yourplan.R;
import com.lubical.android.yourplan.plan.Plan;

import java.util.Date;
import java.util.UUID;

/**
 * Created by lubical on 2016/11/23.
 */

public class GroupPlanFragment  extends Fragment{
    public static final String EXTRA_GROUP_PLAN_ID="groupPlanId";
    private static final String TAG = "GroupPlanFragment";
    private static final String DIALOG_DATE_TIME = "date_timeGP";
    private static final int REQUEST_DATE_TIME = 1;
    private UUID planId;
    private Plan mPlan;
    private Group mGroup;
    private DBManager mDBManager;
    private EditText planName;
    private Button dateTime;
    private EditText planFrequency;
    private TextView planReward;
    private SeekBar planRewardSeekBar;
    private ProgressBar planStatue;
    private Button finishBtn;
    private Button statueClearBtn;
    private UUID groupId;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        planId = UUID.fromString(getArguments().getString(EXTRA_GROUP_PLAN_ID));
        mDBManager = new DBManager(getActivity());
        mPlan = mDBManager.getPlan(planId);
        try {
            groupId = UUID.fromString(mPlan.getUserID());
            mGroup = mDBManager.getGroup(groupId);

        } catch (Exception e) {
            Log.e(TAG, "get groupId failure");
            getActivity().finish();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group_plan, null);

        planName = (EditText)v.findViewById(R.id.fragment_group_plan_nameET);
        planName.setText(mPlan.getPlanName());
        dateTime = (Button) v.findViewById(R.id.fragment_group_plan_dateTimeBt);
        String startDate = DateTimePickerFragment.format.format(mPlan.getPlanStartTime());
        String endDate = DateTimePickerFragment.format.format(mPlan.getPlanEndTime());
        dateTime.setText(startDate+">"+ endDate);
        dateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Log.d(TAG, "planStartDate"+mPlan.getPlanStartTime().getTime());
                Log.d(TAG, "planEndDate"+mPlan.getPlanEndTime().getTime());
                DateTimePickerFragment dialog = DateTimePickerFragment.newInstance(mPlan.getPlanStartTime(), mPlan.getPlanEndTime());
                dialog.setTargetFragment(GroupPlanFragment.this, REQUEST_DATE_TIME);
                dialog.show(fm, DIALOG_DATE_TIME);
            }
        });
        planFrequency = (EditText)v.findViewById(R.id.fragment_group_plan_frequency);
        planFrequency.setText(Integer.toString(mPlan.getPlanRepeatFrequency()));
        planFrequency.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int frequency;
                if (s.length() > 0) {
                    frequency = Integer.parseInt(s.toString());
                }
                else {
                    frequency = 1;
                }
                planStatue.setMax(frequency);
                mPlan.setPlanRepeatFrequency(frequency);
                planRewardSeekBar.setMax(mPlan.getPlanRepeatFrequency()/2);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        planReward = (TextView)v.findViewById(R.id.fragment_group_plan_reward);
        planReward.setText(Integer.toString(mGroup.getGroupPlanRewardTimes()));
        planRewardSeekBar = (SeekBar)v.findViewById(R.id.fragment_group_plan_rewardSeekbar);
        planRewardSeekBar.setMax(mPlan.getPlanRepeatFrequency()/2);
        planRewardSeekBar.setProgress(mGroup.getGroupPlanRewardTimes());
        planRewardSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mGroup.setGroupPlanRewardTimes(progress);
                planReward.setText(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        planStatue = (ProgressBar)v.findViewById(R.id.fragment_group_plan_progressBar);
        planStatue.setMax(mPlan.getPlanRepeatFrequency());
        planStatue.setProgress(mPlan.getPlanStatue());
        finishBtn = (Button)v.findViewById(R.id.fragment_group_plan_finishBtn);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDBManager.updateGroup(mGroup);
                mDBManager.updatePlan(mPlan);
                getActivity().finish();
            }
        });
        statueClearBtn = (Button)v.findViewById(R.id.fragment_group_plan_statueClearBtn);
        statueClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("提示");
                builder.setMessage("确认重置进度，组内成员进度将重置！");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDBManager.accountGroupStateReset(groupId);
                        mPlan.setPlanStatue(0);
                        planStatue.setProgress(0);
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
        });

        return v;
    }

    public static GroupPlanFragment newInstance(String planId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_GROUP_PLAN_ID,planId);
        GroupPlanFragment fragment = new GroupPlanFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_DATE_TIME) {
            Date start = (Date)data.getSerializableExtra(DateTimePickerFragment.EXTRA_START_DATE_TIME);
            Date end = (Date)data.getSerializableExtra(DateTimePickerFragment.EXTRA_END_DATE_TIME);
            mPlan.setPlanStartTime(start);
            mPlan.setPlanEndTime(end);
            String startDate = DateTimePickerFragment.format.format(mPlan.getPlanStartTime());
            String endDate = DateTimePickerFragment.format.format(mPlan.getPlanEndTime());
            dateTime.setText(startDate +">" +endDate);
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        mPlan.setPlanName(planName.getText().toString());
        mDBManager.updatePlan(mPlan);
        mDBManager.updateGroup(mGroup);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mDBManager.closeDB();
    }
}
