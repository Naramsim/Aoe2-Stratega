package com.ale.aoe2.stratega;

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
import android.widget.ImageView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Ale on 11/04/2016.
 */
public class CreateStrategyRecyclerViewAdapter extends RecyclerView.Adapter<CreateStrategyRecyclerViewAdapter.ViewHolder> {
    Context context;
    private ArrayList<String> itemsData;
    private ArrayList<String> stepText;
    private ArrayList<String> hintText;
    Button proceedButton;
    RecyclerView stepsRecyclerView;
    QuickRecyclerView images;
    MaterialSearchView searchView;



    public CreateStrategyRecyclerViewAdapter(Context context, ArrayList<String> itemsData,
                                             Button proceed, QuickRecyclerView images,
                                             ArrayList<String> stepText, ArrayList<String> hintText,
                                             RecyclerView stepsRecyclerView, MaterialSearchView searchView) {
        this.context = context;
        this.itemsData = itemsData;
        this.proceedButton = proceed;
        this.images = images;
        this.stepText = stepText;
        this.hintText = hintText;
        this.stepsRecyclerView = stepsRecyclerView;
        this.searchView = searchView;
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
        Log.d("DD","asdasd");
        Picasso.with(context).load(loadImage(itemsData.get(position)))
                .fit()
                .centerInside()
                .into(viewHolder.initialImage);

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
        viewHolder.t1.setText(stepText.get(position));
        viewHolder.t2.setText(hintText.get(position));
    }

    public File loadImage(String name){
        try {
            File file = new File(context.getDir("images", Context.MODE_PRIVATE), name + ".jpg");
            return file;
//            if(file.exists()){
//                Log.d("DDD","existrs");
//                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                return myBitmap;
//            }
        }catch (Exception e){
            Log.d("DD", e.getMessage());}
        return null;
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

    public boolean setItem(int position, String res){
        itemsData.set(position, res);
        return true;
    }

    void showImageRecyclerView(){
        Animation animFadeIn = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.abc_slide_in_bottom);
        images.setAnimation(animFadeIn);
        images.setVisibility(View.VISIBLE);
        searchView.setVisibility(View.VISIBLE);
        searchView.showSearch(true);
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
            stepText.set(position, charSequence.toString());
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
            hintText.set(position, charSequence.toString());
        }
        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
}
