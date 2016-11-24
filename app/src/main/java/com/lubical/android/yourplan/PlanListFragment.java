package com.lubical.android.yourplan;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
        mPlen = PlanLab.get(getActivity()).getPlans(plan_user,plan_IU);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        PlanAdapter adapter = new PlanAdapter(mPlen);
        setListAdapter(adapter);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_plan, viewGroup, false);
        ListView listView = (ListView)v.findViewById(android.R.id.list);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            registerForContextMenu(listView);
        } else {
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater1 = mode.getMenuInflater();
                    inflater1.inflate(R.menu.plan_list_item_context, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_item_delete_plan:
                            PlanAdapter adapter = (PlanAdapter)getListAdapter();
                            PlanLab planLab = PlanLab.get(getActivity());
                            for (int i=adapter.getCount()-1; i>=0; i--) {
                                if (getListView().isItemChecked(i)) {
                                    planLab.deletePlan(adapter.getItem(i));
                                }
                            }
                            mode.finish();
                            adapter.notifyDataSetChanged();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }
        return v;
    }
    /*
     * 创建上下文菜单
    */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.plan_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = info.position;
        PlanAdapter adapter = (PlanAdapter)getListAdapter();
        Plan plan = adapter.getItem(position);
        switch (item.getItemId()) {
            case R.id.menu_item_delete_plan:
                PlanLab.get(getActivity()).deletePlan(plan);
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
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
