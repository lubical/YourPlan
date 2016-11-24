package com.lubical.android.yourplan;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by lubical on 2016/11/22.
 *
 */

public class GroupApplyLab {
    private static final String TAG = "GroupApplyLab";
    private static final String FILENAME = "groupapply.json";

    private ArrayList<GroupApply> mGroupApplys;
    private static GroupApplyLab sGroupApplyLab;
    private Context mContext;
    private GroupApplyIntentJSONSerializer mSerializer;
    
    private GroupApplyLab(Context context) {
        mContext = context;
        mSerializer = new GroupApplyIntentJSONSerializer(mContext, FILENAME);
        try {
            mGroupApplys = mSerializer.load();
        }catch (Exception e) {
            mGroupApplys = new ArrayList<GroupApply>();
            Log.e(TAG, "Error loading accounts:", e);
        }
    }
 
    public static GroupApplyLab get(Context context) {
        if (sGroupApplyLab == null) {
            sGroupApplyLab = new GroupApplyLab(context.getApplicationContext());
        }
        return sGroupApplyLab;
    }

    public ArrayList<GroupApply> getGroupApplys() {
        return mGroupApplys;
    }

    public ArrayList<GroupApply> getGroupApply(String goupId) {
        ArrayList<GroupApply>list = new ArrayList<GroupApply>();
        for (GroupApply groupApply:mGroupApplys) {
           if (groupApply.getGroupId().equals(goupId)) {
               list.add(groupApply);
           }
        }
        return list;
    }

    public GroupApply findGroupApply(GroupApply groupApply) {
        for (GroupApply mGroupApply:mGroupApplys) {
            if (mGroupApply.getApplyerId().equals(groupApply.getApplyerId())
                    && mGroupApply.getGroupId().equals(groupApply.getGroupId())) {
                return mGroupApply;
            }
        }
        return null;
    }

    public boolean addGroupApply(GroupApply groupApply) {
        for (GroupApply mgroupApply:mGroupApplys) {
            if (mgroupApply.getApplyerId().equals(groupApply.getApplyerId())
                && mgroupApply.getGroupId().equals(groupApply.getGroupId())) {
                return false;
            }
        }
        mGroupApplys.add(groupApply);
        return true;
    }

    public boolean saveGroupApply() {
        try {
            mSerializer.save(mGroupApplys);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving users: ", e);
            return false;
        }
    }

    public boolean deleteGroupApply(GroupApply user) {
        GroupApply temp = findGroupApply(user);
        if (temp == null) {
            Log.d(TAG, "deleteGroupApply failure: not exist");
            return false;
        }
        mGroupApplys.remove(user);
        return true;
    }

}
