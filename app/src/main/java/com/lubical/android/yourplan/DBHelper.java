package com.lubical.android.yourplan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.UUID;

import static java.sql.Types.VARCHAR;

/**
 * Created by lubical on 2016/11/24.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "YourPlan.db";
    private static final int DATABASE_VERSION = 1;
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Account (userId VARCHAR PRIMARY KEY," +
                   "name VARCHAR, age INTEGER, sex INTEGER, weight INTEGER, height INTEGER," +
                   "groupId TEXT, groupTaskState INTEGER, groupTaskReward INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS GroupApply (userId VARCHAR," +
                "groupId TEXT,PRIMARY KEY(userId,groupId))");

        db.execSQL("CREATE TABLE IF NOT EXISTS MGroup (groupId TEXT PRIMARY KEY," +
                "groupName VARCHAR, groupPlanId TEXT, groupMemberCount INTEGER, "+
                "groupOwnerId VARCHAR, groupPlanRewardTimes INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS Plan (planId TEXT PRIMARY KEY," +
                "userId VARCHAR, planName VARCHAR, planStartTime INTEGER, planRemindTime INTEGER, " +
                "planEndTime INTEGER, planRepeatFrequency INTEGER, planState INTEGER, " +
                "planImportantUrgent INTEGER,planClassify VARCHAR)");

        db.execSQL("CREATE TABLE IF NOT EXISTS User (userId VARCHAR PRIMARY KEY," +
                "userPassword VARCHAR, userGrade INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS Review (reviewId TEXT PRIMARY KEY, shareId TEXT," +
                   "userId VARCHAR, comment TEXT, time INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS ReviewThumb (shareId TEXT , userId VARCHAR," +
                  "PRIMARY KEY(userId,shareId))");

        db.execSQL("CREATE TABLE IF NOT EXISTS Share (shareId TEXT PRIMARY KEY, userId VARCHAR," +
                   "planId TEXT, message TEXT, groupId TEXT, time INTEGER, thumbUpCount INTEGER," +
                   "commentCount INTEGER)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Account");
        db.execSQL("DROP TABLE IF EXISTS GroupApply");
        db.execSQL("DROP TABLE IF EXISTS MGroup");
        db.execSQL("DROP TABLE IF EXISTS Plan");
        db.execSQL("DROP TABLE IF EXISTS User");
        db.execSQL("DROP TABLE IF EXISTS Share");
        db.execSQL("DROP TABLE IF EXISTS Review");
        db.execSQL("DROP TABLE IF EXISTS ReviewThumb");
        onCreate(db);
    }
 }
