package com.ale.aoe2.stratega;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

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

        tv = (TextView)lLayout.findViewById(R.id.tip_header);
        tva = (TextView)lLayout.findViewById(R.id.tip_author);
        tv.setText(R.string.tech_fragment_title);
        tva.setText(R.string.tech_fragment_sub);
        lv = (RecyclerView)lLayout.findViewById(R.id.tech_list_view);
        itemsAdapter = new TechAdapter(superActivity, techs);
        llm = new LinearLayoutManager(superActivity);
        //llm.setReverseLayout(true);
        lv.setHasFixedSize(true);
        lv.setLayoutManager(llm);
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
    }


    @Override
    public boolean onQueryTextChange(String query) {
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
        return false;
    }
}
