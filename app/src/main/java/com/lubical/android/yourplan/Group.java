package com.lubical.android.yourplan;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import static android.R.attr.height;
import static android.R.attr.name;

/**
 * Created by lubical on 2016/11/22.
 * 暂时一个群只能有一个计划
 * 通过groupid搜索群内用户
 * plan属性为public即为群计划。
 */

public class Group {
    private static final String GROUP_OWNER_ID = "groupOwnerId";
    private static final String GROUP_ID = "groupId";
    private static final String GROUP_NAME = "graouName";
    private static final String GROUP_PLAN_ID = "groupPlanId";
    private static final String GROUP_MEMBER_COUNT = "groupMemberCount";
    private String groupOwnerId;
    private String groupId;
    private String groupName;
    private String groupPlanId;
    private int groupMemberCount;

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(GROUP_OWNER_ID, groupOwnerId);
        json.put(GROUP_ID, groupId);
        json.put(GROUP_NAME, groupName);
        json.put(GROUP_PLAN_ID, groupPlanId);
        json.put(GROUP_MEMBER_COUNT, groupMemberCount);
        return json;
    }
    public Group(String groupOwnerId) {
        //groupId 应进行检验
        this.groupOwnerId = groupOwnerId;
        groupId = UUID.randomUUID().toString();
        groupName = "group";
        groupMemberCount = 1;
    }
    public Group(JSONObject json) throws JSONException {
        groupOwnerId = json.getString(GROUP_OWNER_ID);
        groupId = json.getString(GROUP_ID);
        groupName = json.getString(GROUP_NAME);
        groupPlanId = json.getString(GROUP_PLAN_ID);
        groupMemberCount = json.getInt(GROUP_MEMBER_COUNT);
    }
    public String getGroupOwnerId() {
        return groupOwnerId;
    }

    public void setGroupOwnerId(String groupOwnerId) {
        this.groupOwnerId = groupOwnerId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupPlanId() {
        return groupPlanId;
    }

    public int getGroupMemberCount() {
        return groupMemberCount;
    }

    public void setGroupMemberCount(int groupMemberCount) {
        this.groupMemberCount = groupMemberCount;
    }

    public void setGroupPlanId(String groupPlanId) {
        this.groupPlanId = groupPlanId;
    }
}
