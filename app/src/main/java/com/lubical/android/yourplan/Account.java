package com.lubical.android.yourplan;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lubical on 2016/11/22.
 */

public class Account {
    private static final String ACCOUNT_USER_ID = "accountUserID";
    private static final String ACCOUNT_GROUP_ID = "accountGroupId";
    private static final String ACCOUNT_AGE = "accountAge";
    private static final String ACCOUNT_SEX = "accountSex";
    private static final String ACCOUNT_WEIGHT = "accountWeight";
    private static final String ACCOUNT_HEIGTH = "accountHeight";
    private static final String ACCOUNT_NAME = "accountName";
    private static final String ACCOUNT_GROUP_TASK_STATE = "groupTaskState";
    private String name;
    private String userId;
    private int age;
    private int sex;
    private int weight;
    private int height;
    private String groupId;
    private int groupTaskState;
    public Account(String userId) {
        this.userId = userId;
        name = "初来乍到";
        groupId = userId;
        age = 1;
        sex = 1;
        weight = 50;
        height = 160;
        groupTaskState = 0;
    }
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(ACCOUNT_USER_ID, userId);
        json.put(ACCOUNT_AGE, age);
        json.put(ACCOUNT_GROUP_ID, groupId);
        json.put(ACCOUNT_HEIGTH, height);
        json.put(ACCOUNT_WEIGHT, weight);
        json.put(ACCOUNT_SEX, sex);
        json.put(ACCOUNT_NAME, name);
        json.put(ACCOUNT_GROUP_TASK_STATE,groupTaskState);
        return json;
    }
    public Account(JSONObject json) throws JSONException {
        userId = json.getString(ACCOUNT_USER_ID);
        groupId = json.getString(ACCOUNT_USER_ID);
        name = json.getString(ACCOUNT_NAME);
        age = json.getInt(ACCOUNT_AGE);
        sex = json.getInt(ACCOUNT_SEX);
        height = json.getInt(ACCOUNT_HEIGTH);
        weight = json.getInt(ACCOUNT_WEIGHT);

    }
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public  String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGroupTaskState() {
        return groupTaskState;
    }

    public void setGroupTaskState(int groupTaskState) {
        this.groupTaskState = groupTaskState;
    }
}
