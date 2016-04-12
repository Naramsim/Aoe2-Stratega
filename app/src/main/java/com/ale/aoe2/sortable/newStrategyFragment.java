package com.ale.aoe2.sortable;

import android.app.Service;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ale on 11/04/2016.
 */
public class NewStrategyFragment extends android.support.v4.app.Fragment {
    RelativeLayout lLayout;
    FragmentActivity superActivity;
    Button proceedButton;
    RecyclerView recyclerView;
    EditText t1;
    EditText t2;
    int position = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        superActivity = super.getActivity();
        lLayout = (RelativeLayout) inflater.inflate(R.layout.new_strategy_fragment, container, false);

        //Button
        proceedButton = (Button)lLayout.findViewById(R.id.proceed);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToAction(1);
            }
        });

        //Recycleviewer
        final org.solovyev.android.views.llm.LinearLayoutManager layoutManager = new org.solovyev.android.views.llm.LinearLayoutManager(superActivity, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = (RecyclerView) lLayout.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<String> initialInstruction = new ArrayList<String>();
        initialInstruction.add("hi");
        recyclerView.setAdapter(new CreateStrategyRecyclerViewAdapter(superActivity,initialInstruction, proceedButton));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new StepperRecyclerViewOnScrollListener(layoutManager, superActivity.getWindow()){});

        return lLayout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        FragmentActivity superActivity = super.getActivity();
        super.onCreateOptionsMenu(menu, inflater);
    }

    protected void scrollToAction(int direction) {
        LinearLayoutManager layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
        boolean smooth = true;
        if(direction == 0){
            recyclerView.smoothScrollToPosition(0);
        }else if (direction == -2){
            recyclerView.smoothScrollToPosition(firstVisiblePosition - 1);
        }else{
            recyclerView.smoothScrollToPosition(firstVisiblePosition + 1);
        }
    }
}
