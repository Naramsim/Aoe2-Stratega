package com.ale.aoe2.sortable;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Created by Ale on 07/03/2016.
 */
public class TechFragment extends android.support.v4.app.Fragment implements SearchView.OnQueryTextListener  {
    RecyclerView lv;
    TextView tv;
    TextView tva;
    LinearLayout lLayout;
    ArrayList<Tech> techs;
    TechAdapter itemsAdapter;
    LinearLayoutManager llm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentActivity superActivity = super.getActivity();
        lLayout = (LinearLayout) inflater.inflate(R.layout.tech_fragment, container, false);

        String[] military_tech = getResources().getStringArray(R.array.tech_military);
        techs = new ArrayList<>();
        for (String string_tech : military_tech) {
            techs.add(new Tech("Military", string_tech));
        }
        tv = (TextView)lLayout.findViewById(R.id.tip_header);
        tva = (TextView)lLayout.findViewById(R.id.tip_author);
        tv.setText("Tech Tree");
        tva.setText("By Philippe le Bon and Naramsim");
        lv = (RecyclerView)lLayout.findViewById(R.id.tech_list_view);
        itemsAdapter = new TechAdapter(superActivity, techs);
        llm = new LinearLayoutManager(superActivity);
        //llm.setReverseLayout(true);
        lv.setHasFixedSize(true);
        lv.setLayoutManager(llm);


        String[] naval_tech = getResources().getStringArray(R.array.naval_tech);
        for (String string_tech : naval_tech) {
            techs.add(new Tech("Naval", string_tech));
        }

        String[] time_tech = getResources().getStringArray(R.array.tech_time);
        for (String string_tech : time_tech) {
            techs.add(new Tech("Time", string_tech));
        }

        String[] eco_tech = getResources().getStringArray(R.array.tech_economy);
        for (String string_tech : eco_tech) {
            techs.add(new Tech("Economy", string_tech));
        }

        String[] rep_tech = getResources().getStringArray(R.array.tech_repair);
        for (String string_tech : rep_tech) {
            techs.add(new Tech("Repair", string_tech));
        }

        String[] res_tech = getResources().getStringArray(R.array.university_tech);
        for (String string_tech : res_tech) {
            techs.add(new Tech("Researches", string_tech));
        }
        lv.setAdapter(itemsAdapter);

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
        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);

//        Toolbar actionBar = (Toolbar) superActivity.findViewById(R.id.toolbar);
//        LayoutInflater inflator = (LayoutInflater) superActivity
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = inflator.inflate(R.layout.actv, null);
//        actionBar.addView(v);
//        final String[] COUNTRIES = new String[] { "Belgium",
//                "France", "France_", "Italy", "Germany", "Spain" };
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(superActivity,
//                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
//        AutoCompleteTextView textView = (AutoCompleteTextView) v
//                .findViewById(R.id.edt);
//        textView.setAdapter(adapter);
    }


    @Override
    public boolean onQueryTextChange(String query) {
        Log.d("EE", Integer.valueOf(techs.size()).toString());
        ArrayList<Tech> filteredModelList = filter(techs, query);
        itemsAdapter.animateTo(filteredModelList);
        new Handler().postDelayed(new Runnable() {
                  @Override
                  public void run() {
                      llm.scrollToPositionWithOffset(0, 0);
                  }
              },100);
        return true;
    }

    private ArrayList<Tech> filter(ArrayList<Tech> models, String query) {
        query = query.toLowerCase();

        final ArrayList<Tech> filteredModelList = new ArrayList<>();
        for (Tech model : models) {
            String title = model.getType().toLowerCase();
            String text = model.getTech().toLowerCase();
            if (text.contains(query) || title.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.activity_menu_item:
//                // Not implemented here
//                return false;
//            case R.id.fragment_menu_item:
//                // Do Fragment menu item stuff here
//                return true;
//            default:
//                break;
//        }

        return false;
    }
}