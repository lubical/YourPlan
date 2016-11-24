package com.lubical.android.yourplan;

import android.support.v4.app.Fragment;

/**
 * Created by lubical on 2016/11/23.
 */

public class GroupPlanActivity extends SingleFragmentActivity {
    public Fragment createFragment() {
        String planId = getIntent().getStringExtra(GroupPlanFragment.EXTRA_GROUP_PLAN_ID);
        return GroupPlanFragment.newInstance(planId);
    }
}
