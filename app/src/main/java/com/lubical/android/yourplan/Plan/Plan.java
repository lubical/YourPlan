package com.lubical.android.yourplan.plan;

import java.util.Date;
import java.util.UUID;

/**
 * Created by lubical on 2016/11/11.
 */

public class Plan {
    public static final String USER_ID = "userId";
    public static final String PLAN_ID = "planId";
    public static final String PLAN_NAME = "planName";
    public static final String PLAN_START_TIME = "planStartTime";
    public static final String PLAN_END_TIME = "planEndTime";
    public static final String PLAN_REMIND_TIME = "planRemindTime";
    public static final String PLAN_REPEATFREQUENCY = "planRepeatFrequency";
    public static final String PLAN_STATUE = "planState";
    public static final String PLAN_IMPORT_URGENT = "planImportantUrgent";
    public static final String PLAN_CLASSIFY = "planClassify";
    private String userID;
    private UUID planID;  //修改为int
    private String planName;
    private Date planStartTime;
    private Date planRemindTime;
    private Date planEndTime;
    private int planRepeatFrequency;      //任务周期数
    private int planState;               //任务状态
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


    public Plan(){planID = UUID.randomUUID();}
    public Plan(String userID){
        this.userID = userID;
        planID = UUID.randomUUID();
        planState = 0;
        planRepeatFrequency = 1;
        planStartTime = new Date();
        planEndTime = new Date();
        planRemindTime = new Date(planStartTime.getTime()+60000);
        planImportantUrgent = IMPORTANT_URGENT;
        planClassify = "未分组";
        planName = new String("NewPlan");
    }
    public int getPlanImportantUrgent() {
        return planImportantUrgent;
    }

    public void setPlanImportantUrgent(int planImportantUrgent) {
        this.planImportantUrgent = planImportantUrgent;
    }

    public UUID getPlanID() {
        return planID;
    }

    public void setPlanID(UUID planID) {
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

    public Date getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(Date planStartTime) {
        this.planStartTime = planStartTime;
    }
    public void setPlanStartTime(long time) {
        planStartTime = new Date(time);
    }

    public Date getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(Date planEndTime) {
        this.planEndTime = planEndTime;
    }
    public void setPlanEndTime(long time) {
        planEndTime = new Date(time);
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
    public void setPlanRemindTime(long time) {
        planRemindTime = new Date(time);
    }
    public void setPlanRepeatFrequency(int planRepeatFrequency) {
        this.planRepeatFrequency = planRepeatFrequency;
    }

    public int getPlanStatue() {
        return planState;
    }

    public void setPlanStatue(int planStatue) {
        this.planState = planStatue;
    }

    public String toString() {
        return planName;
    }
}
