package com.lubical.android.yourplan.group;

import android.support.v4.app.Fragment;

import com.lubical.android.yourplan.SingleFragmentActivity;

/**
 * Created by lubical on 2016/11/23.
 */

public class GroupPlanActivity extends SingleFragmentActivity {
    public Fragment createFragment() {
        String planId = getIntent().getStringExtra(GroupPlanFragment.EXTRA_GROUP_PLAN_ID);
        return GroupPlanFragment.newInstance(planId);
    }
}
