package com.lubical.android.yourplan.user;

import android.support.v4.app.Fragment;

import com.lubical.android.yourplan.SingleFragmentActivity;
import com.lubical.android.yourplan.user.RegisterFragment;

/**
 * Created by lubical on 2016/11/10.
 */

public class RegisterActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new RegisterFragment();
    }
}
