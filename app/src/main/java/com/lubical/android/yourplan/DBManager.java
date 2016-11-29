package com.lubical.android.yourplan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lubical.android.yourplan.account.Account;
import com.lubical.android.yourplan.review.Review;
import com.lubical.android.yourplan.review.ReviewThumb;
import com.lubical.android.yourplan.share.Share;
import com.lubical.android.yourplan.user.User;
import com.lubical.android.yourplan.group.Group;
import com.lubical.android.yourplan.group.GroupApply;
import com.lubical.android.yourplan.plan.Plan;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;


/**
 * Created by lubical on 2016/11/24.
 */

public class DBManager {
    private static final String TAG = "DBManager";
    private DBHelper mHelper;
    private SQLiteDatabase db;

    public static final short NOEXIST = 0;
    public static final short UNMATCH = 1;
    public static final short MATCH = 2;

    public DBManager(Context context) {
        mHelper = new DBHelper(context);
        db = mHelper.getWritableDatabase();
    }
    /**************AccountManager start****************/
    public void addAccount(Account account) {
        db.execSQL("INSERT INTO Account VALUES(?,?,?,?,?,?,?,?,?)", new Object[]{
                account.getUserId(),account.getName(),account.getAge(),account.getSex(),
                account.getWeight(),account.getHeight(),account.getGroupId().toString(),
                account.getGroupTaskState(),account.getGroupTaskReward()
        });
    }
    private HashMap<String, Object> getAccountHashMap(Cursor c) {
        HashMap<String, Object> map = new HashMap<String,Object>();
        map.put(Account.ACCOUNT_USER_ID, c.getString(c.getColumnIndex(Account.ACCOUNT_USER_ID)));
        map.put(Account.ACCOUNT_GROUP_ID, c.getString(c.getColumnIndex(Account.ACCOUNT_GROUP_ID)));
        map.put(Account.ACCOUNT_AGE, c.getInt(c.getColumnIndex(Account.ACCOUNT_AGE)));
        map.put(Account.ACCOUNT_WEIGHT,c.getInt(c.getColumnIndex(Account.ACCOUNT_WEIGHT)));
        map.put(Account.ACCOUNT_HEIGTH, c.getInt(c.getColumnIndex(Account.ACCOUNT_HEIGTH)));
        map.put(Account.ACCOUNT_GROUP_TASK_STATE, c.getInt(c.getColumnIndex(Account.ACCOUNT_GROUP_TASK_STATE)));
        map.put(Account.ACCOUNT_SEX, c.getInt(c.getColumnIndex(Account.ACCOUNT_SEX)));
        map.put(Account.ACCOUNT_NAME, c.getString(c.getColumnIndex(Account.ACCOUNT_NAME)));
        map.put(Account.ACCOUNT_GROUP_TASK_REWARD,c.getInt(c.getColumnIndex(Account.ACCOUNT_GROUP_TASK_REWARD)));
        return  map;
    }
    public List<HashMap<String, Object>>getAccountMapList(UUID groupId) {
        List<HashMap<String, Object>> mapList = new ArrayList<HashMap<String, Object>>();
        Cursor c = db.rawQuery("SELECT * FROM Account WHERE groupId=? ORDER BY groupTaskState DESC",new String[]{groupId.toString()});
        while (c.moveToNext()) {
            mapList.add(getAccountHashMap(c));
        }
        c.close();
        return mapList;
    }
    public List<HashMap<String, Object>>getAccountMapList() {
        List<HashMap<String, Object>> mapList = new ArrayList<HashMap<String, Object>>();
        Cursor c = db.rawQuery("SELECT * FROM Account",null);
        while (c.moveToNext()) {
            mapList.add(getAccountHashMap(c));
        }
        c.close();
        return mapList;
    }
    public void addAccount(List<Account> accounts) {
        db.beginTransaction();
        try {
            for(Account account:accounts) {
               addAccount(account);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
    public void deleteAccount(String userId) {
        db.delete("Account","userId=?",new String[]{userId});
    }
    public void deleteAccount(Account account) {
        db.delete("Account","userId=?",new String[]{account.getUserId()});
    }
    public void updateAccount(Account account) {
        ContentValues cv = new ContentValues();
        cv.put(Account.ACCOUNT_NAME, account.getName());
        cv.put(Account.ACCOUNT_AGE, account.getAge());
        cv.put(Account.ACCOUNT_HEIGTH, account.getHeight());
        cv.put(Account.ACCOUNT_SEX, account.getSex());
        cv.put(Account.ACCOUNT_WEIGHT, account.getWeight());
        cv.put(Account.ACCOUNT_GROUP_ID, account.getGroupId().toString());
        cv.put(Account.ACCOUNT_GROUP_TASK_STATE, account.getGroupTaskState());
        cv.put(Account.ACCOUNT_GROUP_TASK_REWARD, account.getGroupTaskReward());
        db.update("Account",cv,"userId=?",new String[]{account.getUserId()});
    }
    public void accountGroupStateReset (UUID groupId) {
        ContentValues cv = new ContentValues();
        cv.put(Account.ACCOUNT_GROUP_TASK_STATE, 0);
        cv.put(Account.ACCOUNT_GROUP_TASK_REWARD, 0);
        db.update("Account", cv, "groupId=?", new String[]{groupId.toString()});
    }
    public Account getAccount(String userId) {
        Account mAccount = new Account("this");
        Cursor c = db.rawQuery("SELECT * FROM Account where userId=?",new String[]{userId});
        if (c.getCount() == 0) {
            Log.e(TAG, "no account"+userId);
            return null;
        }
        if (c.getCount()>1) {
            Log.e(TAG, "find two account have same userId");
        }
        c.moveToNext();
        mAccount.setUserId(c.getString(c.getColumnIndex(Account.ACCOUNT_USER_ID)));
        mAccount.setHeight(c.getInt(c.getColumnIndex(Account.ACCOUNT_HEIGTH)));
        mAccount.setGroupId(UUID.fromString(c.getString(c.getColumnIndex(Account.ACCOUNT_GROUP_ID))));
        mAccount.setAge(c.getInt(c.getColumnIndex(Account.ACCOUNT_AGE)));
        mAccount.setName(c.getString(c.getColumnIndex(Account.ACCOUNT_NAME)));
        mAccount.setGroupTaskState(c.getInt(c.getColumnIndex(Account.ACCOUNT_GROUP_TASK_STATE)));
        mAccount.setWeight(c.getInt(c.getColumnIndex(Account.ACCOUNT_WEIGHT)));
        mAccount.setSex(c.getInt(c.getColumnIndex(Account.ACCOUNT_SEX)));
        mAccount.setGroupTaskReward(c.getInt(c.getColumnIndex(Account.ACCOUNT_GROUP_TASK_REWARD)));
        c.close();
        return mAccount;
    }
    private void initAccount() {
        Account account = new Account("admin");
        account.setAge(20);
        account.setHeight(168);
        account.setGroupTaskState(0);
        account.setSex(0);
        account.setWeight(60);
        account.setName("lubical");
        account.setGroupTaskReward(0);
        addAccount(account);
    }
    /**************AccountManager end****************/

    /**************GroupApplyManager start****************/
    public boolean addGroupApply(GroupApply groupApply) {
        try {
            db.execSQL("INSERT INTO GroupApply VALUES(?,?)", new Object[]{
                    groupApply.getUserId(), groupApply.getGroupId().toString()});
            return true;
        } catch (Exception e) {
            Log.e(TAG, "have groupApply already");
            return false;
        }
    }
    public void addGroupApply(List<GroupApply> groupApplys) {
        db.beginTransaction();
        try {
            for(GroupApply groupApply:groupApplys) {
                addGroupApply(groupApply);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
    public void deleteGroupApply(String userId, UUID groupId) {
        db.delete("GroupApply","userId=? and groupId=?",
                new String[]{userId,groupId.toString()});
    }
    public void deleteGroupApply(GroupApply groupApply) {
        db.delete("GroupApply","userId=? and groupId=?",
                new String[]{groupApply.getUserId(),groupApply.getGroupId().toString()});
    }
    public List<HashMap<String, Object>> getGroupApplyMapList() {
        List<HashMap<String, Object>> mapList = new ArrayList<HashMap<String, Object>>();
        Cursor c = db.rawQuery("SELECT * FROM GroupApply",null);
        while (c.moveToNext()) {
            mapList.add(getGroupApplyHashMap(c));
        }
        c.close();
        return mapList;
    }
    public List<GroupApply> getGroupApply() {
        List<GroupApply>groupApplies = new ArrayList<GroupApply>();
        Cursor c = db.rawQuery("SELECT * FROM GroupApply",null);
        while(c.moveToNext()) {
            GroupApply mGroupApply = new GroupApply();
            mGroupApply.setUserId(c.getString(c.getColumnIndex(GroupApply.USER_ID)));
            mGroupApply.setGroupId(UUID.fromString(c.getString(c.getColumnIndex(GroupApply.GROUPID))));
            groupApplies.add(mGroupApply);
        }
        c.close();
        return groupApplies;
    }
    private HashMap<String, Object> getGroupApplyHashMap(Cursor c) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(GroupApply.GROUPID, c.getString(c.getColumnIndex(GroupApply.GROUPID)));
        map.put(GroupApply.USER_ID, c.getString(c.getColumnIndex(GroupApply.USER_ID)));
        return map;
    }
    public List<HashMap<String, Object>> getGroupApplyMapList(UUID groupId) {
        List<HashMap<String, Object>> mapList = new ArrayList<HashMap<String, Object>>();
        Cursor c = db.rawQuery("SELECT * FROM GroupApply WHERE groupId=?",new String[]{groupId.toString()});
        while (c.moveToNext()) {
            mapList.add(getGroupApplyHashMap(c));
        }
        c.close();
        return mapList;
    }
    public List<GroupApply> getGroupApply(UUID groupId) {
        List<GroupApply>groupApplies = new ArrayList<GroupApply>();
        Cursor c = db.rawQuery("SELECT * FROM GroupApply where groupId=?",new String[]{groupId.toString()});
        while(c.moveToNext()) {
            GroupApply mGroupApply = new GroupApply();
            mGroupApply.setUserId(c.getString(c.getColumnIndex(GroupApply.USER_ID)));
            mGroupApply.setGroupId(UUID.fromString(c.getString(c.getColumnIndex(GroupApply.GROUPID))));
            groupApplies.add(mGroupApply);
        }
        c.close();
        return groupApplies;
    }
    private void initGroupApply() {
        GroupApply groupApply = new GroupApply(UUID.randomUUID(),"admin");
        addGroupApply(groupApply);
    }
    /**************GroupApplyManager end****************/

    /**************GroupManager start****************/
    public boolean addGroup(Group group) {
        try {
            db.execSQL("INSERT INTO MGroup VALUES(?,?,?,?,?,?)", new Object[]{
                    group.getGroupId().toString(), group.getGroupName(), group.getGroupPlanId().toString(),
                    group.getGroupMemberCount(), group.getGroupOwnerId(), group.getGroupPlanRewardTimes()});
            return true;
        } catch (Exception e) {
            Log.e(TAG, "already have group");
            return false;
        }
    }
    public void addGroup(List<Group> groups) {
        db.beginTransaction();
        try {
            for(Group group:groups) {
                addGroup(group);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
    public void deleteGroup(String groupId) {
        db.delete("MGroup","groupId=?",new String[]{groupId});
    }
    public void deleteGroup(Group group) {
        db.delete("MGroup","groupId=?",new String[]{group.getGroupId().toString()});
    }
    public void updateGroup(Group group) {
        ContentValues cv = new ContentValues();
        cv.put(Group.GROUP_OWNER_ID, group.getGroupOwnerId());
        cv.put(Group.GROUP_NAME, group.getGroupName());
        cv.put(Group.GROUP_MEMBER_COUNT, group.getGroupMemberCount());
        cv.put(Group.GROUP_PLAN_ID, group.getGroupPlanId().toString());
        cv.put(Group.GROUP_PLAN_REWARD_TIMES, group.getGroupPlanRewardTimes());
        db.update("MGroup",cv,"groupId=?",new String[]{group.getGroupId().toString()});
    }
    public List<HashMap<String, Object>> getGroupsMapList() {
        List<HashMap<String, Object>> mapList = new ArrayList<HashMap<String, Object>>();
        Cursor c = db.rawQuery("SELECT * FROM MGroup ",null);
        while (c.moveToNext()) {
            mapList.add(getGroupHashMap(c));
        }
        c.close();
        return mapList;
    }
    public List<Group> getGroups() {
        List<Group> groups = new ArrayList<Group>();
        Cursor c = db.rawQuery("SELECT * FROM MGroup ",null);
        Log.d(TAG, "group number"+c.getCount());
        while(c.moveToNext()) {

            Group mGroup = new Group();
            mGroup.setGroupId(UUID.fromString(c.getString(c.getColumnIndex(Group.GROUP_ID))));
            mGroup.setGroupPlanId(UUID.fromString(c.getString(c.getColumnIndex(Group.GROUP_PLAN_ID))));
            mGroup.setGroupMemberCount(c.getInt(c.getColumnIndex(Group.GROUP_MEMBER_COUNT)));
            mGroup.setGroupName(c.getString(c.getColumnIndex(Group.GROUP_NAME)));
            mGroup.setGroupOwnerId(c.getString(c.getColumnIndex(Group.GROUP_OWNER_ID)));
            mGroup.setGroupPlanRewardTimes(c.getInt(c.getColumnIndex(Group.GROUP_PLAN_REWARD_TIMES)));
            groups.add(mGroup);
        }
        c.close();
        return groups;
    }
    private HashMap<String, Object>getGroupHashMap(Cursor c) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(Group.GROUP_ID, c.getString(c.getColumnIndex(Group.GROUP_ID)));
        map.put(Group.GROUP_OWNER_ID, c.getString(c.getColumnIndex(Group.GROUP_OWNER_ID)));
        map.put(Group.GROUP_PLAN_ID, c.getString(c.getColumnIndex(Group.GROUP_PLAN_ID)));
        map.put(Group.GROUP_NAME, c.getString(c.getColumnIndex(Group.GROUP_NAME)));
        map.put(Group.GROUP_MEMBER_COUNT, c.getString(c.getColumnIndex(Group.GROUP_MEMBER_COUNT)));
        map.put(Group.GROUP_PLAN_REWARD_TIMES, c.getInt(c.getColumnIndex(Group.GROUP_PLAN_REWARD_TIMES)));
        return map;
    }
    public List<HashMap<String, Object>>getGroupsMapList(String name) {
        Cursor c = db.rawQuery("SELECT * FROM MGroup where gruopName like ? or groupId=?",new String[]{name,name});
        List<HashMap<String, Object>> mapList = new ArrayList<HashMap<String, Object>>();
        while(c.moveToNext()) {
            mapList.add(getGroupHashMap(c));
        }
        c.close();
        return  mapList;
    }
    public List<Group> getGroups(String name) {
        List<Group> groups = new ArrayList<Group>();
        Cursor c = db.rawQuery("SELECT * FROM MGroup where groupName like ? or groupId=?",new String[]{name,name});
        while(c.moveToNext()) {
            Log.d(TAG,"after first group"+c.getString(0));
            Group mGroup = new Group();
            mGroup.setGroupId(UUID.fromString(c.getString(c.getColumnIndex(Group.GROUP_ID))));
            mGroup.setGroupPlanId(UUID.fromString(c.getString(c.getColumnIndex(Group.GROUP_PLAN_ID))));
            mGroup.setGroupMemberCount(c.getInt(c.getColumnIndex(Group.GROUP_MEMBER_COUNT)));
            mGroup.setGroupName(c.getString(c.getColumnIndex(Group.GROUP_NAME)));
            mGroup.setGroupOwnerId(c.getString(c.getColumnIndex(Group.GROUP_OWNER_ID)));
            mGroup.setGroupPlanRewardTimes(c.getInt(c.getColumnIndex(Group.GROUP_PLAN_REWARD_TIMES)));
            groups.add(mGroup);
        }
        c.close();
        return groups;
    }
    public boolean isExistGroup(UUID groupId) {
        Cursor c = db.rawQuery("SELECT * FROM MGroup where groupId=?",new String[]{groupId.toString()});
        if (c.getCount() == 0) {
            return false;
        }else {
            return true;
        }
    }
    public Group getGroup(UUID groupId) {
        Group mGroup = new Group("this");
        Cursor c = db.rawQuery("SELECT * FROM MGroup where groupId=?",new String[]{groupId.toString()});
        if (c.getCount() == 0) {
            Log.e(TAG, "no this group"+groupId);
            return null;
        }
        if (c.getCount()>1) {
            Log.e(TAG, "find two group have same groupId");
        }
        c.moveToNext();
        mGroup.setGroupId(c.getString(c.getColumnIndex(Group.GROUP_ID)));
        mGroup.setGroupPlanId(UUID.fromString(c.getString(c.getColumnIndex(Group.GROUP_PLAN_ID))));
        mGroup.setGroupMemberCount(c.getInt(c.getColumnIndex(Group.GROUP_MEMBER_COUNT)));
        mGroup.setGroupName(c.getString(c.getColumnIndex(Group.GROUP_NAME)));
        mGroup.setGroupOwnerId(c.getString(c.getColumnIndex(Group.GROUP_OWNER_ID)));
        mGroup.setGroupPlanRewardTimes(c.getInt(c.getColumnIndex(Group.GROUP_PLAN_REWARD_TIMES)));
        c.close();
        return mGroup;
    }
    private void initGroup(){
        Group group = new Group("admin");
        group.setGroupName("测试组");
        group.setGroupMemberCount(1);
        group.setGroupPlanId(UUID.randomUUID());
        group.setGroupPlanRewardTimes(0);
        addGroup(group);
    }
    /**************GroupApplyManager end****************/

    /**************PlanManager start****************/
    public void addPlan(Plan plan) {
        db.execSQL("INSERT INTO Plan VALUES(?,?,?,?,?,?,?,?,?,?)", new Object[]{
                plan.getPlanID().toString(),plan.getUserID(),plan.getPlanName(),plan.getPlanStartTime().getTime(),
                plan.getPlanRemindTime().getTime(),plan.getPlanEndTime().getTime(),plan.getPlanRepeatFrequency(),
                plan.getPlanStatue(),plan.getPlanImportantUrgent(),plan.getPlanClassify()});
    }
    public void addPlans(List<Plan> plans) {
        db.beginTransaction();
        try {
            for(Plan plan:plans) {
                addPlan(plan);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
    public void deletePlan(UUID planId) {
        db.delete("Plan","planId=?",new String[]{planId.toString()});
    }
    public void deletePlan(Plan plan) {
        db.delete("Plan","planId=?",new String[]{plan.getPlanID().toString()});
    }
    public void updatePlan(Plan plan) {
        ContentValues cv = new ContentValues();
        cv.put(Plan.USER_ID, plan.getUserID());
        cv.put(Plan.PLAN_NAME, plan.getPlanName());
        cv.put(Plan.PLAN_START_TIME, plan.getPlanStartTime().getTime());
        cv.put(Plan.PLAN_REMIND_TIME, plan.getPlanRemindTime().getTime());
        cv.put(Plan.PLAN_END_TIME, plan.getPlanEndTime().getTime());
        cv.put(Plan.PLAN_REPEATFREQUENCY, plan.getPlanRepeatFrequency());
        cv.put(Plan.PLAN_STATUE, plan.getPlanStatue());
        cv.put(Plan.PLAN_IMPORT_URGENT, plan.getPlanImportantUrgent());
        cv.put(Plan.PLAN_CLASSIFY, plan.getPlanClassify());
        db.update("Plan",cv,"planId=?",new String[]{plan.getPlanID().toString()});
    }
    public List<HashMap<String,Object>> getUserPlanMapList(String userId, int import_urgent) {
        List<HashMap<String,Object>> mapList = new ArrayList<HashMap<String, Object>>();
        Cursor c= db.rawQuery("SELECT * FROM Plan where userid=? and planImportantUrgent=?"
                ,new String[]{userId,Integer.toString(import_urgent)});
        while(c.moveToNext()) {
            mapList.add(getPlanHashMap(c));
        }
        c.close();
        return mapList;
    }
    public List<Plan> getUserPlan(String userId,int import_urgent) {
        List<Plan> plen = new ArrayList<Plan>();
        Cursor c= db.rawQuery("SELECT * FROM Plan where userid=? and planImportantUrgent=?"
                ,new String[]{userId,Integer.toString(import_urgent)});
        while(c.moveToNext()) {
            Plan mPlan = new Plan("this");
            Log.d(TAG,"columnIndex"+c.getColumnIndex(Plan.PLAN_ID));
            mPlan.setPlanID(UUID.fromString(c.getString(c.getColumnIndex(Plan.PLAN_ID))));
            mPlan.setUserID(c.getString(c.getColumnIndex(Plan.USER_ID)));
            mPlan.setPlanName(c.getString(c.getColumnIndex(Plan.PLAN_NAME)));
            mPlan.setPlanStartTime(c.getLong(c.getColumnIndex(Plan.PLAN_START_TIME)));
            mPlan.setPlanRemindTime(c.getLong(c.getColumnIndex(Plan.PLAN_REMIND_TIME)));
            mPlan.setPlanEndTime(c.getLong(c.getColumnIndex(Plan.PLAN_END_TIME)));
            mPlan.setPlanRepeatFrequency(c.getInt(c.getColumnIndex(Plan.PLAN_REPEATFREQUENCY)));
            mPlan.setPlanStatue(c.getInt(c.getColumnIndex(Plan.PLAN_STATUE)));
            mPlan.setPlanImportantUrgent(c.getInt(c.getColumnIndex(Plan.PLAN_IMPORT_URGENT)));
            mPlan.setPlanClassify(c.getString(c.getColumnIndex(Plan.PLAN_CLASSIFY)));
            plen.add(mPlan);
        }
        c.close();
        return plen;
    }
    private HashMap<String,Object>getPlanHashMap(Cursor c) {
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put(Plan.PLAN_ID, c.getString(c.getColumnIndex(Plan.PLAN_ID)));
        map.put(Plan.USER_ID, c.getString(c.getColumnIndex(Plan.USER_ID)));
        map.put(Plan.PLAN_NAME, c.getString(c.getColumnIndex(Plan.PLAN_NAME)));
        map.put(Plan.PLAN_START_TIME, c.getLong(c.getColumnIndex(Plan.PLAN_START_TIME)));
        map.put(Plan.PLAN_REMIND_TIME, c.getLong(c.getColumnIndex(Plan.PLAN_REMIND_TIME)));
        map.put(Plan.PLAN_END_TIME, c.getLong(c.getColumnIndex(Plan.PLAN_END_TIME)));
        map.put(Plan.PLAN_REPEATFREQUENCY, c.getInt(c.getColumnIndex(Plan.PLAN_REPEATFREQUENCY)));
        map.put(Plan.PLAN_STATUE, c.getInt(c.getColumnIndex(Plan.PLAN_STATUE)));
        map.put(Plan.PLAN_IMPORT_URGENT, c.getInt(c.getColumnIndex(Plan.PLAN_IMPORT_URGENT)));
        map.put(Plan.PLAN_CLASSIFY, c.getString(c.getColumnIndex(Plan.PLAN_CLASSIFY)));

        return map;
    }
    public List<HashMap<String,Object>> getUserPlanMapList(String userId) {
        List<HashMap<String,Object>> mapList = new ArrayList<HashMap<String, Object>>();
        Cursor c = db.rawQuery("SELECT * FROM Plan where userId=?",new String[]{userId});
        while(c.moveToNext()) {
            mapList.add(getPlanHashMap(c));
        }
        c.close();
        return mapList;
    }
    public List<Plan> getUserPlan(String userId) {
        List<Plan> plen = new ArrayList<Plan>();
        Cursor c = db.rawQuery("SELECT * FROM Plan where userId=?",new String[]{userId});
        while(c.moveToNext()) {
            Plan mPlan = new Plan("this");
            mPlan.setPlanID(UUID.fromString(c.getString(c.getColumnIndex(Plan.PLAN_ID))));
            mPlan.setUserID(c.getString(c.getColumnIndex(Plan.USER_ID)));
            mPlan.setPlanName(c.getString(c.getColumnIndex(Plan.PLAN_NAME)));
            mPlan.setPlanStartTime(c.getLong(c.getColumnIndex(Plan.PLAN_START_TIME)));
            mPlan.setPlanRemindTime(c.getLong(c.getColumnIndex(Plan.PLAN_REMIND_TIME)));
            mPlan.setPlanEndTime(c.getLong(c.getColumnIndex(Plan.PLAN_END_TIME)));
            mPlan.setPlanRepeatFrequency(c.getInt(c.getColumnIndex(Plan.PLAN_REPEATFREQUENCY)));
            mPlan.setPlanStatue(c.getInt(c.getColumnIndex(Plan.PLAN_STATUE)));
            mPlan.setPlanImportantUrgent(c.getInt(c.getColumnIndex(Plan.PLAN_IMPORT_URGENT)));
            mPlan.setPlanClassify(c.getString(c.getColumnIndex(Plan.PLAN_CLASSIFY)));
            plen.add(mPlan);
        }
        c.close();
        return plen;

    }
    public List<HashMap<String,Object>> getPlanClassifyMapList(String userId) {
        List<HashMap<String,Object>> mapList = new ArrayList<HashMap<String, Object>>();
        Cursor c = db.rawQuery("SELECT DISTINCT planClassify FROM Plan WHREE userId=?",new String[]{userId});
        while (c.moveToNext()) {
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put(Plan.PLAN_CLASSIFY, c.getString(c.getColumnIndex(Plan.PLAN_CLASSIFY)));
            mapList.add(map);
        }
        c.close();
        return mapList;
    }
    public List<String>getPlanClassify(String userId) {
        List<String> planClassifys = new ArrayList<String>();
        Cursor c = db.rawQuery("SELECT DISTINCT planClassify FROM Plan WHERE userId=?",new String[]{userId});
        while (c.moveToNext()) {
            planClassifys.add(c.getString(c.getColumnIndex(Plan.PLAN_CLASSIFY)));
        }
        c.close();
        return planClassifys;
    }
    public Plan getPlan(UUID planId) {
        Cursor c = db.rawQuery("SELECT * FROM Plan where planId=?",new String[]{planId.toString()});
        if (c.getCount() == 0) {
            Log.e(TAG, "no plan"+planId);
            return null;
        }
        c.moveToNext();
        Plan mPlan = new Plan();
        mPlan.setPlanID(planId);
        mPlan.setUserID(c.getString(c.getColumnIndex(Plan.USER_ID)));
        mPlan.setPlanName(c.getString(c.getColumnIndex(Plan.PLAN_NAME)));
        mPlan.setPlanStartTime(c.getLong(c.getColumnIndex(Plan.PLAN_START_TIME)));
        mPlan.setPlanRemindTime(c.getLong(c.getColumnIndex(Plan.PLAN_REMIND_TIME)));
        mPlan.setPlanEndTime(c.getLong(c.getColumnIndex(Plan.PLAN_END_TIME)));
        mPlan.setPlanRepeatFrequency(c.getInt(c.getColumnIndex(Plan.PLAN_REPEATFREQUENCY)));
        mPlan.setPlanStatue(c.getInt(c.getColumnIndex(Plan.PLAN_STATUE)));
        mPlan.setPlanImportantUrgent(c.getInt(c.getColumnIndex(Plan.PLAN_IMPORT_URGENT)));
        mPlan.setPlanClassify(c.getString(c.getColumnIndex(Plan.PLAN_CLASSIFY)));

        c.close();
        return mPlan;
    }
    private void initPlan() {
        Plan plan = new Plan();
        plan.setPlanName("测试计划");
        plan.setPlanImportantUrgent(0);
        plan.setPlanRepeatFrequency(1);
        plan.setPlanStatue(0);
        plan.setPlanClassify("未分组");
        plan.setPlanStartTime(new Date().getTime());
        plan.setPlanRemindTime(plan.getPlanStartTime().getTime()+60000);
        plan.setPlanEndTime(new Date().getTime());
        plan.setUserID("admin");
        addPlan(plan);
    }
    /**************PlanManager end****************/

    /**************UserManager start****************/
    public void addUser(User user) {
        db.execSQL("INSERT INTO User VALUES(?,?,?)", new Object[]{
                user.getUserId(),user.getUserPassword(),user.getUserGrade()});
    }
    public void addUser(List<User> users) {
        db.beginTransaction();
        try {
            for(User user:users) {
                addUser(user);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
    public short login(User user) {
        Cursor c = db.rawQuery("SELECT * FROM User where userId=?",new String[]{user.getUserId()});
        Cursor c1 = db.rawQuery("SELECT * FROM User where userId=? and userPassword=?",
                new String[]{user.getUserId(),user.getUserPassword()});
        if (c.getCount() == 0) {
            return NOEXIST;
        }
        if (c1.getCount() == 1) {
            return MATCH;
        }
        return UNMATCH;
    }
    public boolean changePassword(User user,String newPassword) {
        if(login(user)==MATCH) {
            user.setUserPassword(newPassword);
            updateUser(user);
            return true;
        }
        return false;
    }
    public void deleteUser(User user) {
        db.delete("User","userId=",new String[]{user.getUserId()});
    }
    public void updateUser(User user) {
        ContentValues cv = new ContentValues();
        cv.put(User.JSON_USER_PASSWORD,user.getUserPassword());
        cv.put(User.JSON_USER_GRADE, user.getUserGrade());
        db.update("User",cv,"userId=?",new String[]{user.getUserId()});
    }
    public boolean isExistUser(String userId) {
        Cursor c = db.rawQuery("SELECT * FROM User where userId=?",new String[]{userId});
        if (c.getCount() == 0) return false;
        else return true;
    }
    public User getUser(String userId) {
        Cursor c = db.rawQuery("SELECT * FROM User where userId=?",new String[]{userId});
        Log.d(TAG, "searchUser"+c.getCount());
        if (c.getCount() == 0) {
            Log.e(TAG,"no user"+userId);
            return null;
        }
        User mUser = new User();
        c.moveToNext();

        mUser.setUserId(c.getString(c.getColumnIndex(User.JSON_USER_ID)));
        mUser.setUserPassword(c.getString(c.getColumnIndex(User.JSON_USER_PASSWORD)));
        mUser.setUserGrade(c.getInt(c.getColumnIndex(User.JSON_USER_GRADE)));
        if (c.getColumnCount()>1) {
            Log.e(TAG, "find two user have same userId");
        }
        c.close();
        return mUser;
    }
    private void initUser() {
        User user = new User("admin","admin");
        user.setUserGrade(0);
        addUser(user);
    }

    /**************UserManager end****************/

    /**************Share start*********************/
    public void addShare(Share share) {
        db.execSQL("INSERT INTO Share VALUES(?,?,?,?,?,?,?,?)", new Object[]{
                share.getShareId().toString(), share.getUserId(), share.getPlanId().toString(),
                share.getMessage(), share.getGroupId().toString(), share.getTime(),
                share.getThumbUpCount(), share.getCommentCount()
                });
    }

    public void deleteShare(Share share) {
        db.delete("Share","shareId=",new String[]{share.getShareId().toString()});
    }
    public void updateShare(Share share) {
        ContentValues cv = new ContentValues();
        cv.put(Share.SHARE_MESSAGE, share.getMessage());
        cv.put(Share.SHARE_PLANID, share.getPlanId().toString());
        cv.put(Share.SHARE_USERID, share.getUserId());
        cv.put(Share.SHARE_TIME, share.getTime());
        cv.put(Share.SHARE_GROUPID, share.getGroupId().toString());
        cv.put(Share.SHARE_COMMENTCOUNT, share.getCommentCount());
        cv.put(Share.SHARE_THUMBUPCOUNT, share.getThumbUpCount());
        db.update("Share",cv,"shareId=?",new String[]{share.getShareId().toString()});
    }
    public boolean isExistShare(UUID planId) {
        Cursor c = db.rawQuery("SELECT * FROM Share WHERE planId=?",new String[]{planId.toString()});
        if (c.getCount() == 0) return false;
        else return true;
    }
    public Share getShare(UUID shareId) {
        Share share = new Share();
        Cursor c = db.rawQuery("SELECT * FROM Share where shareId = ?", new String[]{shareId.toString()});
        if (c.getCount() == 0) return null;
        c.moveToNext();
        share.setShareId(UUID.fromString(c.getString(c.getColumnIndex(Share.SHARE_SHAREID))));
        share.setPlanId(UUID.fromString(c.getString(c.getColumnIndex(Share.SHARE_PLANID))));
        share.setCommentCount(c.getInt(c.getColumnIndex(Share.SHARE_COMMENTCOUNT)));
        share.setThumbUpCount(c.getInt(c.getColumnIndex(Share.SHARE_THUMBUPCOUNT)));
        share.setGroupId(UUID.fromString(c.getString(c.getColumnIndex(Share.SHARE_GROUPID))));
        share.setTime(c.getInt(c.getColumnIndex(Share.SHARE_TIME)));
        share.setUserId(c.getString(c.getColumnIndex(Share.SHARE_USERID)));
        share.setMessage(c.getString(c.getColumnIndex(Share.SHARE_MESSAGE)));
        c.close();
        return share;
    }
    private HashMap<String, Object>getShareHashMap(Cursor c) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(Share.SHARE_SHAREID, c.getString(c.getColumnIndex(Share.SHARE_SHAREID)));
        map.put(Share.SHARE_USERID, c.getString(c.getColumnIndex(Share.SHARE_USERID)));
        map.put(Share.SHARE_PLANID, c.getString(c.getColumnIndex(Share.SHARE_PLANID)));
        map.put(Share.SHARE_MESSAGE, c.getString(c.getColumnIndex(Share.SHARE_MESSAGE)));
        map.put(Share.SHARE_GROUPID, c.getString(c.getColumnIndex(Share.SHARE_GROUPID)));
        map.put(Share.SHARE_TIME, c.getInt(c.getColumnIndex(Share.SHARE_TIME)));
        map.put(Share.SHARE_THUMBUPCOUNT, c.getInt(c.getColumnIndex(Share.SHARE_THUMBUPCOUNT)));
        map.put(Share.SHARE_COMMENTCOUNT, c.getInt(c.getColumnIndex(Share.SHARE_COMMENTCOUNT)));
        map.put(Plan.PLAN_NAME, c.getString(c.getColumnIndex(Plan.PLAN_NAME)));
        map.put(Account.ACCOUNT_NAME, c.getString(c.getColumnIndex(Account.ACCOUNT_NAME)));
        int times = c.getInt(c.getColumnIndex(Plan.PLAN_REPEATFREQUENCY));
        int statue = c.getInt(c.getColumnIndex(Plan.PLAN_STATUE));
        map.put("statue",statue+"/"+times);
        return map;
    }

    public List<HashMap<String, Object>> getShareMapList(UUID groupId) {
        Log.d(TAG," shareMapList ");
        List<HashMap<String, Object>> mapList = new ArrayList<HashMap<String, Object>>();
        Cursor c = db.rawQuery("SELECT * FROM Share,Plan,Account WHERE Share.groupId = ? "  +
                        " and Share.planId = Plan.planId and Share.userId = Account.userId "  +
                        " ORDER BY Share.time DESC",
                new String[]{groupId.toString()});
        while (c.moveToNext()) {
            mapList.add(getShareHashMap(c));
        }
        c.close();
        return mapList;
    }
    /**************Share end***********************/

    /**************Review start***********************/
    public void addReview(Review review) {
        db.execSQL("INSERT INTO Review VALUES(?,?,?,?,?)", new Object[]{
                review.getReviewId().toString(), review.getShareId().toString(), review.getUserId(),
                review.getComment(), review.getTime()
        });
    }
    private HashMap<String, Object>getReviewHashMap(Cursor c) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(Review.REVIEW_SHAREID, c.getString(c.getColumnIndex(Review.REVIEW_SHAREID)));
        map.put(Review.REVIEW_REVIEWID, c.getString(c.getColumnIndex(Review.REVIEW_REVIEWID)));
        map.put(Review.REVIEW_COMMENT, c.getString(c.getColumnIndex(Review.REVIEW_COMMENT)));
        map.put(Review.REVIEW_TIME, c.getInt(c.getColumnIndex(Review.REVIEW_TIME)));
        map.put(Review.REVIEW_USERID, c.getString(c.getColumnIndex(Review.REVIEW_USERID)));
        map.put(Account.ACCOUNT_NAME, c.getString(c.getColumnIndex(Account.ACCOUNT_NAME)));
        return map;
    }
    public List<HashMap<String, Object>> getReviewMapList(UUID shareId) {
        List<HashMap<String, Object>> mapList = new ArrayList<HashMap<String, Object>>();
        Cursor c =  db.rawQuery("SELECT * FROM Review,Account WHERE Review.shareId = ? " +
                " and Review.userId = Account.userId ORDER BY time DESC limit 0,5",
                              new String[]{shareId.toString()});
        while (c.moveToNext()) {
            mapList.add(getReviewHashMap(c));
        }
        c.close();
        return mapList;
    }
    public void deleteReview(Review review) {
        db.delete("Review","reviewId=?",new String[]{review.getReviewId().toString()});
    }
    /**************Review end***********************/

    /**************ReviewThumb start*****************/
    public void addReviewThumb(ReviewThumb reviewThumb) {
        db.execSQL("INSERT INTO ReviewThumb VALUES(?,?)", new Object[]{
                reviewThumb.getShareId().toString(), reviewThumb.getUserId()
        });
    }
    public boolean isExistThumb(ReviewThumb reviewThumb) {
        Cursor c = db.rawQuery("SELECT * FROM ReviewThumb WHERE shareId=? and userId=?",
                new String[]{reviewThumb.getShareId().toString(), reviewThumb.getUserId()});
        if (c.getCount() == 0) return false;
        else return true;
    }
    public void deleteReviewThumb(ReviewThumb reviewThumb) {
        db.delete("ReviewThumb","shareId=? and userId=?",
                new String[]{reviewThumb.getShareId().toString(), reviewThumb.getUserId()});
    }
    /**************ReviewThumb end*******************/
    public void init() {
        if (!tableIsExist("User")) {
            Log.d(TAG, "table User is exist");
            initAccount();
            initUser();
            initGroupApply();
            initGroup();
            initPlan();
        }
    }
    public boolean tableIsExist(String tableName) {
        if (tableName == null)
            return false;
        try {

             Cursor c = db.rawQuery("select count(*) from sqlite_master where type=? and name=?",
                    new String[]{"table",tableName});
            if (c.moveToNext()) {
                if (c.getInt(0)>0) {
                    return true;
                }
            }

        } catch (Exception e) {
            Log.d(TAG, "tableIsExist failure");
        }
        return false;
    }
    public void closeDB() {
        db.close();
    }
}
