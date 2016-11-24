package com.lubical.android.yourplan;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by lubical on 2016/11/11.
 */

public class PlanLab {
    private static final String TAG = "PlanLab";
    private static final String FILENAME = "plan.json";
    private ArrayList<Plan> mPlans;
    private static PlanLab sPlanLab;
    private Context mContext;
    private PlanIntentJSONSerializer mSerializer;
    private String mUserId;

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public void reload(Context context) {
        mContext = context;
        mSerializer = new PlanIntentJSONSerializer (mContext, FILENAME);
        try {
            //mPlans = new ArrayList<Plan>();
            mPlans = mSerializer.load();
        } catch (Exception e) {
            Log.e(TAG, "Error loading plans: ", e);
        }
    }

    private PlanLab(Context context) {
       reload(context);
    }

    public static PlanLab get(Context context) {
        if (sPlanLab == null) {
            sPlanLab = new PlanLab(context.getApplicationContext());
        }

        return sPlanLab;
    }


    public ArrayList<Plan> getPlans() {
        return mPlans;
    }

    public ArrayList<Plan> getPlans(String userId,int importUrgent) {
        ArrayList<Plan> plans = new ArrayList<Plan>();
        if (mPlans ==null || mPlans.isEmpty()) return plans;
        for (Plan p : mPlans) {
            if (p.getUserID().equals(userId) &&
               (p.getPlanImportantUrgent() == importUrgent)) {
                plans.add(p);
            }
        }
        return plans;
    }

    public Plan getPlan(String planID) {
        for (Plan p : mPlans) {
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
            Log.e(TAG, "Error saving plans: ", e);
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

    public ArrayList<String> getClassfy() {
        Set<String> types = new HashSet<String>();
        for (Plan p:mPlans) {
            types.add(p.getPlanClassify());
        }
        ArrayList<String>list = new ArrayList<String>();
        for (String s:types) {
            list.add(s);
        }
        return list;
    }
}
