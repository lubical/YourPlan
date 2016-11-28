package com.lubical.android.yourplan.group;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.lubical.android.yourplan.DBManager;
import com.lubical.android.yourplan.R;
import com.lubical.android.yourplan.account.Account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by lubical on 2016/11/22.
 */

public class GroupApplyDealFragment extends ListFragment {
    public static final String GRUOP_ID = "groupId";
    private List<HashMap<String,Object>> mGroupApplyMapList;
    private String getGruopId;
    private DBManager mDBManager;
    private GroupApplySimpleAdapter mGroupApplySimpleAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDBManager = new DBManager(getActivity());
        getGruopId = getArguments().getString(GRUOP_ID);
        Log.d("GADeal", getGruopId);
        mGroupApplyMapList = mDBManager.getGroupApplyMapList(UUID.fromString(getGruopId));
        Log.d("GADeal", "size "+mGroupApplyMapList.size());
        mGroupApplySimpleAdapter = new GroupApplySimpleAdapter(
                getActivity(),
                mGroupApplyMapList,
                R.layout.list_item_apply_group,
                new String[]{"userId"},
                new int[]{R.id.list_item_apply_group_applyerId}
        );
        setListAdapter(mGroupApplySimpleAdapter);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_group_apply_deal, null);
        return v;
    }
    
    public static GroupApplyDealFragment newInstance(String groupId) {
        Bundle args = new Bundle();
        args.putSerializable(GRUOP_ID, groupId);
        GroupApplyDealFragment fragment = new GroupApplyDealFragment();
        fragment.setArguments(args);
        return fragment;
        
    }
    private class GroupApplySimpleAdapter extends SimpleAdapter {

        public GroupApplySimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);

            HashMap<String, Object> map = mGroupApplyMapList.get(position);
            final String userId = map.get(GroupApply.USER_ID).toString();
            final UUID groupId = UUID.fromString(map.get(GroupApply.GROUPID).toString());
           /* TextView userTextView = (TextView)convertView.findViewById(list_item_apply_group_applyerId);
            userTextView.setText(map.get(GroupApply.USER_ID).toString());*/
            Button agree = (Button)v.findViewById(R.id.list_item_apply_group_agreeButton);
            agree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Account account = mDBManager.getAccount(userId);
                    account.setGroupId(groupId);
                    account.setGroupTaskState(0);
                    mDBManager.updateAccount(account);
                    mDBManager.deleteGroupApply(userId, groupId);
                    mGroupApplyMapList.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(getActivity(),"加入成功",Toast.LENGTH_SHORT).show();

                }
            });
            Button disagree = (Button)v.findViewById(R.id.list_item_apply_group_disagreeButton);
            disagree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDBManager.deleteGroupApply(userId, groupId);
                    mGroupApplyMapList.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(getActivity(),"拒绝成功",Toast.LENGTH_SHORT).show();
                }
            });
            return v;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDBManager.closeDB();
    }
}
