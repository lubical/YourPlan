package com.lubical.android.yourplan;

import android.support.v4.app.Fragment;

/**
 * Created by lubical on 2016/11/12.
 */

public class PlanActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() { return new PlanFragment(); }

}
