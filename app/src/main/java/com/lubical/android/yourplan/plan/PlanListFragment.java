package com.lubical.android.yourplan.plan;

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
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.lubical.android.yourplan.DBManager;
import com.lubical.android.yourplan.R;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by lubical on 2016/11/10.
 */

public class PlanListFragment extends ListFragment {
    public static final String PLAN_IMPORTANT_URGENT = "yourplan.planlistfrgment.plan_important_urgent";
    private static final String TAG = "PlanListFragment";
    private static final String PLAN_USER = "planlistfragment.planuser";
    private static final int REQUEST_PLAN = 1;
    private int plan_IU;
    private String plan_user;
    private DBManager mDBManager;
    private List<HashMap<String,Object>> mArrayListData;
    private SimpleAdapter planAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (NavUtils.getParentActivityName(getActivity()) != null ) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mDBManager = new DBManager(getActivity());
        plan_IU = (int)getArguments().getSerializable(PLAN_IMPORTANT_URGENT);
        plan_user =(String) getArguments().getSerializable(PLAN_USER);

        mArrayListData = mDBManager.getUserPlanMapList(plan_user, plan_IU);
        planAdapter = new SimpleAdapter(
                getActivity(),
                mArrayListData,
                R.layout.list_item_plan,
                new String[] {"planName"},
                new int[]{R.id.plan_list_item_titleTextView});

        setListAdapter(planAdapter);

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
                            for (int i=mArrayListData.size()-1; i>=0; i--) {
                                if (getListView().isItemChecked(i)) {
                                    HashMap<String, Object> map = mArrayListData.get(i);
                                    mDBManager.deletePlan(UUID.fromString(map.get(Plan.PLAN_ID).toString()));
                                    mArrayListData.remove(i);
                                }
                            }
                            mode.finish();
                            planAdapter.notifyDataSetChanged();
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

        HashMap<String, Object> map = mArrayListData.get(position);
        switch (item.getItemId()) {
            case R.id.menu_item_delete_plan:
                mDBManager.deletePlan(UUID.fromString(map.get(Plan.PLAN_ID).toString()));
                planAdapter.notifyDataSetChanged();
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


    @Override
    public  void onListItemClick(ListView l,View v, int position, long id) {
        HashMap<String, Object> map = mArrayListData.get(position);
        Log.d(TAG, "clicked "+map.get("planId").toString());

        Intent i = new Intent(getActivity(), PlanActivity.class);
        i.putExtra(PlanFragment.EXTRA_PLAN_ID, map.get("planId").toString());
        startActivityForResult(i, REQUEST_PLAN);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume"+plan_user+plan_IU);
        planAdapter.notifyDataSetChanged();
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
                mDBManager.addPlan(plan);
                Log.d(TAG, "addPlan"+plan.getPlanID());
                Intent i = new Intent(getActivity(), PlanActivity.class);
                i.putExtra(PlanFragment.EXTRA_PLAN_ID, plan.getPlanID().toString());
                startActivityForResult(i, REQUEST_PLAN);
                planAdapter.notifyDataSetChanged();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_plan_list, menu);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        mDBManager.closeDB();
    }
}
