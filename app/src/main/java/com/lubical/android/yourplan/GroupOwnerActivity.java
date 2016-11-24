package com.lubical.android.yourplan;

import android.support.v4.app.Fragment;

/**
 * Created by lubical on 2016/11/22.
 */

public class GroupOwnerActivity extends SingleFragmentActivity {
    public Fragment createFragment() {
        String groupId = getIntent().getStringExtra(GroupOwnerFragment.EXTRA_GROUP_ID);
        return GroupOwnerFragment.newInstance(groupId);
    }
}
