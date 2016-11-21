package com.lubical.android.yourplan;

import android.support.v4.app.Fragment;

/**
 * Created by lubical on 2016/11/18.
 */

public class PlanClassifyListActivity extends SingleFragmentActivity {
    public Fragment createFragment() {
        return new PlanClassifyListFragment();
    }
}
