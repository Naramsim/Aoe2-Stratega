package com.ale.aoe2.stratega;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import it.sephiroth.android.library.tooltip.Tooltip;

/**
 * Created by Ale on 11/04/2016.
 */
public class CreateStrategyFragment extends android.support.v4.app.Fragment implements showTooltip{
    RelativeLayout lLayout;
    FragmentActivity superActivity;
    MainActivity main_activity;
    Button proceedButton;
    RecyclerView recyclerView;
    QuickRecyclerView imagesRecyclerView;
    CreateStrategyRecyclerViewAdapter currentAdapter;
    SlideShowAdapter imagesAdapter;
    int position = 0;
    String name;
    String civ;
    String map;
    String aut;
    String img;
    ArrayList<String> stepInstructions;
    ArrayList<String> hintInstructions;
    ArrayList<Integer> stepsImages;
    ArrayList<String> imageNamesList;
    ArrayList<Boolean> instructionsFollowed;
    MaterialSearchView searchView;
    SharedPreferences getPrefs;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity){
            main_activity = (MainActivity) context;
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        main_activity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        superActivity = super.getActivity();
        lLayout = (RelativeLayout) inflater.inflate(R.layout.new_strategy_fragment, container, false);
        stepsImages = new ArrayList<Integer>();
        stepInstructions = new ArrayList<String>();
        hintInstructions = new ArrayList<String>();
        instructionsFollowed = new ArrayList<Boolean>();
        stepsImages.add(R.drawable.three_camera);
        stepInstructions.add("");
        hintInstructions.add("");
        getPrefs = PreferenceManager
                .getDefaultSharedPreferences(superActivity.getBaseContext());
        final boolean isFirstCreation = getPrefs.getBoolean("firstCreation", true);
        proceedButton = (Button)lLayout.findViewById(R.id.proceed);
        recyclerView = (RecyclerView) lLayout.findViewById(R.id.recycler_view);
        imagesRecyclerView = (QuickRecyclerView)lLayout.findViewById(R.id.imageRecycler);
        searchView = (MaterialSearchView) lLayout.findViewById(R.id.search_view);
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

        imageNamesList = new ArrayList<String>();
        for (int item: drawableRes) {
            imageNamesList.add(superActivity.getResources().getResourceEntryName(item));
        }

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                int position = getImagePos(newText);
                if(position >= 0){
                    imagesRecyclerView.scrollToPosition(position);
                }
                return false;
            }
        });

        currentAdapter = new CreateStrategyRecyclerViewAdapter(superActivity, stepsImages,
                                            proceedButton, imagesRecyclerView,
                                            stepInstructions, hintInstructions, recyclerView,
                                            searchView);
        imagesAdapter = new SlideShowAdapter(superActivity, drawableRes, proceedButton,
                                            imagesRecyclerView, recyclerView, currentAdapter,
                                            searchView, this, isFirstCreation);
        //Button
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepsImages.add(R.drawable.three_camera);
                stepInstructions.add("");
                hintInstructions.add("");
                currentAdapter.notifyItemInserted(stepsImages.size()-1);
                scrollToAction(-1);
                if(isFirstCreation){
                    startTooltip(superActivity.getString(R.string.tooltip_finish_creation), recyclerView, Tooltip.Gravity.CENTER);
                }
            }
        });

        proceedButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                LinearLayoutManager layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
                int nextVisiblePosition = layoutManager.findFirstVisibleItemPosition() + 1;
                stepsImages.add(nextVisiblePosition, R.drawable.three_camera);
                stepInstructions.add(nextVisiblePosition, "");
                hintInstructions.add(nextVisiblePosition, "");
                currentAdapter.notifyItemInserted(nextVisiblePosition);
                //scrollToAction(1);
                return true;
            }
        });

        //Recycleviewer
        final org.solovyev.android.views.llm.LinearLayoutManager layoutManager =
                new org.solovyev.android.views.llm.LinearLayoutManager(superActivity, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(currentAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getAdapterPosition();
                stepsImages.remove(position);
                stepInstructions.remove(position);
                hintInstructions.remove(position);
                currentAdapter.notifyItemRemoved(position);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        imagesRecyclerView.setLayoutManager(new LinearLayoutManager(superActivity, LinearLayoutManager.HORIZONTAL, false));
        imagesRecyclerView.setAdapter(imagesAdapter);
        //imagesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        DefaultItemAnimator animator = new DefaultItemAnimator() {
            @Override
            public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
                return true;
            }
        };
        imagesRecyclerView.setItemAnimator(animator);

        //First Dialog
        if (isFirstCreation) {
            showHelpDialog();
        }

        return lLayout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_done:
                showDialog();
                return true;
            case R.id.help_new_strategy:
                showHelpDialog();
                return true;
        }
        return false;
    }

    boolean saveStrategyLocally() {
        try {
            String uuid = UUID.randomUUID().toString();
            FileOutputStream outputStream;
            String step_text;
            String hint_text;
            String step_image;
            String declared_image;
            String strategyContent = "";
            String template_infos = "Str@v1\n" +
                    "Civ: {civ}\n" +
                    "Map: {map}\n" +
                    "Name: {name} \n" +
                    "Author: {aut}\n" +
                    "Icon: {img}\n\n";
            String compiled_infos = template_infos.replace("{civ}",civ).
                    replace("{map}", map).
                    replace("{name}", name).
                    replace("{aut}", aut).
                    replace("{img}", img );
            String compiled_step = "";
            String compiled_hint = "";
            int current_step = 0;

            outputStream = superActivity.openFileOutput(uuid + ".str", Context.MODE_PRIVATE);
            outputStream.write(compiled_infos.getBytes());
            strategyContent += compiled_infos;
            for (int img: stepsImages){
                step_text = "- {step} [{image}]\n";
                hint_text = "\t+ {hint}\n";
                if(! stepInstructions.get(current_step).equals("")) {
                    declared_image = superActivity.getResources().getResourceEntryName(img);
                    step_image = declared_image.equals("three_camera") ? "three_m" : declared_image;
                    compiled_step = step_text.replace("{step}", stepInstructions.get(current_step))
                            .replace("{image}", step_image);
                    outputStream.write(compiled_step.getBytes());
                    strategyContent += compiled_step;
                    if(! hintInstructions.get(current_step).equals("")) {
                        compiled_hint = hint_text.replace("{hint}", hintInstructions.get(current_step));
                        outputStream.write(compiled_hint.getBytes());
                        strategyContent += compiled_hint;
                    }
                }else{
                    startSnackBar(lLayout, R.string.incomplete_step);
                    return false;
                }
                current_step++;
            }
            outputStream.close();
            saveStrategyOnline(strategyContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void saveStrategyOnline(String content){
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(superActivity.getBaseContext());
        JsonObject json = new JsonObject();
        json.addProperty("xdab", getPrefs.getString("xdab", "default"));
        json.addProperty("content", content);
        json.addProperty("name", "no-android");

        Ion.with(superActivity)
                .load("http://betterbin.co/aoe/strategies")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if( e == null){
                            Log.d("DD", result.toString());
                            if(result.get("message").getAsString().equals("GG")){
                                startSnackBar(lLayout, R.string.upload_success);
                                main_activity.getDrawer().setSelectionAtPosition(1,true);
                            }
                        }
                    }
                });
    }

    private int getImagePos(String query){
        for(String imageName: imageNamesList){
            if(imageName.startsWith(query)){
                return imageNamesList.indexOf(imageName);
            }
        }
        return -1;
    }

    protected void scrollToAction(int mode) {
        LinearLayoutManager layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
        if (mode == 1) {
            recyclerView.smoothScrollToPosition(firstVisiblePosition + 1);
        }else {
            recyclerView.smoothScrollToPosition(stepsImages.size() - 1);
        }
    }

    void showDialog() {
        if(stepInstructions.contains("")){
            startSnackBar(lLayout, R.string.step_missing);
            return;
        }

        AutoCompleteTextView actv;
        LayoutInflater inflater = superActivity.getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.strategy_init_dialog, null);
        actv = (AutoCompleteTextView) dialogLayout.findViewById(R.id.str_img_text);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(superActivity,android.R.layout.select_dialog_item, imageNamesList);
        actv.setThreshold(1);
        actv.setDropDownHeight(600);
        actv.setDropDownAnchor(R.id.dialog_description);
        actv.setAdapter(adapter);
        final AlertDialog alertDialog = new AlertDialog.Builder(superActivity).create();
        alertDialog.setView(dialogLayout);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "PUBLISH",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {/*override below*/}
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DISCARD",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() { //workaround
            @Override
            public void onClick(View v) {
                try{
                    TextInputEditText name_text_input = (TextInputEditText) dialogLayout.findViewById(R.id.str_name_text);
                    TextInputEditText civ_text_input = (TextInputEditText) dialogLayout.findViewById(R.id.str_civ_text);
                    TextInputEditText map_text_input = (TextInputEditText) dialogLayout.findViewById(R.id.str_map_text);
                    TextInputEditText aut_text_input = (TextInputEditText) dialogLayout.findViewById(R.id.str_aut_text);
                    AutoCompleteTextView img_text_input = (AutoCompleteTextView) dialogLayout.findViewById(R.id.str_img_text);
                    name = name_text_input.getText().toString();
                    civ = civ_text_input.getText().toString();
                    map = map_text_input.getText().toString();
                    aut = aut_text_input.getText().toString();
                    img = img_text_input.getText().toString();
                    if(name.matches("") || civ.matches("") || map.matches("") || aut.matches("") || img.matches("")) {
                        startSnackBar(dialogLayout, R.string.uncomplete_submit_strategy_dialog);
                    }else if(stepInstructions.get(0).toString().equals("")){
                        startSnackBar(lLayout, R.string.step_missing);
                        alertDialog.dismiss();
                    }else{
                        saveStrategyLocally();
                        alertDialog.dismiss();
                    }
                }catch (Exception e){Log.d("DD", e.getMessage()); startSnackBar(dialogLayout, R.string.general_error);}
            }
        });
    }

    void showHelpDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(superActivity);
        alertDialog.setTitle(R.string.help_new_strategy_title);
        alertDialog.setMessage(superActivity.getResources().getString(R.string.help_new_strategy));
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                startTooltip(superActivity.getString(R.string.tooltip_change_image),
                                recyclerView, Tooltip.Gravity.CENTER);
            }
        });
        alertDialog.setPositiveButton("Ok, let me go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startTooltip(superActivity.getString(R.string.tooltip_change_image),
                        recyclerView, Tooltip.Gravity.CENTER);
            }
        });
        alertDialog.show();
    }

    public void startTooltip(String text, View current, Tooltip.Gravity gravity){
        Tooltip.make(superActivity,
                new Tooltip.Builder(101)
                        .anchor(current, gravity)
                        .closePolicy(new Tooltip.ClosePolicy()
                                .insidePolicy(true, false)
                                .outsidePolicy(true, false), 0)
                        .activateDelay(0)
                        .showDelay(500)
                        .text(text)
                        .maxWidth(700)
                        .withArrow(true)
                        .withOverlay(false).withStyleId(R.style.ToolTipLayoutCustomStyle)
                        .floatingAnimation(Tooltip.AnimationBuilder.DEFAULT).withCallback(new Tooltip.Callback() {
                            @Override
                            public void onTooltipClose(
                                    final Tooltip.TooltipView v, final boolean fromUser,
                                    final boolean containsTouch) {
                                instructionsFollowed.add(true);
                                if(instructionsFollowed.size() >=3 ){
                                    SharedPreferences.Editor e = getPrefs.edit();
                                    e.putBoolean("firstCreation", false);
                                    e.apply();
                                }
                            }

                            @Override
                            public void onTooltipFailed(Tooltip.TooltipView view) {
                            }

                            @Override
                            public void onTooltipShown(Tooltip.TooltipView view) {
                            }

                            @Override
                            public void onTooltipHidden(Tooltip.TooltipView view) {
                            }
                        })
                        .build()
        ).show();

    }

    void startSnackBar(View v, int id) {
        Resources res = getResources();
        Snackbar snackbar = Snackbar
                .make(v, res.getString(id), Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
