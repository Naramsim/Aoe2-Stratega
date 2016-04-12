package com.ale.aoe2.sortable;

import android.app.Service;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Ale on 11/04/2016.
 */
public class NewStrategyFragment extends android.support.v4.app.Fragment {
    RelativeLayout lLayout;
    FragmentActivity superActivity;
    Button proceedButton;
    RecyclerView recyclerView;
    RecyclerView imagesRecyclerView;
    CreateStrategyRecyclerViewAdapter currentAdapter;
    SlideShowAdapter imagesAdapter;
    EditText t1;
    EditText t2;
    int position = 0;
    ArrayList<Integer> initialInstruction;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        superActivity = super.getActivity();
        lLayout = (RelativeLayout) inflater.inflate(R.layout.new_strategy_fragment, container, false);
        initialInstruction = new ArrayList<Integer>();
        proceedButton = (Button)lLayout.findViewById(R.id.proceed);
        recyclerView = (RecyclerView) lLayout.findViewById(R.id.recycler_view);
        imagesRecyclerView = (RecyclerView)lLayout.findViewById(R.id.imageRecycler);
        ArrayList<Integer> drawableRes = new ArrayList<>(Arrays.asList(R.drawable.alabardier,
                R.drawable.arcbalester,
                R.drawable.archer,
                R.drawable.archer_m,
                R.drawable.archer_range,
                R.drawable.assault_m,
                R.drawable.barrack,
                R.drawable.berserk,
                R.drawable.bg_nd,
                R.drawable.blacksmith,
                R.drawable.boar,
                R.drawable.bombard_cannon,
                R.drawable.bombard_tower,
                R.drawable.bush,
                R.drawable.camel,
                R.drawable.cannon_galeon,
                R.drawable.castle,
                R.drawable.cataphract,
                R.drawable.cataphracts,
                R.drawable.cavalier,
                R.drawable.cavalry_arcer,
                R.drawable.cba_castle,
                R.drawable.cba_front,
                R.drawable.cba_relic,
                R.drawable.cba_tower,
                R.drawable.cba_upgrades,
                R.drawable.champion,
                R.drawable.chu_ko_nu,
                R.drawable.church,
                R.drawable.condottiero,
                R.drawable.conquistador,
                R.drawable.crossbow,
                R.drawable.demolition_ship,
                R.drawable.eagle_scout,
                R.drawable.eagle_warrior,
                R.drawable.elephant_archer,
                R.drawable.fire_ship,
                R.drawable.fishing_ship,
                R.drawable.fog_m,
                R.drawable.galley,
                R.drawable.gate,
                R.drawable.genoese_crossbowman,
                R.drawable.giaguar_warrior,
                R.drawable.gold_mine,
                R.drawable.hand_cannonier,
                R.drawable.house,
                R.drawable.huskarl,
                R.drawable.hussar,
                R.drawable.janissar,
                R.drawable.king,
                R.drawable.knight,
                R.drawable.light_cavalry,
                R.drawable.long_swordsman,
                R.drawable.longboat,
                R.drawable.longbowman,
                R.drawable.lumber,
                R.drawable.mameluke,
                R.drawable.man_at_arms,
                R.drawable.mangudai,
                R.drawable.market,
                R.drawable.mayan_arcer,
                R.drawable.militia,
                R.drawable.mill,
                R.drawable.missionar,
                R.drawable.monk,
                R.drawable.monks,
                R.drawable.onager,
                R.drawable.paladin,
                R.drawable.palisade,
                R.drawable.pikeman,
                R.drawable.ram,
                R.drawable.ram_full,
                R.drawable.samurai,
                R.drawable.scorpion,
                R.drawable.scout,
                R.drawable.scout_cavalry,
                R.drawable.sheep,
                R.drawable.siege_tower,
                R.drawable.siege_workshop,
                R.drawable.skirmisher,
                R.drawable.skirmisher_elite,
                R.drawable.spearman,
                R.drawable.stone_mine,
                R.drawable.stone_wall,
                R.drawable.tarkan,
                R.drawable.tc,
                R.drawable.tc_castle,
                R.drawable.tc_dark,
                R.drawable.tc_feudal,
                R.drawable.tc_imperial,
                R.drawable.teutonic_knight,
                R.drawable.three_m,
                R.drawable.tower,
                R.drawable.trade,
                R.drawable.trading_ship,
                R.drawable.transport_ship,
                R.drawable.trebuchet,
                R.drawable.tuna,
                R.drawable.turtle_ship,
                R.drawable.two_hands,
                R.drawable.university,
                R.drawable.villagers,
                R.drawable.war_elephant,
                R.drawable.war_galley,
                R.drawable.war_wagon,
                R.drawable.watch_tower)
        );


        currentAdapter = new CreateStrategyRecyclerViewAdapter(superActivity, initialInstruction, proceedButton, imagesRecyclerView);
        imagesAdapter = new SlideShowAdapter(superActivity, drawableRes, proceedButton, imagesRecyclerView, recyclerView, currentAdapter);
        //Button

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initialInstruction.add(R.drawable.three_camera);
                currentAdapter.notifyItemInserted(initialInstruction.size()-1);
                scrollToAction();
            }
        });

        //Recycleviewer
        final org.solovyev.android.views.llm.LinearLayoutManager layoutManager = new org.solovyev.android.views.llm.LinearLayoutManager(superActivity, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        initialInstruction.add(R.drawable.three_camera);
        recyclerView.setAdapter(currentAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new StepperRecyclerViewOnScrollListener(layoutManager, superActivity.getWindow()){});

        imagesRecyclerView.setLayoutManager(new LinearLayoutManager(superActivity, LinearLayoutManager.HORIZONTAL, false));
        imagesRecyclerView.setAdapter(imagesAdapter);
        imagesRecyclerView.setItemAnimator(new DefaultItemAnimator());

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

    protected void scrollToAction() {
        recyclerView.smoothScrollToPosition(initialInstruction.size() - 1);
    }
}
