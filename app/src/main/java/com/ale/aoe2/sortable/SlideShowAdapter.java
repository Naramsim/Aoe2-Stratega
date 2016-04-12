package com.ale.aoe2.sortable;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntegerRes;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
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
public class SlideShowAdapter extends RecyclerView.Adapter<SlideShowAdapter.ViewHolder> {
    Context context;
    private ArrayList<Integer> itemsData;
    Button proceedButton;
    RecyclerView images;
    RecyclerView onCreatingStep;
    CreateStrategyRecyclerViewAdapter stepAdapter;
    public SlideShowAdapter(Context context, ArrayList<Integer> itemsData, Button proceed,
                            RecyclerView images, RecyclerView onCreatingStep,
                            CreateStrategyRecyclerViewAdapter stepAdapter) {
        this.context = context;
        this.itemsData = itemsData;
        this.proceedButton = proceed;
        this.images = images;
        this.stepAdapter = stepAdapter;
        this.onCreatingStep = onCreatingStep;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SlideShowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slideshow_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.iv.setImageResource(itemsData.get(position));
        viewHolder.ti.setText(context.getResources().getResourceEntryName(itemsData.get(position)));
        viewHolder.iv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Animation animFadeOut = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.abc_slide_out_bottom);
                images.setAnimation(animFadeOut);
                images.setVisibility(View.GONE);
                LinearLayoutManager layoutManager = ((LinearLayoutManager)onCreatingStep.getLayoutManager());
                int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                stepAdapter.setItem(firstVisiblePosition, itemsData.get(position));
                stepAdapter.notifyItemChanged(firstVisiblePosition);
                showProceedButton();
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv;
        public TextView ti;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            iv = (ImageView) itemLayoutView.findViewById(R.id.imageRes);
            ti = (TextView) itemLayoutView.findViewById(R.id.imageName);
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

    void showProceedButton(){
        Animation animFadeIn = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.abc_slide_in_bottom);
        proceedButton.setAnimation(animFadeIn);
        proceedButton.setVisibility(View.VISIBLE);
    }
}
