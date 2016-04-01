package com.ale.aoe2.sortable;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ale on 14/03/2016. //DEPRECATED!
 */
public class StrategyCollectorFragment extends android.support.v4.app.Fragment {
    private SwipeRefreshLayout swipeContainer;
    RelativeLayout lLayout;
    FragmentActivity superActivity;

    ListView mListView;
    private ProgressBar mProgress;
    Future<File> downloading;
    ArrayList<Strategy> strategiesList;
    //List<File> strategiesFileList = new ArrayList<File>();
    StrAdapter strategiesAdapter;
    Toolbar mToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //container.findViewById()
        lLayout = (RelativeLayout) inflater.inflate(R.layout.strategy_fragment, container, false);
        superActivity  = (FragmentActivity) super.getActivity();

        mProgress = (ProgressBar)lLayout.findViewById(R.id.progress);

        // Enable global Ion logging
        Ion.getDefault(superActivity).configure().setLogging("DD", Log.DEBUG);

        mListView = (ListView) lLayout.findViewById(R.id.list);

        loadJsonStrategies(true);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String itemValue = (String) mListView.getItemAtPosition(position);
                //In order to start displaying new activity we need an intent
                Intent intent = new Intent(superActivity.getApplicationContext(), StepperActivity.class);

                //Putting the Id of image as an extra in intent
                intent.putExtra("fileName", (String) strategiesList.get(position).name);
                intent.putExtra("strategyString", (String) strategiesList.get(position).content);
                startActivity(intent);
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final Strategy selectedStrategy = strategiesList.get(position);
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext()); //Check!
                alertDialog.setTitle(selectedStrategy.name);
                //alertDialog.setMessage(selectedStrategy);
                alertDialog.setNegativeButton("Give a star", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //alertDialog.dismiss();
                    }
                });
                alertDialog.setPositiveButton("Download", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(saveStrategyLocally(selectedStrategy._id, selectedStrategy.content)){
                            //startSnack
                        }
                    }
                });

                alertDialog.show();
                return true;
            }
        });

        swipeContainer = (SwipeRefreshLayout) lLayout.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadJsonStrategies(false);
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

    void loadJsonStrategies(final boolean attach) {
        Ion.with(superActivity)
            .load("http://betterbin.co/aoe/last/10/0")
            .asJsonArray()
            .setCallback(new FutureCallback<JsonArray>() {
                @Override
                public void onCompleted(Exception e, JsonArray result) {
                    if (e != null) {
                        Log.d("EE", e.getMessage());
                        startSnackBar(R.string.check_connection);
                    }
                    if(result != null){
                        if(!attach){
                            strategiesList.clear();
                        }
                        strategiesList = new ArrayList<Strategy>( Arrays.asList(
                                new Gson().fromJson(result,Strategy[].class)) );
                        attachListAdapter();
                    }
                }
            });
    }

    void attachListAdapter() {
        Log.d("EE", Integer.valueOf(strategiesList.size()).toString());
        strategiesAdapter = new StrAdapter(superActivity, strategiesList);
        mListView.setAdapter(strategiesAdapter);
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
        } catch (Exception e){Log.d("DD", e.getMessage());}
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
                toDownload = Ion.with(superActivity) //.getApplicationCOntext?
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
                                    //updateStrategiesAdapter(file);
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

//    void updateStrategiesAdapter(File strategy) {
//        Map<String, String> strInfo = new HashMap<String, String>();
//        strInfo = getStrInfo(strategy);
//        Strategy currentStrObj = new Strategy(strInfo.get("name"),
//                strInfo.get("author"),
//                strInfo.get("civ"),
//                strInfo.get("map"),
//                extractIcon(strInfo.get("icon")));
//        strategiesAdapter.add(currentStrObj);
//        strategiesList.add(currentStrObj);
//        strategiesFileList.add(strategy);
//        startSnackBar(R.string.file_imported);
//    }

    void startSnackBar(int id) {
        Resources res = getResources();
        Snackbar snackbar = Snackbar
                .make(lLayout.findViewById(R.id.snackbar), res.getString(id), Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    void resetDownload() {
        // cancel any pending download
        downloading.cancel();
        downloading = null;
        mProgress.setProgress(0);
    }
}
