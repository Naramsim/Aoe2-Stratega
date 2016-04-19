package com.ale.aoe2.aoe2_stratega;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

/**
 * Created by Ale on 18/03/2016.
 */
public class OnlineStrategyFragment extends android.support.v4.app.Fragment {
    LinearLayout lLayout;
    FragmentActivity superActivity;
    private MaterialViewPager mViewPager;
    Toolbar mToolbar;
    SwipeRefreshLayout swipeContainer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //container.findViewById()
        lLayout = (LinearLayout) inflater.inflate(R.layout.view_pager_collector, container, false);
        superActivity = (FragmentActivity) super.getActivity();

        mViewPager = (MaterialViewPager) lLayout.findViewById(R.id.materialViewPager);
        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(superActivity.getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % 4) {
                    case 0:
                        return OnlineStrategyRecyclerViewFragment.newInstance("http://betterbin.co/aoe/starred/10/0");
                    case 1:
                        return OnlineStrategyRecyclerViewFragment.newInstance("http://betterbin.co/aoe/downloaded/10/0");
                    case 2:
                        return OnlineStrategyRecyclerViewFragment.newInstance("http://betterbin.co/aoe/last/10/0");
                    default:
                        return OnlineStrategyRecyclerViewFragment.newInstance("http://betterbin.co/aoe/mine");
                }
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 4) {
                    case 0:
                        return superActivity.getString(R.string.most_stars);
                    case 1:
                        return superActivity.getString(R.string.most_downloads);
                    case 2:
                        return superActivity.getString(R.string.latest);
                    case 3:
                        return superActivity.getString(R.string.mines);
                }
                return "";
            }
        });
        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.md_indigo_600,
                                superActivity.getResources().getDrawable(R.drawable.archer_m));
                    case 1:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.md_blue_500,
                                superActivity.getResources().getDrawable(R.drawable.three_m));
                    case 2:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.md_cyan_800,
                                superActivity.getResources().getDrawable(R.drawable.fog_m));
                    case 3:
                        return HeaderDesign.fromColorResAndDrawable(
                                R.color.md_deep_purple_600,
                                superActivity.getResources().getDrawable(R.drawable.assault_m));
                }

                return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        View logo = lLayout.findViewById(R.id.logo_white);
        if (logo != null)
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                }
            });
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
        inflater.inflate(R.menu.menu_search_to_activity, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
