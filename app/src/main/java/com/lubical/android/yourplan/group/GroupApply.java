package com.lubical.android.yourplan.group;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import static android.R.attr.height;
import static android.R.attr.name;

/**
 * Created by lubical on 2016/11/22.
 */

public class GroupApply {
    public static final String GROUPID ="groupId";
    public static final String USER_ID = "userId";
    private UUID groupId;
    private String userId;
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(GROUPID, groupId.toString());
        json.put(USER_ID, userId);

        return json;
    }
    public GroupApply(JSONObject json) throws JSONException {
        userId = json.getString(USER_ID);
        groupId = UUID.fromString(json.getString(GROUPID));

    }
    public GroupApply(){}
    public GroupApply(UUID groupId,String userId) {
        this.groupId = groupId;
        this.userId = userId;
    }
    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
