package com.lubical.android.yourplan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import static android.R.attr.x;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
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
    private static final String JSON_PLAN_CLASSIFY = "planCalssify";
    private String userID;
    private String planID;
    private String planName;
    private boolean planPrivate;
    private Date planStartTime;
    private Date planRemindTime;
    private Date planEndTime;
    private int planRepeatFrequency;      //任务周期数
    private int planStatue;               //任务状态
    private int planImportantUrgent;
    private String planClassify;             //任务项目，分类
    public static final int IMPORTANT_URGENT = 0;
    public static final int UNIMPORTANT_URGENT = 1;
    public static final int IMPORTANT_NOTURGENT = 2;
    public static final int UNIMPORTANT_NOTURGENT = 3;

    public String getPlanClassify() {
        return planClassify;
    }

    public void setPlanClassify(String planClassify) {
        this.planClassify = planClassify;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_USER_ID, userID);
        json.put(JSON_PLAN_ID, planID);
        json.put(JSON_PLAN_NAME, planName);
        json.put(JSON_PLAN_PRIVATE,planPrivate);
        json.put(JSON_PLAN_START_TIME,planStartTime.getTime());
        json.put(JSON_PLAN_END_TIME, planEndTime.getTime());
        json.put(JSON_PLAN_REMIND, planRemindTime.getTime());
        json.put(JSON_PLAN_REPEATFREQUENCY, planRepeatFrequency);
        json.put(JSON_PLAN_STATUE, planStatue);
        json.put(JSON_PLAN_IMPORT_URGENT, planImportantUrgent);
        json.put(JSON_PLAN_CLASSIFY, planClassify);
        return json;
    }
    public Plan(String userID){
        this.userID = userID;
        long a = new Date().getTime();
        planID = Long.toString(a);
        planStatue = 0;
        planRepeatFrequency = 1;
        planStartTime = new Date();
        planEndTime = new Date();
        planPrivate = true;
        planRemindTime = new Date(planStartTime.getTime()+60000);
        planImportantUrgent = IMPORTANT_URGENT;
        planClassify = "未分组";
        planName = new String("New");
    }
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
        planClassify = json.getString(JSON_PLAN_CLASSIFY);
        planStartTime = new Date(json.getLong(JSON_PLAN_START_TIME));
        planEndTime = new Date(json.getLong(JSON_PLAN_END_TIME));
        planRemindTime = new Date(json.getLong(JSON_PLAN_REMIND));
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

    public int getPlanRepeatFrequency() {
        return planRepeatFrequency;
    }

    public Date getPlanRemindTime() {
        return planRemindTime;
    }

    public void setPlanRemindTime(Date planRemind) {
        this.planRemindTime = planRemind;
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
