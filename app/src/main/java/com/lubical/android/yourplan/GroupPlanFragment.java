package com.lubical.android.yourplan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lubical on 2016/11/23.
 */

public class GroupPlanFragment  extends Fragment{
    public static final String EXTRA_GROUP_PLAN_ID="groupPlanId";
    private String planId;
    private Plan mPlan;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        planId = getArguments().getString(EXTRA_GROUP_PLAN_ID);
        mPlan = PlanLab.get(getActivity()).getPlan(planId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group_plan, null);

        return v;
    }

    public static GroupPlanFragment newInstance(String planId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_GROUP_PLAN_ID,planId);
        GroupPlanFragment fragment = new GroupPlanFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
