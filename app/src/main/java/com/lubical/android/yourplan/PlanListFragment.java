package com.lubical.android.yourplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lubical on 2016/11/10.
 */

public class PlanListFragment extends ListFragment {
    private ArrayList<Plan> mPlen;
    public static final String PLAN_IMPORTANT_URGENT = "yourplan.planlistfrgment.plan_important_urgent";
    private static final String TAG = "PlanListFragment";
    private static final int REQUEST_PLAN = 1;
    private int plan_IU;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plan_IU = (int)getArguments().getSerializable(PLAN_IMPORTANT_URGENT);
        mPlen = PlanLab.get(getActivity()).getPlans(plan_IU);

        PlanAdapter adapter = new PlanAdapter(mPlen);
        setListAdapter(adapter);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_plan, viewGroup, false);
//操作栏在此添加操作


        return v;
    }
    public static PlanListFragment newInstance(int plan_important_urgent) {
        Bundle args = new Bundle();
        args.putSerializable(PLAN_IMPORTANT_URGENT, plan_important_urgent);
        PlanListFragment fragment = new PlanListFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private class PlanAdapter extends ArrayAdapter<Plan> {
        public PlanAdapter(ArrayList<Plan> plans) {
            super(getActivity(), 0, plans);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_plan, null);
            }
            Plan mPlan = getItem(position);
            TextView titleTextView = (TextView)convertView.findViewById(R.id.plan_list_item_titleTextView);
            titleTextView.setText(mPlan.getPlanName());

            return convertView;
       }
    }

    @Override
    public  void onListItemClick(ListView l,View v, int position, long id) {
        Plan plan = ((PlanAdapter)getListAdapter()).getItem(position);
        Log.d(TAG, plan.getPlanName() + " was clicked");
        Intent i = new Intent(getActivity(), PlanListPagerActivity.class);
        i.putExtra(PlanFragment.EXTRA_PLAN_ID, plan.getPlanID());
        startActivityForResult(i, REQUEST_PLAN);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((PlanAdapter)getListAdapter()).notifyDataSetChanged();
    }

}
