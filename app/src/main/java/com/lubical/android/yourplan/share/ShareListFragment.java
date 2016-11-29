package com.lubical.android.yourplan.share;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.lubical.android.yourplan.DBManager;
import com.lubical.android.yourplan.LoginFragment;
import com.lubical.android.yourplan.R;
import com.lubical.android.yourplan.account.Account;
import com.lubical.android.yourplan.group.GroupApply;
import com.lubical.android.yourplan.plan.Plan;
import com.lubical.android.yourplan.review.Review;
import com.lubical.android.yourplan.review.ReviewThumb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static android.R.attr.fingerprintAuthDrawable;
import static android.R.attr.name;
import static android.R.attr.text;
import static android.content.ContentValues.TAG;

/**
 * Created by lubical on 2016/11/28.
 */

public class ShareListFragment extends ListFragment{
    public static final String GROUP_ID = "shareListGroupId";
    public static final String USER_ID = "shareListUserId";
    private static final String TAG = "shareListFragment";
    private UUID groupId;
    private String userId;
    private DBManager mDBManager;
    private List<HashMap<String, Object>> mShareMapList;
    private ShareSimpleAdapter mSimpleAdapter;
    private String text[] = new String[100];
    private int index;
    private myWatcher mWatcher;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = LoginFragment.loginAccount;
        mDBManager = new DBManager(getActivity());
        Account account = mDBManager.getAccount(userId);
        groupId = account.getGroupId();
        Log.d(TAG, " gourpId="+groupId);
        mShareMapList = mDBManager.getShareMapList(groupId);
        mSimpleAdapter = new ShareSimpleAdapter(
                getActivity(),
                mShareMapList,
                R.layout.list_item_share,
                new String[]{"planName","statue","name","thumbUpCount","commentCount","message"},
                new int[]{R.id.list_item_share_planNameTv,R.id.list_item_share_planStatueTv,
                        R.id.list_item_share_planUserTv,R.id.list_item_share_thumbUpCountTv,
                        R.id.list_item_share_commentCountTv, R.id.list_item_share_messageTv}
        );
        setListAdapter(mSimpleAdapter);
        Log.d(TAG, " size "+mShareMapList.size());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_share, null);
        return v;
    }

    private class ShareSimpleAdapter extends SimpleAdapter {

        public ShareSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);

            final HashMap<String, Object> map = mShareMapList.get(position);
            final UUID shareId= UUID.fromString(map.get(Share.SHARE_SHAREID).toString());
            final String userId = map.get(Account.ACCOUNT_USER_ID).toString();
            final ImageButton thumbUpBtn = (ImageButton) v.findViewById(R.id.list_item_share_thumbUpBtn);
            final int[] flag = {0};
            final ReviewThumb reviewThumb = new ReviewThumb(userId,shareId);
            boolean isExistThumbUp = mDBManager.isExistThumb(reviewThumb);
            final Share share = mDBManager.getShare(shareId);
            if (isExistThumbUp) {
                flag[0] = 1;
                thumbUpBtn.setSelected(true);
            } else {
                flag[0] = 0;
                thumbUpBtn.setSelected(false);
            }
            thumbUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag[0] == 0) {
                        flag[0] =1;
                        thumbUpBtn.setSelected(true);
                        mDBManager.addReviewThumb(reviewThumb);
                        share.setThumbUpCount(share.getThumbUpCount()+1);
                        Toast.makeText(getActivity(),"赞+1",Toast.LENGTH_SHORT).show();
                        mDBManager.updateShare(share);
                        mShareMapList.clear();
                        mShareMapList.addAll(mDBManager.getShareMapList(groupId));
                        mSimpleAdapter.notifyDataSetChanged();
                    }else {
                        flag[0] = 0;
                        mDBManager.deleteReviewThumb(reviewThumb);
                        thumbUpBtn.setSelected(false);
                        share.setThumbUpCount(share.getThumbUpCount()-1);
                        mDBManager.updateShare(share);
                        mShareMapList.clear();
                        mShareMapList.addAll(mDBManager.getShareMapList(groupId));
                        Toast.makeText(getActivity(),"取消赞",Toast.LENGTH_SHORT).show();
                        mSimpleAdapter.notifyDataSetChanged();
                    }
                }
            });
            final RelativeLayout showComment = (RelativeLayout)v.findViewById(R.id.list_item_share_showCommentRL);
            ImageButton commentBtn = (ImageButton)v.findViewById(R.id.list_item_share_commentBtn);
            commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // showComment.setVisibility(View.VISIBLE);
                }
            });
            final EditText commentET = (EditText)v.findViewById(R.id.list_item_share_commentET);
            commentET.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        index = position;
                    }
                    return false;
                }
            });
            commentET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    EditText et = (EditText)v;
                    if (mWatcher == null) {
                        mWatcher = new myWatcher();
                    }
                    if (hasFocus) {
                        et.addTextChangedListener(mWatcher);
                    } else  {
                        et.removeTextChangedListener(mWatcher);
                    }
                }
            });
            commentET.clearFocus();
            if (index!=-1 && index == position) {
                commentET.requestFocus();
            }
            commentET.setText(text[position]);
            commentET.setSelection(commentET.getText().length());
            Button commentOkBtn = (Button)v.findViewById(R.id.list_item_share_commentOkBtn);
            ListView listView = (ListView)v.findViewById(R.id.list_item_share_listView);
            List<HashMap<String, Object>> reviewMapList = mDBManager.getReviewMapList(shareId);
            final SimpleAdapter reviewAdapter = new SimpleAdapter(
                    getActivity(),
                    reviewMapList,
                    R.layout.list_item_review,
                    new String[]{"name","comment"},
                    new int[]{R.id.list_item_review_user, R.id.list_item_review_comment}
            );
            listView.setAdapter(reviewAdapter);
            setListViewHeight(listView);
            commentOkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String comment = commentET.getText().toString().trim();
                    if (comment.length()>0) {
                        Review review = new Review(shareId,userId,comment);
                        mDBManager.addReview(review);
                        share.setCommentCount(share.getCommentCount()+1);
                        mDBManager.updateShare(share);
                        text[position]="";
                        commentET.setText("");
                        mShareMapList.clear();
                        mShareMapList.addAll(mDBManager.getShareMapList(groupId));
                        reviewAdapter.notifyDataSetChanged();
                        mSimpleAdapter.notifyDataSetChanged();
                    }
                }
            });
            return v;
        }
    }
    class myWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            text[index] = s.toString();
        }
    }
    public void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i=0;i<listAdapter.getCount();i++) {
            View listItem = listAdapter.getView(i,null,listView);
            listItem.measure(0,0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight()*(listAdapter.getCount()-1));
        listView.setLayoutParams(params);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mDBManager.closeDB();
    }
}
