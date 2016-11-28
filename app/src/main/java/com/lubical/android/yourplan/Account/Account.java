package com.lubical.android.yourplan.account;

import java.util.UUID;

/**
 * Created by lubical on 2016/11/22.
 */

public class Account {
    /*
    * db.execSQL("CREATE TABLE IF NOT EXISTS Account (userId VARCHAR PRIMARY KEY," +
                   "name VARCHAR, age INTEGER, sex INTEGER, weight INTEGER, height INTEGER" +
                   "groupId VARCHAR, groupTaskState INTEGER)");
     */
    public static final String ACCOUNT_USER_ID = "userId";
    public static final String ACCOUNT_GROUP_ID = "groupId";
    public static final String ACCOUNT_AGE = "age";
    public static final String ACCOUNT_SEX = "sex";
    public static final String ACCOUNT_WEIGHT = "weight";
    public static final String ACCOUNT_HEIGTH = "height";
    public static final String ACCOUNT_NAME = "name";
    public static final String ACCOUNT_GROUP_TASK_STATE = "groupTaskState";
    public static final String ACCOUNT_GROUP_TASK_REWARD = "groupTaskReward";
    private String name;
    private String userId;
    private int age;
    private int sex;
    private int weight;
    private int height;
    private UUID groupId;
    private int groupTaskState;
    private int groupTaskReward;
    public Account(String userId) {
        this.userId = userId;
        name = "初来乍到";
        groupId = UUID.randomUUID();
        age = 1;
        sex = 1;
        weight = 50;
        height = 160;
        groupTaskState = 0;
        groupTaskReward = 0;
    }

    public int getGroupTaskReward() {
        return groupTaskReward;
    }

    public void setGroupTaskReward(int groupTaskReward) {
        this.groupTaskReward = groupTaskReward;
    }

    public Account(){}
    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
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
