package com.ale.aoe2.sortable;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    private ArrayList<Integer> itemsData;
    Button proceedButton;
    RecyclerView images;

    public CreateStrategyRecyclerViewAdapter(Context context, ArrayList<Integer> itemsData,
                                             Button proceed, RecyclerView images) {
        this.context = context;
        this.itemsData = itemsData;
        this.proceedButton = proceed;
        this.images = images;
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
        viewHolder.initialImage.setImageResource(itemsData.get(position));
        viewHolder.initialImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Animation animFadeOut = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.abc_slide_out_bottom);
                proceedButton.setAnimation(animFadeOut);
                proceedButton.setVisibility(View.GONE);
                showImageRecyclerView();
            }
        });
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
    }

    @Override
    public int getItemCount() {
        return itemsData.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public boolean setItem(int position, int res){
        itemsData.set(position, res);
        return true;
    }

    void showImageRecyclerView(){
        Animation animFadeIn = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.abc_slide_in_bottom);
        images.setAnimation(animFadeIn);
        images.setVisibility(View.VISIBLE);
    }
}
