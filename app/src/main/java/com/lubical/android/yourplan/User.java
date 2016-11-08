package com.lubical.android.yourplan;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lubical on 2016/11/7.
 */

public class User {
    private static final String JSON_USER_ID = "userID";
    private static final String JSON_USER_PASSWORD = "userPassword";
    private static final String JSON_USER_GRADE = "userGrade";

    private String userPassword;
    private enum grade{ADMIN, GROUPOWNER, REGULARUSER};
    private grade userGrade;
    private String userID;
    private static final String TAG = "User";

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public grade getUserGrade() {
        return userGrade;
    }

    public void setUserGrade(grade userGrade) {
        this.userGrade = userGrade;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_USER_ID, userID);
        json.put(JSON_USER_PASSWORD, userPassword);
        json.put(JSON_USER_GRADE, userGrade.name());
        return json;
    }

    public User(JSONObject json) throws JSONException {
        userID = json.getString(JSON_USER_ID);
        userPassword = json.getString(JSON_USER_PASSWORD);
        String usergrade = json.getString(JSON_USER_GRADE);
        switch (usergrade) {
            case "ADMIN":
                userGrade = grade.ADMIN;
                break;
            case "GROUPOWNER":
                userGrade = grade.GROUPOWNER;
                break;
            case "REGULARUSER":
                userGrade = grade.REGULARUSER;
                break;
            default:
                Log.d(TAG, "userGrade was wrong!");
                userGrade = grade.REGULARUSER;
        }

    }

    @Override
    public String toString() {
        return userID;
    }

}
