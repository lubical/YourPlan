package com.lubical.android.yourplan.plan;

import android.support.v4.app.Fragment;

import com.lubical.android.yourplan.SingleFragmentActivity;

/**
 * Created by lubical on 2016/11/12.
 */

public class PlanActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() { return new PlanFragment(); }

}
