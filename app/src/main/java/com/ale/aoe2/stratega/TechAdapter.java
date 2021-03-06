package com.ale.aoe2.stratega;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ale on 09/03/2016.
 */
public class TechAdapter extends RecyclerView.Adapter<TechAdapter.ViewHolder>  {
    private ArrayList<Tech> itemsData;
    private LayoutInflater mInflater;
    Context context;

    public TechAdapter(Context context, ArrayList<Tech> itemsData) {
        this.mInflater = LayoutInflater.from(context);
        this.itemsData = new ArrayList<>(itemsData);
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TechAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tech_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.txtViewTitle.setText(itemsData.get(position).getType());
        viewHolder.tvt.setText(itemsData.get(position).getTech());
        viewHolder.cv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        itemsData.get(position).getTech());
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
                return true;
            }
        });
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtViewTitle;
        public TextView tvt;
        public CardView cv;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.tech_type);
            tvt = (TextView) itemLayoutView.findViewById(R.id.tech_row);
            cv = (CardView) itemLayoutView.findViewById(R.id.lyy_root);
        }

        public void bind(Tech tech) {
            txtViewTitle.setText(tech.getType());
            tvt.setText(tech.getTech());
        }
    }

    @Override
    public int getItemCount() {
        return itemsData.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setModels(ArrayList<Tech> techs) {
        itemsData = new ArrayList<>(techs);
    }

    public Tech removeItem(int position) {
        final Tech tech = itemsData.remove(position);
        notifyItemRemoved(position);
        return tech;
    }

    public void addItem(int position, Tech tech) {
        itemsData.add(position, tech);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Tech tech = itemsData.remove(fromPosition);
        itemsData.add(toPosition, tech);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(ArrayList<Tech> tech) {
        applyAndAnimateRemovals(tech);
        applyAndAnimateAdditions(tech);
        applyAndAnimateMovedItems(tech);
    }

    private void applyAndAnimateRemovals(ArrayList<Tech> newModels) {
        for (int i = itemsData.size() - 1; i >= 0; i--) {
            final Tech model = itemsData.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }
    private void applyAndAnimateAdditions(ArrayList<Tech> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Tech model = newModels.get(i);
            if (!itemsData.contains(model)) {
                addItem(i, model);
            }
        }
    }
    private void applyAndAnimateMovedItems(ArrayList<Tech> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Tech model = newModels.get(toPosition);
            final int fromPosition = itemsData.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
}
