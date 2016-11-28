package com.lubical.android.yourplan.group;

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
    public static final String GROUP_OWNER_ID = "groupOwnerId";
    public static final String GROUP_ID = "groupId";
    public static final String GROUP_NAME = "groupName";
    public static final String GROUP_PLAN_ID = "groupPlanId";
    public static final String GROUP_MEMBER_COUNT = "groupMemberCount";
    public static final String GROUP_PLAN_REWARD_TIMES = "groupPlanRewardTimes";
    private String groupOwnerId;
    private UUID groupId;
    private String groupName;
    private UUID groupPlanId;
    private int groupMemberCount;
    private int groupPlanRewardTimes;


    public Group(){groupId = UUID.randomUUID();}
    public Group(String groupOwnerId) {
        //groupId 应进行检验
        this.groupOwnerId = groupOwnerId;
        groupId = UUID.randomUUID();
        groupName = "group";
        groupMemberCount = 1;
        groupPlanRewardTimes = 0;
        groupPlanId = UUID.randomUUID();
    }

    public int getGroupPlanRewardTimes() {
        return groupPlanRewardTimes;
    }

    public void setGroupPlanRewardTimes(int groupPlanRewardTimes) {
        this.groupPlanRewardTimes = groupPlanRewardTimes;
    }

    public String getGroupOwnerId() {
        return groupOwnerId;
    }

    public void setGroupOwnerId(String groupOwnerId) {
        this.groupOwnerId = groupOwnerId;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {this.groupId = UUID.fromString(groupId);}
    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public UUID getGroupPlanId() {
        return groupPlanId;
    }

    public int getGroupMemberCount() {
        return groupMemberCount;
    }

    public void setGroupMemberCount(int groupMemberCount) {
        this.groupMemberCount = groupMemberCount;
    }

    public void setGroupPlanId(UUID groupPlanId) {
        this.groupPlanId = groupPlanId;
    }
}
