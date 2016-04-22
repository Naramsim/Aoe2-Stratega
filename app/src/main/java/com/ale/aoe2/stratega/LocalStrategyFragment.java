package com.ale.aoe2.stratega;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tr.xip.errorview.ErrorView;

/**
 * Created by Ale on 07/03/2016.
 */
public class LocalStrategyFragment extends android.support.v4.app.Fragment {
    RelativeLayout lLayout;
    FragmentActivity superActivity;
    MainActivity main_activity;
    ListView mListView;
    ErrorView error_view;
    private ProgressBar mProgress;
    Future<File> downloading;
    ArrayList<Strategy> strategiesList;
    LocalStrategiesListViewAdapter strategiesAdapter;
    Toolbar mToolbar;
    private SwipeRefreshLayout swipeContainer;

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

        //container.findViewById()
        lLayout = (RelativeLayout) inflater.inflate(R.layout.strategy_fragment, container, false);
        superActivity  = (FragmentActivity) super.getActivity();
        strategiesList = new ArrayList<Strategy>();
        mProgress = (ProgressBar)lLayout.findViewById(R.id.progress);

        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(superActivity.getBaseContext());
        //  Create a new boolean and preference and set it to true
        boolean isFirstStart = getPrefs.getBoolean("firstDialog", true);
        //  If the activity has never started before...
        if (isFirstStart) {
            showFirstDialog();
            SharedPreferences.Editor e = getPrefs.edit();
            //  Edit preference to make it false because we don't want this to run again
            e.putBoolean("firstDialog", false);
            e.apply();
        }

        // Enable global Ion logging
        Ion.getDefault(superActivity).configure().setLogging("DD", Log.DEBUG);
        //downloading = downloadStrategy("http://deep.ytlab.me/fast-castle.str");

        mListView = (ListView) lLayout.findViewById(R.id.list);
        error_view = (ErrorView) lLayout.findViewById(R.id.error_view);



        //StepsAdapter that populate the listView
        //final ArrayList<Strategy> arrayOfStrategies = new ArrayList<Strategy>();
        //Log.d("DD", strategiesList.toString());
        strategiesAdapter = new LocalStrategiesListViewAdapter(superActivity, strategiesList);
        mListView.setAdapter(strategiesAdapter);
        //strategiesAdapter.addAll(strategiesList);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent(superActivity.getApplicationContext(), StepperActivity.class);

            String transitionName = getString(R.string.transition_string);
            // Define the view that the animation will start from
            View viewStart = view;
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(superActivity,
                                viewStart,
                                transitionName
                        );

