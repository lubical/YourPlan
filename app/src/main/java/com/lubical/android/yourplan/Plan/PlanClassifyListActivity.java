package com.lubical.android.yourplan.plan;

import android.support.v4.app.Fragment;

import com.lubical.android.yourplan.SingleFragmentActivity;

/**
 * Created by lubical on 2016/11/18.
 */

public class PlanClassifyListActivity extends SingleFragmentActivity {
    public Fragment createFragment() {
        return new PlanClassifyListFragment();
    }
}
