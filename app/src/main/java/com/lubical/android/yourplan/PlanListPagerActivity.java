package com.lubical.android.yourplan;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.N;

/**
 * Created by lubical on 2016/11/11.
 */

public class PlanListPagerActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonIU;
    private RadioButton mRadioButtonNIU;
    private RadioButton mRadioButtonINU;
    private RadioButton mRadioButtonNINU;
    private List<PlanListFragment> mPlanListFragments = new ArrayList<PlanListFragment>();
    private String mUserId;
    private static final String USER_ID = "userId";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_pager_list);
       // getActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState ==null) {
            mUserId = getIntent().getStringExtra(LoginFragment.EXTRA_USER_ID);
        } else {
            mUserId = savedInstanceState.getString(USER_ID);
        }
        mUserId = PlanLab.getmUserId();
        Toast.makeText(getApplicationContext(), mUserId +"lalalalala", Toast.LENGTH_SHORT).show();
        PlanLab.get(getApplicationContext(), mUserId);
        initViews();
        initEvent();
    }
    private void initViews() {
        mRadioGroup = (RadioGroup)findViewById(R.id.activity_plan_pager_list_rg);
        mRadioButtonIU = (RadioButton)findViewById(R.id.activity_plan_pagerr_list_rbIU);
        mRadioButtonINU = (RadioButton)findViewById(R.id.activity_plan_pagerr_list_rbINU);
        mRadioButtonNIU = (RadioButton)findViewById(R.id.activity_plan_pagerr_list_rbNIU);
        mRadioButtonNINU = (RadioButton)findViewById(R.id.activity_plan_pagerr_list_rbNINU);
        PlanListFragment pe_iu = PlanListFragment.newInstance(Plan.IMPORTANT_URGENT, mUserId);
        PlanListFragment pe_niu = PlanListFragment.newInstance(Plan.UNIMPORTANT_URGENT, mUserId);
        PlanListFragment pe_inu = PlanListFragment.newInstance(Plan.IMPORTANT_NOTURGENT, mUserId);
        PlanListFragment pe_ninu = PlanListFragment.newInstance(Plan.UNIMPORTANT_NOTURGENT, mUserId);
        mPlanListFragments.add(pe_iu);
        mPlanListFragments.add(pe_inu);
        mPlanListFragments.add(pe_niu);
        mPlanListFragments.add(pe_ninu);
        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        FragmentManager fm = getSupportFragmentManager();
        TabAdapter adapter = new TabAdapter(fm, mPlanListFragments);
        mViewPager.setAdapter(adapter);
    }

    private void initEvent() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int chckedId) {
                switch (chckedId) {
                    case R.id.activity_plan_pagerr_list_rbIU:
                        mViewPager.setCurrentItem(Plan.IMPORTANT_URGENT);
                        break;
                    case R.id.activity_plan_pagerr_list_rbINU:
                        mViewPager.setCurrentItem(Plan.IMPORTANT_NOTURGENT);
                        break;
                    case R.id.activity_plan_pagerr_list_rbNIU:
                        mViewPager.setCurrentItem(Plan.UNIMPORTANT_URGENT);
                        break;
                    case R.id.activity_plan_pagerr_list_rbNINU:
                        mViewPager.setCurrentItem(Plan.UNIMPORTANT_NOTURGENT);
                        break;
                    default:
                        mViewPager.setCurrentItem(Plan.IMPORTANT_URGENT);
                        break;
                }
            }

        });
        mViewPager.addOnPageChangeListener(new TabOnPageChangeListener());
    }

    public class TabOnPageChangeListener implements ViewPager.OnPageChangeListener{
        public void onPageScrollStateChanged(int state) {}
        public void onPageScrolled(int position, float possitionOffset, int positionOffsetPixels) {

        }

        public void onPageSelected(int position) {
            switch (position) {
                case Plan.IMPORTANT_URGENT:
                    mRadioButtonIU.setChecked(true);
                    break;
                case Plan.UNIMPORTANT_URGENT:
                    mRadioButtonNIU.setChecked(true);
                    break;
                case Plan.IMPORTANT_NOTURGENT:
                    mRadioButtonINU.setChecked(true);
                    break;
                case Plan.UNIMPORTANT_NOTURGENT:
                    mRadioButtonNINU.setChecked(true);
                    break;
                default:
                    mRadioButtonIU.setChecked(true);
            }
        }
    }
    private class TabAdapter extends FragmentPagerAdapter {
        private List<PlanListFragment> mPlanListFragments;
        public TabAdapter(FragmentManager fm, List<PlanListFragment> listFragments) {
            super(fm);
            mPlanListFragments = listFragments;
        }
        public PlanListFragment getItem(int position) {
            return mPlanListFragments.get(position);
        }
        public int getCount() {
            return mPlanListFragments.size();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(this) != null) {
                    //NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(USER_ID, mUserId);

    }
}