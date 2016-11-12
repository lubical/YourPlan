package com.lubical.android.yourplan;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by lubical on 2016/11/11.
 */

public class PlanLab {
    private static final String TAG ="PlanLab";
    private static final String FILENAME = "plan.json";
    private ArrayList<Plan> mPlans;
    private static PlanLab sPlanLab;
    private Context mContext;
    private PlanIntentJSONSerializer<Plan> mSerializer;

    public static String getmUserId() {
        return mUserId;
    }

    public static void setmUserId(String mUserId) {
        PlanLab.mUserId = mUserId;
    }

    private static String mUserId;
    private PlanLab(Context context, String userId) {
        mContext = context;
        mUserId = userId;
        mSerializer = new PlanIntentJSONSerializer<Plan>(mContext, FILENAME);
        try{
            mPlans = mSerializer.load();
            filterUserPlans();
        } catch (Exception e) {
            Log.e(TAG, "Error loading plans: ", e);
        }
    }
    public static PlanLab get(Context context) {
        if (sPlanLab == null) {
            sPlanLab = new PlanLab(context.getApplicationContext(), mUserId);
        }
        if (mUserId == null) {
            Log.d(TAG,"PlanLab userId not exist wrong!!!");
        }
        return sPlanLab;
    }
    public static PlanLab get(Context context, String userId) {
        if (sPlanLab == null) {
            sPlanLab = new PlanLab(context.getApplicationContext(), userId);
        }
        return sPlanLab;
    }
    public void filterUserPlans() {
        if (mPlans!=null) {
            for (Plan p:mPlans)
                if (!p.getUserID().equals(mUserId)) {
                    mPlans.remove(p);
                }
        }
    }
    public ArrayList<Plan>getPlans() {
        return mPlans;
    }
    public ArrayList<Plan>getPlans(int importUrgent) {
        ArrayList<Plan> plans = new ArrayList<Plan>();
        for (Plan p:mPlans) {
            if (p.getPlanImportantUrgent() == importUrgent) {
                plans.add(p);
            }
        }
        return plans;
    }
    public Plan getPlan(String planID) {
        for (Plan p:mPlans) {
            if (p.getPlanID().equals(planID)) {
                return p;
            }
        }
        return null;
    }

    public boolean addPlan(Plan plan) {
        if (plan.getPlanName() == null) {
            return false;
        }
        mPlans.add(plan);
        return true;
    }

    public boolean savePlan() {
        try {
            mSerializer.save(mPlans);
            return true;
        } catch (Exception e) {
            Log.e(TAG,"Error saving plans: ", e);
            return false;
        }
    }

    public boolean deletePlan(Plan plan) {
        Plan temp = getPlan(plan.getPlanID());
        if (temp == null) {
            Log.d(TAG, "deletePlan failure: not exist");
            return false;
        }
        mPlans.remove(plan);
        return true;
    }
}
