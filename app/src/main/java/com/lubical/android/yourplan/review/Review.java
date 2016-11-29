package com.lubical.android.yourplan.review;

import java.util.Date;
import java.util.UUID;

/**
 * Created by lubical on 2016/11/28.
 */

public class Review {
    public static final String REVIEW_REVIEWID = "reviewId";
    public static final String REVIEW_SHAREID = "shareId";
    public static final String REVIEW_USERID = "userId";
    public static final String REVIEW_COMMENT = "comment";
    public static final String REVIEW_TIME = "time";
    private UUID reviewId;
    private UUID shareId;
    private String userId;
    private String comment;
    private long time;
    public Review(){}

    public Review(UUID shareId, String userId, String comment) {
        this.reviewId = UUID.randomUUID();
        this.shareId = shareId;
        this.userId = userId;
        this.comment = comment;
        this.time = new Date().getTime();
    }

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
