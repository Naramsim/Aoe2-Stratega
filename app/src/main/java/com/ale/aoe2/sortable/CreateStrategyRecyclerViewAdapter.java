package com.ale.aoe2.sortable;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ale on 11/04/2016.
 */
public class CreateStrategyRecyclerViewAdapter extends RecyclerView.Adapter<CreateStrategyRecyclerViewAdapter.ViewHolder> {
    Context context;
    private ArrayList<String> itemsData;
    Button proceedButton;
    public CreateStrategyRecyclerViewAdapter(Context context, ArrayList<String> itemsData, Button proceed) {
        this.context = context;
        this.itemsData = itemsData;
        this.proceedButton = proceed;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CreateStrategyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.on_creating_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.initialImage.setImageResource(R.drawable.alabardier);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView initialImage;
        public TextInputEditText t1;
        public TextInputEditText t2;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            initialImage = (ImageView) itemLayoutView.findViewById(R.id.instruction_img);
            t1 = (TextInputEditText) itemLayoutView.findViewById(R.id.edit_text);
            t2 = (TextInputEditText) itemLayoutView.findViewById(R.id.hint_edit_text);
        }

        public void bind(Tech tech) {
            initialImage.setImageResource(R.drawable.alabardier);
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
}
