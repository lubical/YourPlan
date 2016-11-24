package com.lubical.android.yourplan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by lubical on 2016/11/22.
 */

public class GroupApplyDealFragment extends ListFragment {
    public static final String GRUOP_ID = "groupId";
    private ArrayList<GroupApply> mGroupApplies;
    private String getGruopId;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getGruopId = (String)getArguments().getSerializable(GRUOP_ID);
        mGroupApplies = GroupApplyLab.get(getActivity()).getGroupApply(getGruopId);
        GroupApplyAdapter applyAdapter = new GroupApplyAdapter(mGroupApplies);
        setListAdapter(applyAdapter);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_group_apply_deal, null);
        return v;
    }
    
    public static GroupApplyDealFragment newInstance(String gruopId) {
        Bundle args = new Bundle();
        args.putSerializable(GRUOP_ID, gruopId);
        GroupApplyDealFragment fragment = new GroupApplyDealFragment();
        fragment.setArguments(args);
        return fragment;
        
    }
    private class GroupApplyAdapter extends ArrayAdapter<GroupApply> {
        public GroupApplyAdapter(ArrayList<GroupApply> groupApplies) {
            super(getActivity(), 0, groupApplies);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_apply_group, null);
            }
            final GroupApply mGroupApply = getItem(position);
            TextView userTextView = (TextView)convertView.findViewById(R.id.list_item_apply_group_applyerId);
            userTextView.setText(mGroupApply.getApplyerId());
            Button agree = (Button)convertView.findViewById(R.id.list_item_apply_group_agreeButton);
            agree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Account account = AccountLab.get(getActivity()).getAccount(mGroupApply.getApplyerId());
                    account.setGroupId(mGroupApply.getGroupId());
                    account.setGroupTaskState(0);
                    AccountLab.get(getActivity()).saveAccount();
                    GroupApplyLab.get(getActivity()).deleteGroupApply(mGroupApply);
                    GroupApplyLab.get(getActivity()).saveGroupApply();
                    GroupApplyAdapter applyAdapter = (GroupApplyAdapter)getListAdapter();
                    Toast.makeText(getActivity(),"加入成功",Toast.LENGTH_SHORT).show();
                    applyAdapter.notifyDataSetChanged();
                }
            });
            Button disagree = (Button)convertView.findViewById(R.id.list_item_apply_group_disagreeButton);
            disagree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GroupApplyLab.get(getActivity()).deleteGroupApply(mGroupApply);
                    GroupApplyLab.get(getActivity()).saveGroupApply();
                    GroupApplyAdapter applyAdapter = (GroupApplyAdapter)getListAdapter();
                    Toast.makeText(getActivity(),"拒绝成功",Toast.LENGTH_SHORT).show();
                    applyAdapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        ((GroupApplyAdapter)getListAdapter()).notifyDataSetChanged();
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
}
