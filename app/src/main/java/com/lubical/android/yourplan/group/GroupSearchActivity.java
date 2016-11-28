package com.lubical.android.yourplan.group;

import android.support.v4.app.Fragment;

import com.lubical.android.yourplan.SingleFragmentActivity;

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
