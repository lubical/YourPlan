package com.lubical.android.yourplan;

import android.support.v4.app.Fragment;

/**
 * Created by lubical on 2016/11/22.
 */

public class GroupMemberActivity extends SingleFragmentActivity {
    public Fragment createFragment() {
        return new GroupMemberFragment();
    }
}
