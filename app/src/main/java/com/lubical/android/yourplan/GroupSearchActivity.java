package com.lubical.android.yourplan;

import android.support.v4.app.Fragment;

/**
 * Created by lubical on 2016/11/22.
 */

public class GroupSearchActivity extends SingleFragmentActivity {
    @Override
    public Fragment createFragment() {
        String userId = getIntent().getStringExtra(GroupSearchFragment.USER_ID);
        return GroupSearchFragment.newInstance(userId);
    }
}
