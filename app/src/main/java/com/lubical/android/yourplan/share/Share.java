package com.lubical.android.yourplan.share;

import java.util.Date;
import java.util.UUID;

/**
 * Created by lubical on 2016/11/28.
 */

public class Share {
    public static final String SHARE_SHAREID = "shareId";
    public static final String SHARE_USERID = "userId";
    public static final String SHARE_PLANID = "planId";
    public static final String SHARE_MESSAGE = "message";
    public static final String SHARE_GROUPID = "groupId";
    public static final String SHARE_TIME = "time";
    public static final String SHARE_THUMBUPCOUNT = "thumbUpCount";
    public static final String SHARE_COMMENTCOUNT = "commentCount";
    private UUID shareId;
    private String userId;
    private UUID planId;
    private String message;
    private UUID groupId;
    private long time;
    private int thumbUpCount;
    private int commentCount;
    public Share(){}
    public Share(UUID planId,UUID groupId,String userId, String message) {
        shareId = UUID.randomUUID();
        this.groupId = groupId;
        this.planId = planId;
        this.userId = userId;
        if (message.length()<1) {
            message = "分享是最大的收获(默认)";
        }
        this.message = message;
        time = new Date().getTime();
        thumbUpCount = 0;
        commentCount = 0;
    }

    public int getThumbUpCount() {
        return thumbUpCount;
    }

    public void setThumbUpCount(int thumbUpCount) {
        this.thumbUpCount = thumbUpCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public UUID getShareId() {
        return shareId;
    }

    public void setShareId(UUID shareId) {
        this.shareId = shareId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UUID getPlanId() {
        return planId;
    }

    public void setPlanId(UUID planId) {
        this.planId = planId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
