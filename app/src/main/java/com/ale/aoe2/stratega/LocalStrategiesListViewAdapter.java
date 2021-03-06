package com.ale.aoe2.stratega;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Ale on 29/02/2016.
 */
public class LocalStrategiesListViewAdapter extends ArrayAdapter<Strategy> {
    Context context;
    ArrayList<Strategy> userStrategies;
    public LocalStrategiesListViewAdapter(Context context, ArrayList<Strategy> userStrategies) {
        super(context, 0, userStrategies);
        this.context = context;
        this.userStrategies = userStrategies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Resources res = parent.getResources();
        // Get the data item for this position
        Strategy current = userStrategies.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.strategy, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.strName);
        TextView tvAuthor = (TextView) convertView.findViewById(R.id.strAuthor);
        TextView tvCiv = (TextView) convertView.findViewById(R.id.strCiv);
        TextView tvMap = (TextView) convertView.findViewById(R.id.strMap);
        de.hdodenhof.circleimageview.CircleImageView ivIcon = (de.hdodenhof.circleimageview.CircleImageView) convertView.findViewById(R.id.circleImage);
        // Populate the data into the template view using the data object
        if(current.title_declared != null){
            tvName.setText(current.title_declared.trim());
        }else{
            tvName.setText(current.name.trim());
        }

        tvAuthor.setText(String.format("%s%s", res.getString(R.string.author), current.author.trim()));
        tvCiv.setText(String.format("%s%s", res.getString(R.string.civ), current.civ.trim()));
        tvMap.setText(String.format("%s%s", res.getString(R.string.map), current.map.trim()));

        if(Integer.valueOf(current.intIcon) == 0){
            Picasso.with(context).load(loadImage(current.icon.trim()))
                    //.fit() // not working
                    //.centerCrop()
                    .into(ivIcon);
        }else{
            ivIcon.setImageResource(current.intIcon);
        }
        // Return the completed view to render on screen
        return convertView;
    }

    public File loadImage(String name){
        try {
            File file = new File(context.getDir("images", Context.MODE_PRIVATE), name + ".jpg");
            return file;
        }catch (Exception e){
            Log.d("DD", e.getMessage());}
        return null;
    }
}