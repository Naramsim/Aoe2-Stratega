package com.ale.aoe2.sortable;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class OnlineStrategyRecyclerViewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter strategiesAdapter;
    FragmentActivity superActivity;
    RelativeLayout lLayout;
    ArrayList<Strategy> strategiesList;
    String downloadUrl;
    String statType;
    SwipeRefreshLayout swipeContainer;



    public static OnlineStrategyRecyclerViewFragment newInstance( String url) {
        OnlineStrategyRecyclerViewFragment f = new OnlineStrategyRecyclerViewFragment();
        Bundle bdl = new Bundle(4);
        bdl.putString("URL", url);
        if(url.contains("downloaded")){
            bdl.putString("TYPE","download");
        }else if(url.contains("starred")) {
            bdl.putString("TYPE","star");
        }else{
            bdl.putString("TYPE","nothing");
        }
        f.setArguments(bdl);
        return f;
    }

    public OnlineStrategyRecyclerViewFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.downloadUrl = getArguments().getString("URL");
        this.statType = getArguments().getString("TYPE");
        superActivity  = (FragmentActivity) super.getActivity();
        lLayout = (RelativeLayout)inflater.inflate(R.layout.fragment_recyclerview, container, false);

        swipeContainer = (SwipeRefreshLayout) lLayout.findViewById(R.id.swipeContainerOnline);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadJsonStrategies(false, container);
                Log.d("EE", "refresh");
                swipeContainer.setRefreshing(false);//TODO: pass the event to loadJson...
            }
        });
        //Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.md_black_1000,
                R.color.md_cyan_600,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        return lLayout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        loadJsonStrategies(true, view);
        //mAdapter = new RecyclerViewMaterialAdapter(new OnlineStrategyRecyclerViewAdapter(mContentItems));
        //mRecyclerView.setAdapter(mAdapter);

    }

    void loadJsonStrategies(final boolean attach, View view) {
        final View v = view;
        Ion.with(superActivity)
                .load(downloadUrl)
            .asJsonArray()
            .setCallback(new FutureCallback<JsonArray>() {
                @Override
                public void onCompleted(Exception e, JsonArray result) {
                    if (e != null) {
                        Log.d("EE", e.getMessage());
                        startSnackBar(R.string.check_connection, v);
                    }
                    if (result != null) {
                        if (!attach) {
                            strategiesList.clear();
                        }
                        strategiesList = new ArrayList<Strategy>(Arrays.asList(
                                new Gson().fromJson(result, Strategy[].class)));
                        attachListAdapter();
                    }
                }
            });
    }

    void attachListAdapter() {
        Log.d("EE", Integer.valueOf(strategiesList.size()).toString());
        strategiesAdapter = new RecyclerViewMaterialAdapter(new OnlineStrategyRecyclerViewAdapter(strategiesList, superActivity, statType));
        mRecyclerView.setAdapter(strategiesAdapter);
//        for (int i = 0; i < ITEM_COUNT; ++i) {
//            mContentItems.add(new Object());
//        }
//        strategiesAdapter.notifyDataSetChanged();

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
    }

    void startSnackBar(int id, View v) {
        Snackbar snackbar = Snackbar
                .make(v, superActivity.getResources().getString(id), Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
