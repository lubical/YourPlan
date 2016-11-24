package com.lubical.android.yourplan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.fragment;

/**
 * Created by lubical on 2016/11/21.
 */

public class NavigationDrawerActivity extends FragmentActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private static final String TAG = "NavigationDrawerAct";
    private String[] listViewString = {"计划管理","个人信息","群组管理","退出登陆"};
    private List<Fragment> mFragmentList;
    private final FragmentManager mManager = getSupportFragmentManager();
    private FragmentTransaction mFragmentTransaction;
    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_drawer);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listViewString));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                android.R.drawable.ic_menu_edit,
                R.string.drawer_open,
                R.string.drawer_close){
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        initFragment();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }
    private void initFragment() {
        mFragmentList = new ArrayList<Fragment>();
        FragmentTransaction ft = mManager.beginTransaction();
        PlanListPagerFragment fragment = new PlanListPagerFragment();
        mFragmentList.add(fragment);
        ft.add(R.id.content_frame, fragment);
        ft.commit();

        UserDetalFragment fragment1 = new UserDetalFragment();
        FragmentTransaction ft1 = mManager.beginTransaction();
        mFragmentList.add(fragment1);
        ft.add(R.id.content_frame,fragment1);
        ft1.commit();
        ft1.hide(fragment1);

        GroupFragment fragment2 = new GroupFragment();
        FragmentTransaction ft2 = mManager.beginTransaction();
        mFragmentList.add(fragment2);
        ft2.add(R.id.content_frame,fragment2);
        ft2.commit();
        ft2.hide(fragment2);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        Log.d(TAG, "show"+position);
        if (position == 3) {
            Log.d(TAG, "tuichu");
            PlanLab.get(getApplication()).savePlan();
            AccountLab.get(getApplication()).saveAccount();
            GroupLab.get(getApplication()).saveGroup();
            finish();

        }
        for (int i=0;i<mFragmentList.size();i++) {
            if (i != position) {
                mFragmentTransaction.hide(mFragmentList.get(i));
            }else {
               mFragmentTransaction.show(mFragmentList.get(i));
            }
        }
        mFragmentTransaction.commit();
    }


}
