package com.ale.aoe2.aoe2_stratega;

import android.content.Context;
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
    private List<Step> steps;
    String infoRegex = "(?:-\\s?((?:[^\\[\\]\\n\\r])*)\\s?(?:\\[(\\w*)\\])?)(?:(?:\\n|\\t|\\s)*\\+\\s?(.*))?"; //http://regexr.com/3d7r6

    public StepsAdapter(File receiving, Context current) {
        super();
        steps = new ArrayList<>();
        Scanner scanner = null;
        try{
            scanner = new Scanner( receiving );
            detectSteps(scanner, current);
        }catch (Exception e) {Log.d("DD", e.getMessage());}
    }

    public StepsAdapter(String receiving, Context current) {
        super();
        steps = new ArrayList<>();
        Scanner scanner = null;
        try{
            scanner = new Scanner( receiving );
            detectSteps(scanner, current);
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
        final Step item = steps.get(position);
        if(item.getTitle() != null){
            holder.title.setText(item.getTitle());
        }
        if(item.getSubtitle() != null){
            holder.subtitle.setText(item.getSubtitle());
        }else{
         //   holder.line_separator.setVisibility(View.GONE);
        }
        holder.img.setImageResource(item.getImg());
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView subtitle;
        ImageView img;
        View line_separator;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            subtitle = (TextView) itemView.findViewById(R.id.subtitle);
            img = (ImageView) itemView.findViewById(R.id.instruction_img);
            line_separator = (View) itemView.findViewById(R.id.line_separator);
        }
    }

    private void detectSteps(Scanner scanner, Context current){
        String text = "";
        text = scanner.useDelimiter("\\A").next();
        int i = 0;
        Pattern pattern = Pattern.compile(infoRegex);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String instruction = matcher.group(1);
            String image = (matcher.group(2)!=null && !matcher.group(2).isEmpty()) ? matcher.group(2) : "three_m";
            String hint = matcher.group(3);
            steps.add(new Step(instruction, image, hint, current));
            i++;
        }
        scanner.close();
        //randomly generate final CardView
        int[] battleCriesId = {R.string.final_hint0,
                R.string.final_hint1,
                R.string.final_hint2,
                R.string.final_hint3,
                R.string.final_hint4,
                R.string.final_hint5,
                R.string.final_hint6};
        int randomIndex = new Random().nextInt(7);
        int battleCry = battleCriesId[randomIndex];
        steps.add(new Step(current.getResources().getString(battleCry), "three_m", "Defeat the enemy", current));
    }
}