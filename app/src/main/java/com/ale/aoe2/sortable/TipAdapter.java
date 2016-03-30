package com.ale.aoe2.sortable;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater;
import com.mikepenz.iconics.view.IconicsImageView;

import java.util.ArrayList;

/**
 * Created by Ale on 08/03/2016.
 */
public class TipAdapter extends RecyclerView.Adapter<TipAdapter.ViewHolder> {
    private ArrayList<Tip> itemsData;
    private Context context;

    public TipAdapter(ArrayList<Tip> itemsData, Context context) {
        this.itemsData = itemsData;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TipAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tip_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        GoogleMaterial.Icon icon = null;
        int exp = itemsData.get(position).getExp();
        if(exp==0){
            icon = GoogleMaterial.Icon.gmd_accessible;
        }else if (exp == 1){
            icon = GoogleMaterial.Icon.gmd_directions_walk;
        }else if (exp == 2){
            icon = GoogleMaterial.Icon.gmd_directions_run;
        }
        viewHolder.txtViewTitle.setText(itemsData.get(position).getTip());
        viewHolder.iw.setIcon( new IconicsDrawable(context)
                .icon(icon)
                .color(Color.RED)
                .sizeDp(18));
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtViewTitle;
        public IconicsImageView iw;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.tip_row);
            iw = (IconicsImageView) itemLayoutView.findViewById(R.id.iconics);
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsData.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
