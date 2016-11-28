package com.lubical.android.yourplan.review;

import java.util.UUID;

/**
 * Created by lubical on 2016/11/28.
 */

public class Review {
    public static final String REVIEW_REVIEWID = "reviewId";
    public static final String REVIEW_SHAREID = "shareId";
    public static final String REVIEW_USERID = "userId";
    public static final String REVIEW_ISTHUMPUP = "isThumbUp";
    public static final String REVIEW_COMMENT = "comment";
    public static final String REVIEW_TIME = "time";
    private UUID reviewId;
    private UUID shareId;
    private String userId;
    private int isThumbUp;
    private String comment;
    private long time;

    public UUID getReviewId() {
        return reviewId;
    }

    public void setReviewId(UUID reviewId) {
        this.reviewId = reviewId;
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

    public int getIsThumbUp() {
        return isThumbUp;
    }

    public void setIsThumbUp(int isThumbUp) {
        this.isThumbUp = isThumbUp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
