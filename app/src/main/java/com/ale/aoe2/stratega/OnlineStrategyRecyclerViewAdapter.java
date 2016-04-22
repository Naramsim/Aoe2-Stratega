package com.ale.aoe2.stratega;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Objects;


public class OnlineStrategyRecyclerViewAdapter extends RecyclerView.Adapter<OnlineStrategyRecyclerViewAdapter.ViewHolder> {

    ArrayList<Strategy> contents;
    Context context;
    FragmentActivity superActivity;
    FrameLayout lLayout;
    Resources res;
    String statType;
    OnlineStrategyRecyclerViewFragment superAdapter;

    public OnlineStrategyRecyclerViewAdapter(ArrayList<Strategy> contents,
                                             Context context, String statType,
                                             FragmentActivity superActivity, OnlineStrategyRecyclerViewFragment superAdapter) {
        this.context = context;
        this.contents = contents;
        this.res = context.getResources();
        this.statType = statType;
        this.superActivity = superActivity;
        this.superAdapter = superAdapter;
    }

    public OnlineStrategyRecyclerViewAdapter(ArrayList<Strategy> contents,
                                             Context context, String statType,
                                             FragmentActivity superActivity) {
        this.context = context;
        this.contents = contents;
        this.res = context.getResources();
        this.statType = statType;
        this.superActivity = superActivity;
    }


    @Override
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.strategy_card, parent, false);
        lLayout = (FrameLayout)v;
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Strategy strategy = contents.get(position);
        // Populate the data into the template view using the data object
        if(strategy.title_declared != null){
            holder.tvName.setText(strategy.title_declared.trim());
        }else{
            holder.tvName.setText(strategy.name.trim());
        }

        if(Objects.equals(statType, "download")){
            holder.tvStat.setText(String.format("%s %s", strategy.downloaded, res.getString(R.string.download)));
        }else if(Objects.equals(statType, "star")){
            holder.tvStat.setText(String.format("%s %s", strategy.stars, res.getString(R.string.star)));
        }else{
            //nothing
        }

        holder.tvAuthor.setText(String.format("%s%s", res.getString(R.string.author), strategy.author.trim()));
        holder.tvCiv.setText(String.format("%s%s", res.getString(R.string.civ), strategy.civ.trim()));
        holder.tvMap.setText(String.format("%s%s", res.getString(R.string.map), strategy.map.trim()));

        if(strategy.intIcon == 0){
            //Log.d("EE", Integer.valueOf(context.getResources().getIdentifier(current.icon.trim(), "drawable", getContext().getPackageName())).toString());
            holder.ivIcon.setImageResource(context.getResources().getIdentifier(strategy.icon.trim(), "drawable", context.getPackageName()));
        } else{
            holder.ivIcon.setImageResource(strategy.intIcon);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String itemValue = (String) mListView.getItemAtPosition(position);
                //In order to start displaying new activity we need an intent
                Intent intent = new Intent(context.getApplicationContext(), StepperActivity.class);

                //Putting the Id of image as an extra in intent
                intent.putExtra("fileName", (String) contents.get(position).name);
                intent.putExtra("strategyString", (String) contents.get(position).content);
                context.startActivity(intent);
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final View thisView = view;
                final Strategy selectedStrategy = contents.get(position);
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext()); //Check!
                alertDialog.setTitle(selectedStrategy.title_declared);
                if(statType.equals("mine")){
                    alertDialog.setNegativeButton(superActivity.getString(R.string.delete_online), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteStrategy(selectedStrategy._id, thisView);
                        }
                    });
                }else{
                    alertDialog.setNegativeButton(superActivity.getString(R.string.give_star), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startSnackBar(R.string.starred, thisView);
                            giveStarPoint(selectedStrategy._id);
                        }
                    });
                }
                alertDialog.setPositiveButton(superActivity.getString(R.string.download_strategy), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (saveStrategyLocally(selectedStrategy._id, selectedStrategy.content)) {
                            startSnackBar(R.string.file_downloaded, thisView);
                            giveDownloadPoint(selectedStrategy._id);
                        }
                    }
                });
                alertDialog.show();
                return true;
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName ;
        TextView tvAuthor;
        TextView tvCiv;
        TextView tvMap;
        TextView tvStat;
        public View view;
        private CardView cardView;
        de.hdodenhof.circleimageview.CircleImageView ivIcon;
        public ViewHolder(View itemView){
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.strName);
            tvAuthor = (TextView) itemView.findViewById(R.id.strAuthor);
            tvCiv = (TextView) itemView.findViewById(R.id.strCiv);
            tvMap = (TextView) itemView.findViewById(R.id.strMap);
            ivIcon = (de.hdodenhof.circleimageview.CircleImageView) itemView.findViewById(R.id.circleImage);
            cardView = (CardView) itemView.findViewById(R.id.card_view_collector);
            tvStat = (TextView) itemView.findViewById(R.id.strategyStat);

        }
    }

    boolean saveStrategyLocally(String name, String content) {
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(name + ".str", Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
            //startSnackBar(R.string.file_downloaded);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    void startSnackBar(int id, View v) {
        Log.d("EE", context.getResources().getString(id));
        Snackbar snackbar = Snackbar
                .make(v, context.getResources().getString(id), Snackbar.LENGTH_LONG);
        snackbar.show();
    }
    void giveStarPoint(String id) {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        Ion.with(context)
            .load("http://betterbin.co/aoe/stars")
            .setJsonObjectBody(json)
            .asString()
            .withResponse()
            .setCallback(new FutureCallback<Response<String>>() {
                @Override
                public void onCompleted(Exception e, Response<String> result) {
                    if (e != null) {
                        Log.d("DD", result.getResult());
                    }
                }
            });
    }

    void giveDownloadPoint(String id) {
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        Ion.with(context)
                .load("http://betterbin.co/aoe/download")
            .setJsonObjectBody(json)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        if (e != null) {
                            Log.d("DD", result.getResult());
                        }
                    }
            });
    }

    void deleteStrategy(String id, final View v){
        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(superActivity.getBaseContext());
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("xdab", getPrefs.getString("xdab", "no-default"));
        Ion.with(context)
                .load("http://betterbin.co/aoe/delete")
                .setJsonObjectBody(json)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> result) {
                        if (e != null) {
                            Log.d("DD", result.getResult());
                        }else{
                            if(result.getResult().equals("true")){
                                startSnackBar(R.string.successful_delete, v);
                                superAdapter.loadJsonStrategies(false, lLayout);
                            }
                        }
                    }
                });
    }
}