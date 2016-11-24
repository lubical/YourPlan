package com.lubical.android.yourplan;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by lubical on 2016/11/22.
 */

public class AccountLab {
    private static final String TAG = "AccountLab";
    private static final String FILENAME = "account.json";
    private String mUserId;
    private ArrayList<Account> mAccounts;
    private static AccountLab sAccountLab;
    private Context mContext;
    private AccountIntentJSONSerializer mSerializer;
    private void reload(Context context) {
        mContext = context;
        mSerializer = new AccountIntentJSONSerializer(mContext, FILENAME);
        try {
            mAccounts = mSerializer.load();
        }catch (Exception e) {
            mAccounts = new ArrayList<Account>();
            Log.e(TAG, "Error loading accounts:", e);
        }
    }
    private AccountLab(Context context) {
        reload(context);
    }
    public static AccountLab get(Context context) {
        if (sAccountLab == null) {
            sAccountLab = new AccountLab(context.getApplicationContext());
        }
        return sAccountLab;
    }


    public ArrayList<Account> getAccounts() {
        return mAccounts;
    }

    public Account getAccount(String ac) {
        for (Account user:mAccounts) {
            if (user.getUserId().equals(ac)) {
                return user;
            }
        }
        return null;
    }

    public boolean addAccount(Account account) {
        if (getAccount(account.getUserId()) != null) {
            return false;
        }
        mAccounts.add(account);
        return true;
    }

    public boolean saveAccount() {
        try {
            mSerializer.save(mAccounts);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving users: ", e);
            return false;
        }
    }

    public boolean deleteAccount(Account user) {
        Account temp = getAccount(user.getUserId());
        if (temp == null) {
            Log.d(TAG, "deleteAccount failure: not exist");
            return false;
        }
        mAccounts.remove(user);
        return true;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }
}
