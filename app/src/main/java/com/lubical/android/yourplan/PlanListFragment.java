package com.lubical.android.yourplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private static final String PLAN_USER = "planlistfragment.planuser";
    private static final int REQUEST_PLAN = 1;
    private int plan_IU;
    private String plan_user;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plan_IU = (int)getArguments().getSerializable(PLAN_IMPORTANT_URGENT);
        plan_user =(String) getArguments().getSerializable(PLAN_USER);
        mPlen = PlanLab.get(getActivity()).getPlans(plan_IU);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        PlanAdapter adapter = new PlanAdapter(mPlen);
        setListAdapter(adapter);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_plan, viewGroup, false);
//操作栏在此添加操作


        return v;
    }
    public static PlanListFragment newInstance(int plan_important_urgent, String planUser) {
        Bundle args = new Bundle();
        args.putSerializable(PLAN_IMPORTANT_URGENT, plan_important_urgent);
        args.putSerializable(PLAN_USER, planUser);
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
        Intent i = new Intent(getActivity(), PlanActivity.class);
        i.putExtra(PlanFragment.EXTRA_PLAN_ID, plan.getPlanID());
        startActivityForResult(i, REQUEST_PLAN);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((PlanAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                    return true;
                }
            case R.id.menu_item_new_plan:
                Plan plan = new Plan(plan_user);
                plan.setPlanImportantUrgent(plan_IU);
                PlanLab.get(getActivity()).addPlan(plan);
                Intent i = new Intent(getActivity(), PlanActivity.class);
                i.putExtra(PlanFragment.EXTRA_PLAN_ID, plan.getPlanID());
                startActivityForResult(i, REQUEST_PLAN);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_plan_list, menu);
    }
}
