package com.lubical.android.yourplan.plan;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.lubical.android.yourplan.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static android.media.CamcorderProfile.get;

/**
 * Created by lubical on 2016/11/15.
 */

public class PlanRemindDateTimeFragment extends DialogFragment{

    public static final String EXTRA_REMIND_TIME = "planreminddatetimefragment.remindTime";
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private Calendar remindCalendar = Calendar.getInstance(Locale.CHINA);

    public static PlanRemindDateTimeFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_REMIND_TIME, date);
        PlanRemindDateTimeFragment fragment = new PlanRemindDateTimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override @TargetApi(23)
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_plan_remind_datetime, null);

        Date mDate = (Date)getArguments().getSerializable(EXTRA_REMIND_TIME);
        remindCalendar.setTime(mDate);
        mDatePicker = (DatePicker)v.findViewById(R.id.fragment_plan_remind_datetimeDatePicker);
        mTimePicker = (TimePicker)v.findViewById(R.id.fragment_plan_remind_datetimeTimePicker);
        DatePicker.OnDateChangedListener dateChangedListener = new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                remindCalendar.set(Calendar.YEAR, year);
                remindCalendar.set(Calendar.MONTH, monthOfYear);
                remindCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }
        };

        mTimePicker.setIs24HourView(true);
        TimePicker.OnTimeChangedListener timeChangedListener = new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                remindCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                remindCalendar.set(Calendar.MINUTE, minute);
            }
        };
        mTimePicker.setOnTimeChangedListener(timeChangedListener);
        mDatePicker.init(remindCalendar.get(Calendar.YEAR), remindCalendar.get(Calendar.MONTH), remindCalendar.get(Calendar.DAY_OF_MONTH), dateChangedListener);

        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            mTimePicker.setMinute(remindCalendar.get(Calendar.MINUTE));
            mTimePicker.setHour(remindCalendar.get(Calendar.HOUR_OF_DAY));
        } else {
            mTimePicker.setCurrentHour(remindCalendar.get(Calendar.MINUTE));
            mTimePicker.setCurrentMinute(remindCalendar.get(Calendar.HOUR_OF_DAY));
        }
        resizePicker(mDatePicker);
        resizePicker(mTimePicker);
        return new AlertDialog.Builder(getActivity())
                .setTitle("设置计划日期时间")
                .setView(v)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
    }

    private void sendResult(int resultCode) {

        Date date= new GregorianCalendar(remindCalendar.get(Calendar.YEAR), remindCalendar.get(Calendar.MONTH), remindCalendar.get(Calendar.DAY_OF_MONTH),
                remindCalendar.get(Calendar.HOUR_OF_DAY), remindCalendar.get(Calendar.MINUTE)).getTime();

        Intent intent = new Intent();
        intent.putExtra(EXTRA_REMIND_TIME, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        List<NumberPicker>npList = new ArrayList<NumberPicker>();
        View child = null;
        if (null != viewGroup) {
            for (int i=0;i<viewGroup.getChildCount();i++) {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker) {
                    npList.add((NumberPicker)child);
                }else if(child instanceof LinearLayout){
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0)
                        return result;
                }
            }
        }
        return npList;
    }

    private void resizeNumberPicker(NumberPicker np) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,0,10,0);
        np.setLayoutParams(params);
    }
    private void resizePicker(FrameLayout tp) {
        List<NumberPicker>npList = findNumberPicker(tp);
        for (NumberPicker np:npList) {
            resizeNumberPicker(np);
        }
    }
}
