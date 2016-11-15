package com.lubical.android.yourplan;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by lubical on 2016/11/11.
 */

public class PlanFragment extends Fragment {
    public static final String EXTRA_PLAN_ID ="yourplan.planfragment.plan_id";
    private static final String TAG = "planFragment";
    private static final int REQUEST_DATE_TIME = 1;
    private static final String DIALOG_DATE_TIME = "date_time";
    private EditText planNameET;
    private Button planTimeBt;
    private Button planRemindBt;
    private RadioGroup rg;
    private RadioButton iuRb;
    private RadioButton niuRb;
    private RadioButton inuRb;
    private RadioButton ninuRb;
    private Button finishTodayBt;
    private Button classifyBt;
    private ProgressBar planProgressBar;
    private String planId;
    private Plan mplan;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        planId = getActivity().getIntent().getStringExtra(EXTRA_PLAN_ID);
        mplan = PlanLab.get(getActivity()).getPlan(planId);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_plan, null);

        initView(v);

        return v;
    }
    private void initView(View v) {
        final int color[]={android.R.color.holo_red_light,android.R.color.holo_orange_light
                ,android.R.color.holo_blue_light,android.R.color.holo_green_light};
        rg = (RadioGroup)v.findViewById(R.id.fragment_plan_rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.fragment_plan_rbIU:
                        planNameET.setBackgroundColor(ContextCompat.getColor(getActivity(),color[0]));
                        break;
                    case R.id.fragment_plan_rbNIU:
                        planNameET.setBackgroundColor(ContextCompat.getColor(getActivity(), color[1]));
                        break;
                    case R.id.fragment_plan_rbINU:
                        planNameET.setBackgroundColor(ContextCompat.getColor(getActivity(), color[2]));
                        break;
                    case R.id.fragment_plan_rbNINU:
                        planNameET.setBackgroundColor(ContextCompat.getColor(getActivity(), color[3]));
                }
            }
        });
        planNameET = (EditText)v.findViewById(R.id.fragment_plan_nameET);
        planNameET.setText(mplan.getPlanName());
        planRemindBt = (Button)v.findViewById(R.id.fragment_plan_remindBt);
        switch (mplan.getPlanImportantUrgent()) {
            case Plan.IMPORTANT_URGENT:
                planNameET.setBackgroundColor(ContextCompat.getColor(getActivity(),color[0]));
                iuRb.setChecked(true);
                break;
            case Plan.UNIMPORTANT_URGENT:
                planNameET.setBackgroundColor(ContextCompat.getColor(getActivity(),color[1]));
                niuRb.setChecked(true);
                break;
            case Plan.IMPORTANT_NOTURGENT:
                planNameET.setBackgroundColor(ContextCompat.getColor(getActivity(), color[2]));
                inuRb.setChecked(true);
                break;
            case Plan.UNIMPORTANT_NOTURGENT:
                planNameET.setBackgroundColor(ContextCompat.getColor(getActivity(), color[3]));
                ninuRb.setChecked(true);
                break;
        }
        planTimeBt.setText(mplan.getPlanStartTime()+">"+mplan.getPlanEndTime());
        planTimeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DateTimePickerFragment dialog = DateTimePickerFragment.newInstance(mplan.getPlanStartTime(), mplan.getPlanEndTime());
                dialog.setTargetFragment(PlanFragment.this, REQUEST_DATE_TIME);
                dialog.show(fm, DIALOG_DATE_TIME);
            }
        });
        planRemindBt.setText(mplan.getPlanRemind());
        classifyBt = (Button)v.findViewById(R.id.frgment_plan_classifyBt);
        classifyBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //待处理，  先显示已有分类，可选择，也可自定义
            }
        });
        finishTodayBt = (Button)v.findViewById(R.id.fragment_plan_finishToday);
        finishTodayBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int progress = planProgressBar.getProgress();
                if (progress < planProgressBar.getMax()) {
                    planProgressBar.setProgress(progress + 1);
                }
            }
        });
        planProgressBar = (ProgressBar)v.findViewById(R.id.fragment_plan_progressBar);
        planProgressBar.setMax(mplan.getPlanRepeatFrequency());
        planProgressBar.setProgress(mplan.getPlanStatue());

    }
}
