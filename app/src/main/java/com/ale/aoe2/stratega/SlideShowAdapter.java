package com.ale.aoe2.stratega;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import it.sephiroth.android.library.tooltip.Tooltip;

/**
 * Created by Ale on 11/04/2016.
 */
public class SlideShowAdapter extends RecyclerView.Adapter<SlideShowAdapter.ViewHolder> {
    Context context;
    private ArrayList<Integer> itemsData;
    Button proceedButton;
    QuickRecyclerView images;
    RecyclerView onCreatingStep;
    CreateStrategyRecyclerViewAdapter stepAdapter;
    MaterialSearchView searchView;
    private showTooltip tooltipInterface;
    boolean isFirstCreation;

    public SlideShowAdapter(Context context, ArrayList<Integer> itemsData, Button proceed,
                            QuickRecyclerView images, RecyclerView onCreatingStep,
                            CreateStrategyRecyclerViewAdapter stepAdapter, MaterialSearchView searchView,
                            showTooltip tooltipInterface, boolean isFirstCreation) {
        this.context = context;
        this.itemsData = itemsData;
        this.proceedButton = proceed;
        this.images = images;
        this.stepAdapter = stepAdapter;
        this.onCreatingStep = onCreatingStep;
        this.searchView = searchView;
        this.tooltipInterface = tooltipInterface;
        this.isFirstCreation = isFirstCreation;
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
        //viewHolder.iv.setImageResource(itemsData.get(position));

        Picasso.with(context)
                .load(itemsData.get(position))
                .resize(350,300)
                .centerCrop()
                //.fit()
                .into(viewHolder.iv);
        viewHolder.ti.setText(context.getResources().getResourceEntryName(itemsData.get(position)));
        viewHolder.iv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Animation animFadeOut = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.abc_slide_out_top);
                animFadeOut.setAnimationListener(new Animation.AnimationListener(){
                    @Override
                    public void onAnimationStart(Animation animation){
                        searchView.closeSearch();
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation){}
                    @Override
                    public void onAnimationEnd(Animation animation){
                        images.setVisibility(View.GONE);
                        LinearLayoutManager layoutManager = ((LinearLayoutManager)onCreatingStep.getLayoutManager());
                        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                        stepAdapter.setItem(firstVisiblePosition, itemsData.get(position));
                        stepAdapter.notifyItemChanged(firstVisiblePosition);
                        showProceedButton();
                    }
                });
                images.startAnimation(animFadeOut);
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
        if (isFirstCreation) {
            tooltipInterface.startTooltip(context.getString(R.string.tooltip_step_explanation), proceedButton, Tooltip.Gravity.TOP);
        }
    }
}
