package com.lubical.android.yourplan;

import android.support.v4.app.Fragment;

/**
 * Created by lubical on 2016/11/23.
 */

public class GroupApplyDealActivity extends SingleFragmentActivity {
    public Fragment createFragment() {
        String groupId = getIntent().getStringExtra(GroupApplyDealFragment.GRUOP_ID);
        return GroupApplyDealFragment.newInstance(groupId);
    }
}
