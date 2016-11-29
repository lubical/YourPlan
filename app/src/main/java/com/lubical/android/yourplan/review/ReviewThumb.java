package com.lubical.android.yourplan.review;

import java.util.UUID;

/**
 * Created by lubical on 2016/11/28.
 */

public class ReviewThumb {
    public static final String REVIEWTHUMB_USERID = "userId";
    public static final String REVIEWTHUMB_SHAREID = "shareId";
    private String userId;
    private UUID shareId;

    public ReviewThumb(String userId, UUID shareId) {
        this.userId = userId;
        this.shareId = shareId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UUID getShareId() {
        return shareId;
    }

    public void setShareId(UUID shareId) {
        this.shareId = shareId;
    }
}
