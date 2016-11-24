package com.lubical.android.yourplan;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lubical on 2016/11/22.
 */

public class GroupLab {
    private static final String TAG = "GroupLab";
    private static final String FILENAME = "group.json";
    private GroupIntentJSONSerializer mSerializer;
    private static GroupLab sGroupLab;
    private Context mContext;
    private ArrayList<Group>mGroups;
    private String mGroupOwnerId;
    private void reload(Context context) {
        mContext = context;
        mSerializer = new GroupIntentJSONSerializer(mContext, FILENAME);
        try {
            mGroups = mSerializer.load();
        }catch (Exception e) {
            mGroups = new ArrayList<Group>();
            Log.e(TAG, "Error loading groups:", e);
        }
    }
    private GroupLab(Context context) {
      reload(context);
    }

    public static GroupLab get(Context context) {
        if (sGroupLab == null) {
            sGroupLab = new GroupLab(context.getApplicationContext());
        }
        return sGroupLab;
    }

    public ArrayList<Group> getGroups() {
        return mGroups;
    }
    public ArrayList<Group> getGroups(String nameOrId) {
        ArrayList<Group>arrayList = new ArrayList<Group>();
        for (Group group:mGroups) {
            if (group.getGroupId().equals(nameOrId) || group.getGroupName().contains(nameOrId)) {
                arrayList.add(group);
            }
        }
        return arrayList;
    }

    public Group getGroup(String groupId) {
        for (Group group:mGroups) {
            if (group.getGroupId().equals(groupId)) {
                return group;
            }
        }
        return null;
    }

    public boolean addGroup(Group group) {
        if (getGroup(group.getGroupId()) != null) {
            return false;
        }
        mGroups.add(group);
        return true;
    }

    public boolean saveGroup() {
        try {
            mSerializer.save(mGroups);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving groups: ", e);
            return false;
        }
    }

    public boolean deleteGroup(Group group) {
        Group temp = getGroup(group.getGroupId());
        if (temp == null) {
            Log.d(TAG, "deleteGroup failure: not exist");
            return false;
        }
        mGroups.remove(group);
        return true;
    }

    public void setGroupOwnerId(String groupOwnerId) {
        mGroupOwnerId = groupOwnerId;
    }

    public String getmGroupOwnerId() {
        return mGroupOwnerId;
    }
}

