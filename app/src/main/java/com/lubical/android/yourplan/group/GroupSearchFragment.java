package com.lubical.android.yourplan.group;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lubical.android.yourplan.DBManager;
import com.lubical.android.yourplan.R;

import java.util.List;

/**
 * Created by lubical on 2016/11/22.
 */

public class GroupSearchFragment extends ListFragment {
    private static final String TAG = "groupsearchfra";
    public static final String USER_ID = "groupsf.userId";
    private EditText groupNameOrId;
    private Button searchBtn;
    private Button applyBtn;
    private ListView mListView;
    private List<Group> mGroups;
    private String userId;
    private DBManager mDBManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (NavUtils.getParentActivityName(getActivity()) != null ) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mDBManager = new DBManager(getActivity());
        mGroups = mDBManager.getGroups();
        GroupAdapter adapter = new GroupAdapter(mGroups);
        userId = getArguments().getString(USER_ID);
        setListAdapter(adapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group_search,null);
        groupNameOrId = (EditText)v.findViewById(R.id.fragment_group_search_nameEt);
        searchBtn = (Button)v.findViewById(R.id.fragment_group_search_searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupsearch = groupNameOrId.getText().toString().trim();
                mGroups = mDBManager.getGroups(groupsearch);
                GroupAdapter adapter = new GroupAdapter(mGroups);
                setListAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        return v;
    }

    public static GroupSearchFragment newInstance(String userId) {
        Bundle args = new Bundle();
        args.putSerializable(USER_ID, userId);
        GroupSearchFragment fragment = new GroupSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private class GroupAdapter extends ArrayAdapter<Group> {
        public GroupAdapter(List<Group> groups) {
            super(getActivity(), 0, groups);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_group_search, null);
            }
            Group mGroup = getItem(position);
            TextView titleTextView = (TextView)convertView.findViewById(R.id.list_item_group_id);
            titleTextView.setText(mGroup.getGroupName());
            TextView numberTextView = (TextView)convertView.findViewById(R.id.list_item_group_number);
            numberTextView.setText(Integer.toString(mGroup.getGroupMemberCount()));

            return convertView;
        }
    }

    @Override
    public void onListItemClick(ListView l,View v,int position, long id) {

        Group group = ((GroupAdapter)getListAdapter()).getItem(position);
        GroupApply groupApply = new GroupApply(group.getGroupId(),userId);
        Boolean addresult=mDBManager.addGroupApply(groupApply);
        if (addresult) {
            Toast.makeText(getActivity(), "申请成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "已申请，请耐心等待", Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, "group apply success"+group.getGroupId()+userId);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {

                    NavUtils.navigateUpFromSameTask(getActivity());
                    return true;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDBManager.closeDB();
    }
}
