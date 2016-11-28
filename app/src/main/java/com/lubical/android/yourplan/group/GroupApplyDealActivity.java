package com.lubical.android.yourplan.group;

import android.support.v4.app.Fragment;

import com.lubical.android.yourplan.SingleFragmentActivity;

/**
 * Created by lubical on 2016/11/23.
 */

public class GroupApplyDealActivity extends SingleFragmentActivity {
    public Fragment createFragment() {
        String groupId = getIntent().getStringExtra(GroupApplyDealFragment.GRUOP_ID);
        return GroupApplyDealFragment.newInstance(groupId);
    }
}
