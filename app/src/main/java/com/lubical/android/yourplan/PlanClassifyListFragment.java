package com.lubical.android.yourplan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by lubical on 2016/11/18.
 */

public class PlanClassifyListFragment extends ListFragment {
    public static final String EXTRA_PLANCLASSIFY = "planclassifylistfragment.planClssify";
    private static final String TAG = "PlanClassifyListFrag";
    private ArrayList<String> planClass;
    private EditText mEditText_new;
    private Button mButton_done;
    private String planclass;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        planClass = PlanLab.get(getActivity()).getClassfy();
        setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, planClass));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_planclass, null);
        mButton_done = (Button)v.findViewById(R.id.fragment_list_planclass_button);
        mEditText_new = (EditText)v.findViewById(R.id.fragment_list_planclass_editText);
        mButton_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planclass = mEditText_new.getText().toString();
                sendResult(Activity.RESULT_OK);
            }
        });
        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        planclass = getListAdapter().getItem(position).toString();
        sendResult(Activity.RESULT_OK);
        Log.d(TAG, planclass+"was clicked");
    }

    private void sendResult(int resultCode) {
        Intent i = new Intent();
        i.putExtra(EXTRA_PLANCLASSIFY, planclass);
        getActivity().setResult(resultCode,i);
        getActivity().finish();
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