            //Putting the Id of image as an extra in intent
            intent.putExtra("fileName", (String) strategiesList.get(position).name);
            intent.putExtra("file", (File) strategiesList.get(position).strategyFile);
            //startActivity(intent);
            ActivityCompat.startActivity(superActivity, intent, options.toBundle());
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            final String selectedStrategy = strategiesList.get(position).name;
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext()); //Check!
            alertDialog.setTitle(superActivity.getString(R.string.delete));
            alertDialog.setMessage(selectedStrategy);
            alertDialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //strategiesAdapter.remove(strategiesList.get(position)); //TODO: really necessary?
                    boolean wasDeleted = strategiesList.get(position).strategyFile.delete();
                    strategiesList.remove(position);
                    strategiesAdapter.notifyDataSetChanged();
                    if(wasDeleted) {
                        startSnackBar(R.string.delete_strategy);
                        updateListView();
                    }
                }
            });
            alertDialog.setPositiveButton(superActivity.getString(R.string.keep), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //alertDialog.dismiss();
                }
            });

            alertDialog.show();
            return true;
            }
        });

        //Init listView
        updateListView();

        //Get intent if file has been loaded from a file browser
        Uri filePath = superActivity.getIntent().getData();
        try{
            final String scheme = filePath.getScheme();
            final String host = filePath.getHost();
            final String path = filePath.getPath();
            downloading = downloadStrategy(scheme + "://" + host + path);
        }catch(Exception e) {Log.d("DD", "No start from intent");}

        swipeContainer = (SwipeRefreshLayout) lLayout.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                //fetchTimelineAsync(0);
                updateListView();
                Log.d("EE","refresh");
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return lLayout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sort, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sort_by_name) {
            Collections.sort(strategiesList, new Comparator<Strategy>() {
                @Override
                public int compare(Strategy lhs, Strategy rhs) {
                    return lhs.name.toLowerCase().compareTo(rhs.name.toLowerCase());
                }
            });
            strategiesAdapter.notifyDataSetChanged();
            return true;
        }
        if (id == R.id.sort_by_time) {
            Collections.sort(strategiesList, new Comparator<Strategy>() {
                @Override
                public int compare(Strategy lhs, Strategy rhs) {
                    return lhs.strategyFile.lastModified() > rhs.strategyFile.lastModified() ? -1 : 1;
                }
            });
            strategiesAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void updateListView(){
        strategiesList.clear();
        File f = superActivity.getFilesDir();
        File file[] = f.listFiles();
        for (File aFile : file) {
            String currentStrategy = aFile.getName();
            if (isStrategyType(currentStrategy)) { //TODO: check length
                Log.d("Files", "FileName:" + currentStrategy);
                Map<String, String> strInfo = new HashMap<String, String>();
                strInfo = getStrInfo(aFile);
                Strategy currentStrObj = new Strategy(strInfo.get("name"),
                        strInfo.get("author"),
                        strInfo.get("civ"),
                        strInfo.get("map"),
                        extractIcon(strInfo.get("icon")),
                        aFile);
                strategiesList.add(currentStrObj);
            }
        }
        strategiesAdapter.notifyDataSetChanged();
        if (strategiesList.isEmpty()){
            showErrorView(superActivity.getString(R.string.no_local_strategies),
                    superActivity.getString(R.string.download_strategies_online),
                    superActivity.getString(R.string.go_online));
        }else{
            hideErrorView();
        }
    }

    int extractIcon(String icon) {
        return getResources().getIdentifier(icon, "drawable", superActivity.getPackageName());
    }

    boolean isStrategyType(String fileName) {
        return fileName.substring(fileName.length() - 3).equalsIgnoreCase("str");
    }

    Map<String, String> getStrInfo(File toExplore) {
        Map<String, String> toReturn = new HashMap<String, String>();
        Scanner scanner = null;
        String text = "";
        try {
            scanner = new Scanner( toExplore );
            text = scanner.useDelimiter("\\A").next();
            //Log.d("DD",text.replace("\n", "").replace("\r", ""));
            String infoRegex = "^Str@(.*)[\\r\\n]*^Civ:?\\s?(.*)[\\r\\n]*^Map:?\\s?(.*)[\\r\\n]*Name:?\\s?(.*)[\\r\\n]*Author:?\\s?(.*)[\\r\\n]*^Icon:\\s?(.*)";
            Pattern pattern = Pattern.compile(infoRegex, Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                String version = matcher.group(1);
                toReturn.put("v", version);
                String civ = matcher.group(2);
                toReturn.put("civ", civ);
                String map = matcher.group(3);
                toReturn.put("map", map);
                String name = matcher.group(4);
                toReturn.put("name", name);
                String author = matcher.group(5);
                toReturn.put("author", author);
                String icon = matcher.group(6);
                toReturn.put("icon", icon);
            }
            scanner.close();
        } catch (Exception e){}
        return toReturn;
    }

    Future downloadStrategy(String Url) {
        Future toDownload = null;
        String strategyFileName = "";
        if(isStrategyType(Url)) {
            try {
                URI uri = new URI(Url);
                String[] segments = uri.getPath().split("/");
                strategyFileName = segments[segments.length - 1];
                Log.d("DD", uri.getScheme());
            }catch (Exception e) {Log.d("DD", e.toString());}
            if(isNewStrategy(strategyFileName)) {
                toDownload = Ion.with(superActivity) //.getApplicationContext?
                        .load(Url)
                        .progressBar(mProgress)
                                //.progressDialog(progressDialog)
                        .progressHandler(new ProgressCallback() {
                            @Override
                            public void onProgress(long downloaded, long total) {
                                System.out.println("" + downloaded + " / " + total);
                            }
                        })
                        .write(superActivity.getFileStreamPath(strategyFileName))
                        .setCallback(new FutureCallback<File>() {
                            @Override
                            public void onCompleted(Exception e, File file) {
                                if(checkStrategyIntegrity(file)) {
                                    resetDownload();
                                    updateStrategiesAdapter(file);
                                    if (e != null) {
                                        Toast.makeText(superActivity, "Error downloading file", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    //Toast.makeText(MainActivity.this, "File Download complete", Toast.LENGTH_LONG).show();
                                }else {
                                    startSnackBar(R.string.import_error);
                                }
                            }
                        });
            }
        }
        return toDownload;
    }

    void downloadInitialStrategy(String Url) {
        Ion.with(superActivity)
            .load(Url)
            .asJsonObject()
            .setCallback(new FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {
                    if (e != null) {
                        Log.d("DD", e.getMessage());
                        startSnackBar(R.string.check_connection);
                    }
                    if (result != null) {
                        Strategy current = new Gson().fromJson(result.getAsJsonObject("data"), Strategy.class);
                        //Log.d("DD", current.content);
                        if(isNewStrategy(current._id)) {
                            if(saveStrategyLocally(current._id, current.content)){
                                updateListView();
                                startSnackBar(R.string.file_downloaded);
                            }
                        }
                    }
                }
            });
    }

    boolean saveStrategyLocally(String name, String content) {
        FileOutputStream outputStream;
        try {
            outputStream = superActivity.openFileOutput(name + ".str", Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
            startSnackBar(R.string.file_downloaded);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    boolean isNewStrategy(String fileName) {
        File f = superActivity.getFilesDir();
        File file[] = f.listFiles();
        boolean isNew = true;
        for (File aFile : file) {
            if(aFile.getName().equals(fileName)){isNew = false;Log.d("DD", "file already exists");}
        }
        return isNew;
    }

    boolean checkStrategyIntegrity(File strategy) {
        Map<String, String> strInfo = new HashMap<String, String>();
        strInfo = getStrInfo(strategy);
        return !isNullOrEmpty(strInfo);
    }

    public static boolean isNullOrEmpty( final Map< ?, ? > m ) {
        return m == null || m.isEmpty();
    }

    void updateStrategiesAdapter(File strategy) {
        Map<String, String> strInfo = new HashMap<String, String>();
        strInfo = getStrInfo(strategy);
        Strategy currentStrObj = new Strategy(strInfo.get("name"),
                strInfo.get("author"),
                strInfo.get("civ"),
                strInfo.get("map"),
                extractIcon(strInfo.get("icon")),
                strategy);
        //strategiesAdapter.add(currentStrObj);
        strategiesList.add(currentStrObj);
        strategiesAdapter.notifyDataSetChanged();
        startSnackBar(R.string.file_imported);
    }

    void startSnackBar(int id) {
        Resources res = getResources();
        Snackbar snackbar = Snackbar
                .make(lLayout, res.getString(id), Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    void resetDownload() {
        // cancel any pending download
        downloading.cancel();
        downloading = null;
        mProgress.setProgress(0);
    }

    void showErrorView(String title, String subtitle, String button){
        if(error_view != null){
            error_view.setVisibility(View.VISIBLE);
            error_view.setTitle(title);
            error_view.setSubtitle(subtitle);
            error_view.setRetryButtonText(button);
            error_view.setOnRetryListener(new ErrorView.RetryListener() {
                @Override
                public void onRetry() {
                    Log.d("DD", "retry");
                    main_activity.getDrawer().setSelectionAtPosition(2,true);
                }
            });
        }
    }

    void hideErrorView(){
        if(error_view != null){
            error_view.setVisibility(View.GONE);
        }
    }

    void showFirstDialog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(lLayout.getContext()); //Check!
        alertDialog.setTitle(superActivity.getString(R.string.welcome));
        alertDialog.setMessage(superActivity.getString(R.string.welcome_message));
        alertDialog.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {}
        });
        alertDialog.setPositiveButton("Yes, sure", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //alertDialog.dismiss();
                downloadInitialStrategy("http://betterbin.co/aoe/strategies/fYwbyQwAun3pT8pgM");
                downloadInitialStrategy("http://betterbin.co/aoe/strategies/fcGEtMMrqrP9oNF4K");
            }
        });

        alertDialog.show();
    }
}
