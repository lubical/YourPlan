package com.lubical.android.yourplan.plan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lubical.android.yourplan.DBManager;
import com.lubical.android.yourplan.DateTimePickerFragment;
import com.lubical.android.yourplan.R;
import com.lubical.android.yourplan.account.Account;
import com.lubical.android.yourplan.share.Share;

import java.util.Date;
import java.util.UUID;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static java.lang.Integer.parseInt;

/**
 * Created by lubical on 2016/11/11.
 */

public class PlanFragment extends Fragment {
    public static final String EXTRA_PLAN_ID ="yourplan.planfragment.plan_id";
    private static final String TAG = "planFragment";
    private static final int REQUEST_DATE_TIME = 1;
    private static final String DIALOG_DATE_TIME = "date_time";
    private static final int REQUEST_REMIND_TIME = 2;
    private static final int REQUEST_PLANCLASS = 3;
    private EditText planNameET;
    private EditText planFrequency;
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
    private UUID planId;
    private Plan mPlan;
    private DBManager mDBManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (NavUtils.getParentActivityName(getActivity()) != null ) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mDBManager = new DBManager(getActivity());
        planId = UUID.fromString(getActivity().getIntent().getStringExtra(EXTRA_PLAN_ID));
        Log.d(TAG, "planId"+planId);
        mPlan = mDBManager.getPlan(planId);
        Log.d(TAG,"imorturgent"+mPlan.getPlanImportantUrgent());
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
        planNameET = (EditText)v.findViewById(R.id.fragment_plan_nameET);
        planNameET.setText(mPlan.getPlanName());
        planNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPlan.setPlanName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rg = (RadioGroup)v.findViewById(R.id.fragment_plan_rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.fragment_plan_rbIU:
                        mPlan.setPlanImportantUrgent(Plan.IMPORTANT_URGENT);
                        planNameET.setBackgroundColor(ContextCompat.getColor(getActivity(),color[0]));
                        break;
                    case R.id.fragment_plan_rbNIU:
                        mPlan.setPlanImportantUrgent(Plan.UNIMPORTANT_URGENT);
                        planNameET.setBackgroundColor(ContextCompat.getColor(getActivity(), color[1]));
                        break;
                    case R.id.fragment_plan_rbINU:
                        mPlan.setPlanImportantUrgent(Plan.IMPORTANT_NOTURGENT);
                        planNameET.setBackgroundColor(ContextCompat.getColor(getActivity(), color[2]));
                        break;
                    case R.id.fragment_plan_rbNINU:
                        mPlan.setPlanImportantUrgent(Plan.UNIMPORTANT_NOTURGENT);
                        planNameET.setBackgroundColor(ContextCompat.getColor(getActivity(), color[3]));
                }
            }
        });
        inuRb = (RadioButton)v.findViewById(R.id.fragment_plan_rbINU);
        iuRb = (RadioButton)v.findViewById(R.id.fragment_plan_rbIU);
        niuRb = (RadioButton)v.findViewById(R.id.fragment_plan_rbNIU);
        ninuRb = (RadioButton)v.findViewById(R.id.fragment_plan_rbNINU);

        switch (mPlan.getPlanImportantUrgent()) {
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
        planTimeBt = (Button)v.findViewById(R.id.fragment_plan_dateTimeBt);
        String startDate = DateTimePickerFragment.format.format(mPlan.getPlanStartTime());
        String endDate = DateTimePickerFragment.format.format(mPlan.getPlanEndTime());
        planTimeBt.setText(startDate+">"+ endDate);
        planTimeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Log.d(TAG, "planStartDate"+mPlan.getPlanStartTime().getTime());
                Log.d(TAG, "planEndDate"+mPlan.getPlanEndTime().getTime());
                DateTimePickerFragment dialog = DateTimePickerFragment.newInstance(mPlan.getPlanStartTime(), mPlan.getPlanEndTime());
                dialog.setTargetFragment(PlanFragment.this, REQUEST_DATE_TIME);
                dialog.show(fm, DIALOG_DATE_TIME);
            }
        });
        planRemindBt = (Button)v.findViewById(R.id.fragment_plan_remindBt);
        setRemindText();
        planRemindBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlanRemindActivity.class);
                intent.putExtra(PlanRemindFragment.EXTRA_TIME, mPlan.getPlanStartTime().getTime());
                intent.putExtra(PlanRemindFragment.EXTRA_REMIND_TIME, mPlan.getPlanRemindTime().getTime());
                startActivityForResult(intent, REQUEST_REMIND_TIME);
            }
        });
        classifyBt = (Button)v.findViewById(R.id.frgment_plan_classifyBt);
        classifyBt.setText(mPlan.getPlanClassify());
        classifyBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlanClassifyListActivity.class);
                startActivityForResult(intent, REQUEST_PLANCLASS);
            }
        });
        finishTodayBt = (Button)v.findViewById(R.id.fragment_plan_finishToday);
        finishTodayBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int progress = planProgressBar.getProgress();
                if (progress < planProgressBar.getMax()) {
                    planProgressBar.setProgress(progress + 1);
                    mPlan.setPlanStatue(progress + 1);
                }
            }
        });
        planProgressBar = (ProgressBar)v.findViewById(R.id.fragment_plan_progressBar);
        planProgressBar.setMax(mPlan.getPlanRepeatFrequency());
        planProgressBar.setProgress(mPlan.getPlanStatue());
        planFrequency = (EditText)v.findViewById(R.id.fragment_plan_frequency);
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
                planProgressBar.setMax(frequency);
                mPlan.setPlanRepeatFrequency(frequency);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void setRemindText() {
        long timegap = mPlan.getPlanStartTime().getTime()- mPlan.getPlanRemindTime().getTime();
        timegap/=60000;
        switch ((int)timegap) {
            case -1:
                planRemindBt.setText("不提醒");
                break;
            case 0:
                planRemindBt.setText("准时");
                break;
            case 5:
                planRemindBt.setText("提前5分钟");
                break;
            case 10:
                planRemindBt.setText("提前10分钟");
                break;
            case 15:
                planRemindBt.setText("提前15分钟");
                break;
            case 30:
                planRemindBt.setText("提前30分钟");
                break;
            case 60:
                planRemindBt.setText("提前1小时");
                break;
            default:
                planRemindBt.setText(DateTimePickerFragment.format.format(mPlan.getPlanRemindTime()));
                break;
        }

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
            planTimeBt.setText(startDate +">" +endDate);
        }
        if (requestCode == REQUEST_REMIND_TIME) {
            Date remind = (Date)data.getSerializableExtra(PlanRemindFragment.EXTRA_REMIND_TIME);
            mPlan.setPlanRemindTime(remind);
            setRemindText();
        }
        if (requestCode == REQUEST_PLANCLASS) {
            String planclass = data.getStringExtra(PlanClassifyListFragment.EXTRA_PLANCLASSIFY);
            classifyBt.setText(planclass);
            mPlan.setPlanClassify(planclass);
            classifyBt.setText(mPlan.getPlanClassify());
        }

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_plan_share, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    Log.d(TAG, mPlan.getPlanName()+mPlan.getUserID()+mPlan.getPlanImportantUrgent());
                    mDBManager.updatePlan(mPlan);
                    NavUtils.navigateUpFromSameTask(getActivity());
                    return true;
                }
                break;
            case R.id.menu_item_plan_share:
                final EditText editText = new EditText(getActivity());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("分享");
                builder.setMessage("请输入分享感言(可选)");
                builder.setView(editText);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String message = editText.getText().toString();
                        Account account = mDBManager.getAccount(mPlan.getUserID());
                        Share share = new Share(mPlan.getPlanID(),account.getGroupId(), mPlan.getUserID(), message);
                        Log.d(TAG, " userId="+account.getUserId());
                        Log.d(TAG, " groupId="+account.getGroupId());
                        boolean shared = mDBManager.isExistShare(mPlan.getPlanID());
                        if (!shared) {
                           mDBManager.addShare(share);
                            Toast.makeText(getActivity(),"分享成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(),"已分享", Toast.LENGTH_SHORT).show();
                        }
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        mDBManager.updatePlan(mPlan);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDBManager.closeDB();
    }
}
