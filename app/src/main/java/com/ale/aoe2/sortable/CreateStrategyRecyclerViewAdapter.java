package com.ale.aoe2.sortable;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
    RecyclerView stepsRecyclerView;
    RecyclerView images;
    //TODO: make array list
    private String[] stepText;
    private String[] hintText;

    public CreateStrategyRecyclerViewAdapter(Context context, ArrayList<Integer> itemsData,
                                             Button proceed, RecyclerView images,
                                             String[] stepText, String[] hintText,
                                             RecyclerView stepsRecyclerView) {
        this.context = context;
        this.itemsData = itemsData;
        this.proceedButton = proceed;
        this.images = images;
        this.stepText = stepText;
        this.hintText = hintText;
        this.stepsRecyclerView = stepsRecyclerView;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CreateStrategyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.on_creating_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView, new StepTextListener(), new HintTextListener());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.initialImage.setImageResource(itemsData.get(position));
        viewHolder.initialImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Animation animFadeOut = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.abc_slide_out_bottom);
                proceedButton.setAnimation(animFadeOut);
                proceedButton.setVisibility(View.GONE);
                stepsRecyclerView.smoothScrollToPosition(position);
                showImageRecyclerView();
            }
        });
        //http://stackoverflow.com/questions/31844373/saving-edittext-content-in-recyclerview
        viewHolder.stl.updatePosition(position);
        viewHolder.htl.updatePosition(position);
        viewHolder.t1.setText(stepText[position]);
        viewHolder.t2.setText(hintText[position]);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView initialImage;
        public TextInputEditText t1;
        public TextInputEditText t2;
        public StepTextListener stl;
        public HintTextListener htl;

        public ViewHolder(View itemLayoutView, StepTextListener stl, HintTextListener htl) {
            super(itemLayoutView);
            this.stl = stl;
            this.htl = htl;
            this.initialImage = (ImageView) itemLayoutView.findViewById(R.id.instruction_img);
            this.t1 = (TextInputEditText) itemLayoutView.findViewById(R.id.edit_text);
            this.t2 = (TextInputEditText) itemLayoutView.findViewById(R.id.hint_edit_text);
            this.t1.addTextChangedListener(stl);
            this.t2.addTextChangedListener(htl);
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

    private class StepTextListener implements TextWatcher {
        private int position;
        public void updatePosition(int position) {
            this.position = position;
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            stepText[position] = charSequence.toString();
        }
        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }

    private class HintTextListener implements TextWatcher {
        private int position;
        public void updatePosition(int position) {
            this.position = position;
        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            hintText[position] = charSequence.toString();
        }
        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
}
