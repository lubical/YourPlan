package com.lubical.android.yourplan;

import android.support.v4.app.Fragment;

/**
 * Created by lubical on 2016/11/10.
 */

public class PlanListActivity extends SingleFragmentActivity{
    @Override
    protected Fragment createFragment() {
        return new PlanListFragment();
    }
}
