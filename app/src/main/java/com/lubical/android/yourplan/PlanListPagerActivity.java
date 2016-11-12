package com.lubical.android.yourplan;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import java.util.ArrayList;
import java.util.List;

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
    private static final int Important_Urgent = 0;
    private static final int NoImportant_Urgent = 1;
    private static final int Important_NoUrgent = 2;
    private static final int NoImportant_NoUrgent = 3;
    private String mUserId;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_pager_list);

        mUserId = savedInstanceState.getString(LoginFragment.EXTRA_USER_ID);
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
        PlanListFragment pe_iu = PlanListFragment.newInstance(Important_Urgent);
        PlanListFragment pe_niu = PlanListFragment.newInstance(NoImportant_Urgent);
        PlanListFragment pe_inu = PlanListFragment.newInstance(Important_NoUrgent);
        PlanListFragment pe_ninu = PlanListFragment.newInstance(NoImportant_NoUrgent);
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
                        mViewPager.setCurrentItem(Important_Urgent);
                        break;
                    case R.id.activity_plan_pagerr_list_rbINU:
                        mViewPager.setCurrentItem(Important_NoUrgent);
                        break;
                    case R.id.activity_plan_pagerr_list_rbNIU:
                        mViewPager.setCurrentItem(NoImportant_Urgent);
                        break;
                    case R.id.activity_plan_pagerr_list_rbNINU:
                        mViewPager.setCurrentItem(NoImportant_NoUrgent);
                        break;
                    default:
                        mViewPager.setCurrentItem(Important_Urgent);
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
                case Important_Urgent:
                    mRadioButtonIU.setChecked(true);
                    break;
                case NoImportant_Urgent:
                    mRadioButtonNIU.setChecked(true);
                    break;
                case Important_NoUrgent:
                    mRadioButtonINU.setChecked(true);
                    break;
                case NoImportant_NoUrgent:
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

}
