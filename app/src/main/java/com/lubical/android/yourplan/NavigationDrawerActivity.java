package com.lubical.android.yourplan;

import android.support.v4.app.FragmentActivity;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lubical.android.yourplan.user.UserDetalFragment;
import com.lubical.android.yourplan.group.GroupFragment;
import com.lubical.android.yourplan.plan.PlanListPagerFragment;

/**
 * Created by lubical on 2016/11/21.
 */

public class NavigationDrawerActivity extends FragmentActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private static final String TAG = "NavigationDrawerAct";
    private String[] listViewString = {"计划管理","个人信息","群组管理","退出登陆"};
    private Fragment planListPagerFragment,userDetalFragment,groupFragment;
    private final FragmentManager mManager = getSupportFragmentManager();
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
        if (savedInstanceState == null) {
            selectItem(0);
        }
        mDrawerLayout.setDrawerListener(mDrawerToggle);

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
        Log.d(TAG, "show"+position);
        FragmentTransaction ft = mManager.beginTransaction();
        hideFragment(ft);
        switch (position) {
            case 0:
                if (planListPagerFragment == null) {
                    planListPagerFragment = new PlanListPagerFragment();
                    ft.add(R.id.content_frame, planListPagerFragment);
                    ft.show(planListPagerFragment);
                } else {
                    ft.show(planListPagerFragment);
                }
                break;
            case 1:
                if (userDetalFragment == null) {
                    userDetalFragment = new UserDetalFragment();
                    ft.add(R.id.content_frame,userDetalFragment);
                    ft.show(userDetalFragment);
                } else {
                    ft.show(userDetalFragment);
                }
                break;
            case 2:
                if (groupFragment == null) {
                    groupFragment = new GroupFragment();
                    ft.add(R.id.content_frame,groupFragment);
                    ft.show(groupFragment);
                } else {
                    ft.show(groupFragment);
                }
                break;
            case 3:
                Log.d(TAG, "退出");
                finish();
        }

        ft.commit();
    }
    public void hideFragment(FragmentTransaction ft) {
        if (planListPagerFragment != null)
            ft.hide(planListPagerFragment);
        if (userDetalFragment != null)
            ft.hide(userDetalFragment);
        if (groupFragment != null)
            ft.hide(groupFragment);
        //ft.commit();
    }


}
