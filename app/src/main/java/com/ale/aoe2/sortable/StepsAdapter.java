package com.ale.aoe2.sortable;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ale on 24/02/2016.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = StepsAdapter.class.getSimpleName();
    private List<Item> items;

    public StepsAdapter(File receiving, Context current) {
        super();
        Resources res = current.getResources();
        items = new ArrayList<>();
        Scanner scanner = null;
        String text = "";
        try{
            scanner = new Scanner( receiving );
            text = scanner.useDelimiter("\\A").next();
            int i = 0;
            //Log.d("DD",text.replace("\n", "").replace("\r", ""));
            String infoRegex = "(?:-\\s?(.*)\\s?\\[(.*)\\])(?:(?:\\n|\\t|\\s)*\\+\\s?(.*))?";
            Pattern pattern = Pattern.compile(infoRegex);
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                String inst = matcher.group(1);
                String img = matcher.group(2);
                String hint = matcher.group(3);
                items.add(new Item(inst, img, hint, current));
                i++;
            }
            scanner.close();
            Log.d("EE",Integer.valueOf(i).toString());
            //randomly generate final cardview
            int[] battleCriesId = {R.string.final_hint0,
                R.string.final_hint1,
                R.string.final_hint2,
                R.string.final_hint3,
                R.string.final_hint4,
                R.string.final_hint5,
                R.string.final_hint6};
            int randomIndex = new Random().nextInt(7);
            int battleCry = battleCriesId[randomIndex];
            items.add(new Item(res.getString(battleCry), "three_m", "", current));
        }catch (Exception e) {Log.d("DD", e.getMessage());}
    }

    public StepsAdapter(String receiving, Context current) {
        super();
        Resources res = current.getResources();
        items = new ArrayList<>();
        Scanner scanner = null;
        String text = "";
        try{
            scanner = new Scanner( receiving );
            text = scanner.useDelimiter("\\A").next();
            int i = 0;
            //Log.d("DD",text.replace("\n", "").replace("\r", ""));
            String infoRegex = "(?:-\\s?(.*)\\s?\\[(.*)\\])(?:(?:\\n|\\t|\\s)*\\+\\s?(.*))?";
            Pattern pattern = Pattern.compile(infoRegex);
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                String inst = matcher.group(1);
                String img = matcher.group(2);
                String hint = matcher.group(3);
                items.add(new Item(inst, img, hint, current));
                i++;
            }
            scanner.close();
            Log.d("EE",Integer.valueOf(i).toString());
            //randomly generate final cardview
            int[] battleCriesId = {R.string.final_hint0,
                    R.string.final_hint1,
                    R.string.final_hint2,
                    R.string.final_hint3,
                    R.string.final_hint4,
                    R.string.final_hint5,
                    R.string.final_hint6};
            int randomIndex = new Random().nextInt(7);
            int battleCry = battleCriesId[randomIndex];
            items.add(new Item(res.getString(battleCry), "assault_m", "", current));
        }catch (Exception e) {Log.d("DD", e.getMessage());}
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        WindowManager windowManager = (WindowManager) parent.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        RecyclerView.LayoutParams inflateParameters = new RecyclerView.LayoutParams(metrics.widthPixels - 32, RecyclerView.LayoutParams.WRAP_CONTENT);
        inflateParameters.setMargins(16,16,16,16);
        v.setLayoutParams(inflateParameters);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Item item = items.get(position);

        holder.title.setText(item.getTitle());
        holder.subtitle.setText(item.getSubtitle());
        holder.img.setImageResource(item.getImg());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView subtitle;
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            subtitle = (TextView) itemView.findViewById(R.id.subtitle);
            img = (ImageView) itemView.findViewById(R.id.instruction_img);
        }
    }
}