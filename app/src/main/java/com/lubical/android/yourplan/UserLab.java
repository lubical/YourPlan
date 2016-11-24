package com.lubical.android.yourplan;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by lubical on 2016/11/9.
 */

public class UserLab {
    private static final String TAG = "UserLab";
    private static final String FILENAME = "users.json";
    private static final short NOEXIST = 0;
    private static final short UNMATCH = 1;
    private static final short MATCH = 2;
    private ArrayList<User> mUsers;
    private UserIntentJSONSerializer  mUserIntentJSONSerializer;
    private static UserLab sUserLab;
    private Context mContext;

    private UserLab(Context context) {
        mContext = context;
        mUserIntentJSONSerializer = new UserIntentJSONSerializer(mContext, FILENAME);
        try {
            mUsers = mUserIntentJSONSerializer.load();
        }catch (Exception e) {
            mUsers = new ArrayList<User>();
            Log.e(TAG, "Error loading users:", e);
        }
    }

    public static UserLab get(Context context) {
        if (sUserLab == null) {
            sUserLab = new UserLab(context.getApplicationContext());
        }
        return sUserLab;
    }

    public ArrayList<User> getUsers() {
        return mUsers;
    }

    public User getUser(String ac) {
        for (User user:mUsers) {
            if (user.getUserID().equals(ac)) {
                return user;
            }
        }
        return null;
    }
    public short findUser(User user) {
        for (User user1:mUsers) {
            if (user1.getUserID().equals(user.getUserID())) {
                if (user1.getUserPassword().equals(user.getUserPassword())) {
                    return MATCH;
                } else {
                    return UNMATCH;
                }
            }
        }
        return NOEXIST;
    }
    public boolean addUser(User user) {
        if (getUser(user.getUserID()) != null) {
            return false;
        }
        mUsers.add(user);
        return true;
    }
    public boolean changePassword(User user, String newPassword) {
        for (User user1:mUsers) {
            if (user1.getUserID().equals(user.getUserID())) {
                if (user1.getUserPassword().equals(user.getUserPassword())) {
                    user1.setUserPassword(newPassword);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean saveUser() {
        try {
            mUserIntentJSONSerializer.save(mUsers);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving users: ", e);
            return false;
        }
    }

    public boolean deleteUser(User user) {
        User temp = getUser(user.getUserID());
        if (temp == null) {
            Log.d(TAG, "deleteUser failure: not exist");
            return false;
        }
        mUsers.remove(user);
        return true;
    }
}
