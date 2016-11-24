package com.lubical.android.yourplan;

import org.json.JSONException;
import org.json.JSONObject;
import static android.R.attr.height;
import static android.R.attr.name;

/**
 * Created by lubical on 2016/11/22.
 */

public class GroupApply {
    private static final String GROUPID ="groupId";
    private static final String APPLYER_ID = "applyerId";
    private String groupId;
    private String applyerId;
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(GROUPID, groupId);
        json.put(APPLYER_ID, applyerId);

        return json;
    }
    public GroupApply(JSONObject json) throws JSONException {
        applyerId = json.getString(APPLYER_ID);
        groupId = json.getString(GROUPID);

    }

    public GroupApply(String groupId,String applyerId) {
        this.groupId = groupId;
        this.applyerId = applyerId;
    }
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getApplyerId() {
        return applyerId;
    }

    public void setApplyerId(String applyerId) {
        this.applyerId = applyerId;
    }
}
