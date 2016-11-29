package com.lubical.android.yourplan.group;

import android.support.v4.app.Fragment;

import com.lubical.android.yourplan.SingleFragmentActivity;

/**
 * Created by lubical on 2016/11/22.
 */

public class GroupMemberActivity extends SingleFragmentActivity {
    public Fragment createFragment() {
        String userId = getIntent().getStringExtra(GroupMemberFragment.EXTRA_USER_ID);
        return GroupMemberFragment.newInstance(userId);
    }
}
