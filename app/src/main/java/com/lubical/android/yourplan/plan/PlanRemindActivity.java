package com.lubical.android.yourplan.plan;

import android.support.v4.app.Fragment;

import com.lubical.android.yourplan.SingleFragmentActivity;

import java.util.Date;

/**
 * Created by lubical on 2016/11/15.
 */

public class PlanRemindActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        long now = new Date().getTime();
        Date date =new Date(getIntent().getLongExtra(PlanRemindFragment.EXTRA_TIME, now));
        Date remind = new Date(getIntent().getLongExtra(PlanRemindFragment.EXTRA_REMIND_TIME, now+60000));
        PlanRemindFragment fragment = PlanRemindFragment.newInstance(date, remind);
        return fragment;
    }
}
