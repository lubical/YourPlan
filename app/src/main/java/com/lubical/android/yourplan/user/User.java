package com.lubical.android.yourplan.user;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lubical on 2016/11/7.
 */

public class User {
    public static final String JSON_USER_ID = "userId";
    public static final String JSON_USER_PASSWORD = "userPassword";
    public static final String JSON_USER_GRADE = "userGrade";

    private String userPassword;

    private int userGrade;  //改成int
    private String userId;
    private static final String TAG = "User";

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getUserGrade() {
        return userGrade;
    }

    public void setUserGrade(int userGrade) {
        this.userGrade = userGrade;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_USER_ID, userId);
        json.put(JSON_USER_PASSWORD, userPassword);
        json.put(JSON_USER_GRADE, userGrade);
        return json;
    }
    public User(){}

    public User(String userId, String userPassword) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userGrade = 0;
    }
    public User(JSONObject json) throws JSONException {
        userId = json.getString(JSON_USER_ID);
        userPassword = json.getString(JSON_USER_PASSWORD);
        userGrade = json.getInt(JSON_USER_GRADE);

    }

    @Override
    public String toString() {
        return userId;
    }

}
