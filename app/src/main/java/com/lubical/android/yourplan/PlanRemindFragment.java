package com.lubical.android.yourplan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Date;
import java.util.GregorianCalendar;

import static android.R.attr.data;

/**
 * Created by lubical on 2016/11/15.
 */

public class PlanRemindFragment extends Fragment {
    public static final String EXTRA_TIME = "planremindfragment.time";
    public static final String EXTRA_REMIND_TIME = "planremindfragment.remindtime";
    private static final String TAG = "PlanRemindFragment";
    private static final String DIALOG = "planremind.time";
    private static final int REQUEST = 1;
    private Button mButton_onTime;
    private Button mButton_5;
    private Button mButton_10;
    private Button mButton_15;
    private Button mButton_halfHour;
    private Button mButton_oneHour;
    private Button mButton_no;
    private Button mButton_define;
    private Date mDate;
    private Date mDate_remind;
    private long remindgap;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDate = (Date)getArguments().getSerializable(EXTRA_TIME);
        mDate_remind = (Date)getArguments().getSerializable(EXTRA_REMIND_TIME);
        remindgap = mDate.getTime() - mDate_remind.getTime();
        remindgap/=60000;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_plan_remind, null);
        mButton_onTime = (Button)v.findViewById(R.id.fragment_plan_remind_onTimeButton);
        mButton_onTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDate_remind = mDate;
                mButton_onTime.setBackgroundColor(ContextCompat.getColor(getActivity(),android.R.color.holo_blue_light));
                sendResult(Activity.RESULT_OK);
            }
        });
        mButton_5 = (Button)v.findViewById(R.id.fragment_plan_remind_5Button);
        mButton_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDate_remind = new Date(mDate.getTime()-5*60*1000);
                mButton_5.setBackgroundColor(ContextCompat.getColor(getActivity(),android.R.color.holo_blue_light));
                sendResult(Activity.RESULT_OK);
            }
        });
        mButton_10 = (Button)v.findViewById(R.id.fragment_plan_remind_10Button);
        mButton_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDate_remind = new Date(mDate.getTime()-10*60*1000);
                mButton_10.setBackgroundColor(ContextCompat.getColor(getActivity(),android.R.color.holo_blue_light));
                sendResult(Activity.RESULT_OK);
            }
        });
        mButton_15 = (Button)v.findViewById(R.id.fragment_plan_remind_15Button);
        mButton_15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDate_remind = new Date(mDate.getTime()-15*60*1000);
                mButton_15.setBackgroundColor(ContextCompat.getColor(getActivity(),android.R.color.holo_blue_light));
                sendResult(Activity.RESULT_OK);
            }
        });
        mButton_halfHour = (Button)v.findViewById(R.id.fragment_plan_remind_halfHourButton);
        mButton_halfHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDate_remind = new Date(mDate.getTime()-30*60*1000);
                mButton_halfHour.setBackgroundColor(ContextCompat.getColor(getActivity(),android.R.color.holo_blue_light));
                sendResult(Activity.RESULT_OK);
            }
        });
        mButton_oneHour = (Button)v.findViewById(R.id.fragment_plan_remind_oneHourButton);
        mButton_oneHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDate_remind = new Date(mDate.getTime()-60*60*1000);
                mButton_oneHour.setBackgroundColor(ContextCompat.getColor(getActivity(),android.R.color.holo_blue_light));
                sendResult(Activity.RESULT_OK);
            }
        });
        mButton_no = (Button)v.findViewById(R.id.fragment_plan_remind_noButton);
        mButton_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDate_remind = new Date(mDate.getTime()+60000);
                mButton_no.setBackgroundColor(ContextCompat.getColor(getActivity(),android.R.color.holo_blue_light));
                sendResult(Activity.RESULT_OK);
            }
        });
        mButton_define = (Button)v.findViewById(R.id.fragment_plan_remind_defineButton);
        mButton_define.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                PlanRemindDateTimeFragment dialog = PlanRemindDateTimeFragment.newInstance(mDate);
                dialog.setTargetFragment(PlanRemindFragment.this, REQUEST);
                dialog.show(fm, DIALOG);
            }
        });

        switch ((int)remindgap) {
            case -1:
                mButton_no.setBackgroundColor(ContextCompat.getColor(getActivity(),android.R.color.holo_blue_light));
                break;
            case 0:
                mButton_onTime.setBackgroundColor(ContextCompat.getColor(getActivity(),android.R.color.holo_blue_light));
                break;
            case 5:
                mButton_5.setBackgroundColor(ContextCompat.getColor(getActivity(),android.R.color.holo_blue_light));
                break;
            case 10:
                mButton_10.setBackgroundColor(ContextCompat.getColor(getActivity(),android.R.color.holo_blue_light));
                break;
            case 15:
                mButton_15.setBackgroundColor(ContextCompat.getColor(getActivity(),android.R.color.holo_blue_light));
                break;
            case 30:
                mButton_halfHour.setBackgroundColor(ContextCompat.getColor(getActivity(),android.R.color.holo_blue_light));
                break;
            case 60:
                mButton_oneHour.setBackgroundColor(ContextCompat.getColor(getActivity(),android.R.color.holo_blue_light));
                break;
            default:
                mButton_define.setBackgroundColor(ContextCompat.getColor(getActivity(),android.R.color.holo_blue_light));
                break;
        }

        return v;
    }

    /**
     * 自定义时间获取
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST) {
            mDate_remind = (Date)data.getSerializableExtra(PlanRemindDateTimeFragment.EXTRA_REMIND_TIME);
            sendResult(resultCode);
        }
    }

    private void sendResult(int resultCode) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_REMIND_TIME, mDate_remind);
        getActivity().setResult(resultCode,intent);
        getActivity().finish();
    }
    public static PlanRemindFragment newInstance(Date date, Date remindDate) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TIME, date);
        args.putSerializable(EXTRA_REMIND_TIME, remindDate);
        PlanRemindFragment fragment = new PlanRemindFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
