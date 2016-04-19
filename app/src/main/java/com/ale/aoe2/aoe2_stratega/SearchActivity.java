package com.ale.aoe2.aoe2_stratega;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by Ale on 30/03/2016.
 */
public class SearchActivity extends AppCompatActivity {

    static MaterialSearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheTheme();
        setContentView(R.layout.search_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        if (savedInstanceState == null) {
             getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus){
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            searchView.showSearch();
                        }
                    },
                    100);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_online, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle app bar item clicks here. The app bar
        // automatically handles clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {
        RecyclerView lv;
        ArrayList<Strategy> strategiesList;
        OnlineStrategyRecyclerViewAdapter itemsAdapter;
        LinearLayoutManager llm;
        FragmentActivity superActivity;
        View rootView;
        String lastQuery;
        SwipeRefreshLayout swipeContainer;
        //static MaterialSearchView searchView;
        public PlaceholderFragment() { }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            //TODO: put lv init in onViewCreated(as in OnlineStrategyRecyclerViewFragment)
            superActivity = super.getActivity();
            rootView = inflater.inflate(R.layout.fragment_recyclerview, container, false);
            lv = (RecyclerView)rootView.findViewById(R.id.recyclerView);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            lv.setLayoutManager(layoutManager);
            lv.setHasFixedSize(true);
            lastQuery = "";
            loadJsonStrategies(true, "Yaidjask");

            swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainerOnline);
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadJsonStrategies(false, lastQuery);
                    Log.d("EE", "refresh");
                    swipeContainer.setRefreshing(false);//TODO: pass the event to loadJson...
                }
            });
            //Configure the refreshing colors
            swipeContainer.setColorSchemeResources(R.color.md_black_1000,
                    R.color.md_cyan_600,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
            searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    loadJsonStrategies(false, query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    final String query = newText;
                    final boolean typing = true;
                    lastQuery = newText;
//                    new android.os.Handler().postDelayed(
//                            new Runnable() {
//                                public void run() {
                    if(newText.length() > 1){
                        loadJsonStrategies(false, newText);
                    }
//                                }
//                            },
//                            300);
                    return false;
                }
            });
            return rootView;
        }
        void loadJsonStrategies(final boolean attach, String query) {
            JsonObject json = new JsonObject();
            json.addProperty("match", query);
            Log.d("DD", json.toString());
            Ion.with(superActivity)
                    .load("http://betterbin.co/aoe/search")
                    .setHeader("Content-Type", "application/json; charset=UTF-8")
                    .setJsonObjectBody(json)
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {
                            if (e != null) {
                                Log.d("EE", e.getMessage());
                                startSnackBar(R.string.check_connection);
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
            itemsAdapter = new OnlineStrategyRecyclerViewAdapter(strategiesList, superActivity, "star",superActivity);
            lv.setAdapter(itemsAdapter);
        }

        void startSnackBar(int id){
            Snackbar snackbar = Snackbar
                    .make(getView(), superActivity.getResources().getString(id), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    Boolean setTheTheme(){
        try{
            Context context = this;
            SharedPreferences userDetails = context.getSharedPreferences(
                    getString(R.string.theme_key), MODE_PRIVATE);
            String theme = userDetails.getString("theme", "");

            if(Objects.equals(theme, "dark")){
                Log.d("DD", theme);
                setTheme(R.style.AppTheme);
                return true;
            }else{
                setTheme(R.style.AppThemeLightt);
            }

        }catch (Exception e){
            Log.d("DD", e.getMessage());
        }finally {
            //Log.d("DD", "finally");
        }
        return false;
    }

}
