package com.lubical.android.yourplan;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static android.R.attr.fragment;
import static android.R.attr.start;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.lubical.android.yourplan.R.layout.fragment_datetimepicker;

/**
 * Created by lubical on 2016/11/14.
 * timepicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY))
 */

public class DateTimePickerFragment extends DialogFragment {
    private static final String TAG = "DateTimePickerFragment";
    public static final String EXTRA_START_DATE_TIME = "datetimepickerfragment.start_date_time";
    public static final String EXTRA_END_DATE_TIME = "datetimepickerfragment.end_date_time";
    private Calendar startTime = Calendar.getInstance(Locale.CHINA);
    private Calendar endTime = Calendar.getInstance(Locale.CHINA);
    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    private Button mButton_start;
    private Button mButton_end;
    private LinearLayout startLinearLayout;
    private LinearLayout endLinearLayout;
    private DatePicker datePicker_start;
    private TimePicker timePicker_start;
    private DatePicker datePicker_end;
    private TimePicker timePicker_end;

    @Override @TargetApi(23)
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(fragment_datetimepicker, null);
        startLinearLayout = (LinearLayout)v.findViewById(R.id.fragment_datetimepicker_startLinearLayout);
        endLinearLayout = (LinearLayout)v.findViewById(R.id.fragment_datetimepicker_endLinearLayout);
        Date start = (Date)getArguments().getSerializable(EXTRA_START_DATE_TIME);
        startTime.setTime(start);
        Date end = (Date)getArguments().getSerializable(EXTRA_END_DATE_TIME);
        endTime.setTime(end);
        mButton_start = (Button)v.findViewById(R.id.fragment_datetimepicker_startButton);
        mButton_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLinearLayout.setVisibility(View.VISIBLE);
                endLinearLayout.setVisibility(View.GONE);
            }
        });

        mButton_end = (Button)v.findViewById(R.id.fragment_datetimepicker_endButton);
        mButton_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLinearLayout.setVisibility(View.GONE);
                endLinearLayout.setVisibility(View.VISIBLE);
            }
        });

        datePicker_start = (DatePicker)v.findViewById(R.id.fragment_datetimepicker_startDatePicker);
        DatePicker.OnDateChangedListener dateChangedListener = new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                startTime.set(Calendar.YEAR, year);
                startTime.set(Calendar.MONTH, monthOfYear);
                startTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mButton_start.setText(format.format(startTime.getTime()));
            }
        };
        mButton_start.setText(format.format(startTime.getTime()));
        resizePicker(datePicker_start);
        timePicker_start = (TimePicker) v.findViewById(R.id.fragment_datetimepicker_startTimePicker);
        timePicker_start.setIs24HourView(true);
        resizePicker(timePicker_start);
        TimePicker.OnTimeChangedListener timeChangedListener = new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                startTime.set(Calendar.HOUR_OF_DAY,hourOfDay);
                startTime.set(Calendar.MINUTE, minute);
                mButton_start.setText(format.format(startTime.getTime()));
            }
        };
        timePicker_start.setOnTimeChangedListener(timeChangedListener);

        datePicker_start.init(startTime.get(Calendar.YEAR), startTime.get(Calendar.MONTH), startTime.get(Calendar.DAY_OF_MONTH), dateChangedListener);

        timePicker_end = (TimePicker)v.findViewById(R.id.fragment_datetimepicker_endTimePicker);
        TimePicker.OnTimeChangedListener timeChangedListener1 = new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                endTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                endTime.set(Calendar.MINUTE, minute);
                mButton_end.setText(format.format(endTime.getTime()));
            }
        };
        timePicker_end.setIs24HourView(true);
        mButton_end.setText(format.format(endTime.getTime()));
        timePicker_end.setOnTimeChangedListener(timeChangedListener1);
        resizePicker(timePicker_end);
        int start_hour = startTime.get(Calendar.HOUR_OF_DAY);
        int start_minute = startTime.get(Calendar.MINUTE);
        int end_hour = endTime.get(Calendar.HOUR_OF_DAY);
        int end_minute = endTime.get(Calendar.MINUTE);
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            timePicker_start.setMinute(start_minute);
            timePicker_start.setHour(start_hour);
            timePicker_end.setMinute(end_minute);
            timePicker_end.setHour(end_hour);
        } else {
            timePicker_start.setCurrentMinute(start_minute);
            timePicker_start.setCurrentHour(start_hour);
            timePicker_end.setCurrentMinute(end_minute);
            timePicker_end.setCurrentHour(end_hour);
        }
        datePicker_end = (DatePicker)v.findViewById(R.id.fragment_datetimepicker_endDatePicker);
        DatePicker.OnDateChangedListener dateChangedListener1 = new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                endTime.set(Calendar.YEAR, year);
                endTime.set(Calendar.MONTH, monthOfYear);
                endTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mButton_end.setText(format.format(endTime.getTime()));
            }
        };
        datePicker_end.init(endTime.get(Calendar.YEAR), endTime.get(Calendar.MONTH), endTime.get(Calendar.DAY_OF_MONTH), dateChangedListener1);
        resizePicker(datePicker_end);
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
        Date date_start = new GregorianCalendar(startTime.get(Calendar.YEAR), startTime.get(Calendar.MONTH), startTime.get(Calendar.DAY_OF_MONTH),
                                                startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE)).getTime();

        Date date_end= new GregorianCalendar(endTime.get(Calendar.YEAR), endTime.get(Calendar.MONTH), endTime.get(Calendar.DAY_OF_MONTH),
                endTime.get(Calendar.HOUR_OF_DAY), endTime.get(Calendar.MINUTE)).getTime();
        Intent intent = new Intent();
        intent.putExtra(EXTRA_START_DATE_TIME, date_start);
        intent.putExtra(EXTRA_END_DATE_TIME, date_end);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    public static DateTimePickerFragment newInstance(Date startDate, Date endDate) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_START_DATE_TIME, startDate);
        args.putSerializable(EXTRA_END_DATE_TIME, endDate);
        DateTimePickerFragment fragment = new DateTimePickerFragment();
        fragment.setArguments(args);
        return fragment;
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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
