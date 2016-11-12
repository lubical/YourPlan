package com.lubical.android.yourplan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import static android.R.attr.x;
import static android.telephony.PhoneNumberUtils.WAIT;

/**
 * Created by lubical on 2016/11/11.
 */

public class Plan {
    private static final String JSON_USER_ID = "userID";
    private static final String JSON_PLAN_ID = "planID";
    private static final String JSON_PLAN_NAME = "planName";
    private static final String JSON_PLAN_PRIVATE = "planPrivate";
    private static final String JSON_PLAN_START_TIME = "planStartTime";
    private static final String JSON_PLAN_END_TIME = "planEndTime";
    private static final String JSON_PLAN_REMIND = "planRemind";
    private static final String JSON_PLAN_REPEATFREQUENCY = "planRepeatFrequency";
    private static final String JSON_PLAN_STATUE = "planStatue";
    private static final String JSON_PLAN_IMPORT_URGENT = "planImportantUrgent";
    private String userID;
    private String planID;
    private String planName;
    private boolean planPrivate;
    private Date planStartTime;
    private Date planEndTime;
    private boolean planRemind;
    private int planRepeatFrequency;
    private int planStatue;
    private int planImportantUrgent;

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_USER_ID, userID);
        json.put(JSON_PLAN_ID, planID);
        json.put(JSON_PLAN_NAME, planName);
        json.put(JSON_PLAN_PRIVATE,planPrivate);
        json.put(JSON_PLAN_START_TIME,planStartTime.getTime());
        json.put(JSON_PLAN_END_TIME, planEndTime.getTime());
        json.put(JSON_PLAN_REMIND, planRemind);
        json.put(JSON_PLAN_REPEATFREQUENCY, planRepeatFrequency);
        json.put(JSON_PLAN_STATUE, planStatue);
        json.put(JSON_PLAN_IMPORT_URGENT, planImportantUrgent);
        return json;
    }
    public Plan(){};

    public int getPlanImportantUrgent() {
        return planImportantUrgent;
    }

    public void setPlanImportantUrgent(int planImportantUrgent) {
        this.planImportantUrgent = planImportantUrgent;
    }

    public Plan(JSONObject json) throws JSONException {
        userID = json.getString(JSON_USER_ID);
        planID = json.getString(JSON_PLAN_ID);
        planName = json.getString(JSON_PLAN_NAME);
        planPrivate = json.getBoolean(JSON_PLAN_PRIVATE);
        planStartTime = new Date(json.getLong(JSON_PLAN_START_TIME));
        planEndTime = new Date(json.getLong(JSON_PLAN_END_TIME));
        planRemind = json.getBoolean(JSON_PLAN_REMIND);
        planRepeatFrequency = json.getInt(JSON_PLAN_REPEATFREQUENCY);
        planStatue = json.getInt(JSON_PLAN_STATUE);
        planImportantUrgent = json.getInt(JSON_PLAN_IMPORT_URGENT);
    }
    public String getPlanID() {
        return planID;
    }

    public void setPlanID(String planID) {
        this.planID = planID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public boolean isPlanPrivate() {
        return planPrivate;
    }

    public void setPlanPrivate(boolean planPrivate) {
        this.planPrivate = planPrivate;
    }

    public Date getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(Date planStartTime) {
        this.planStartTime = planStartTime;
    }

    public Date getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(Date planEndTime) {
        this.planEndTime = planEndTime;
    }

    public boolean isPlanRemind() {
        return planRemind;
    }

    public void setPlanRemind(boolean planRemind) {
        this.planRemind = planRemind;
    }

    public int getPlanRepeatFrequency() {
        return planRepeatFrequency;
    }

    public void setPlanRepeatFrequency(int planRepeatFrequency) {
        this.planRepeatFrequency = planRepeatFrequency;
    }

    public int getPlanStatue() {
        return planStatue;
    }

    public void setPlanStatue(int planStatue) {
        this.planStatue = planStatue;
    }

    public String toString() {
        return planName;
    }
}
