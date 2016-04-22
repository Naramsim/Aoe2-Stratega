package com.ale.aoe2.stratega;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class CustomSlide extends Fragment {

    private RelativeLayout layoutContainer;
    private ImageView iv;
    private TextView tv;
    private TextView hv;

    public static CustomSlide newInstance(String image, String upperText, String lowerText, int color) {
        CustomSlide myFragment = new CustomSlide();

        Bundle args = new Bundle();
        args.putString("image", image);
        args.putString("upperText", upperText);
        args.putString("lowerText", lowerText);
        args.putInt("color", color);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        int color = getArguments().getInt("color");
        String upper = getArguments().getString("upperText");
        String lower = getArguments().getString("lowerText");
        String image = getArguments().getString("image");

        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        View view = inflater.inflate(R.layout.slide, container, false);

        layoutContainer = (RelativeLayout) view.findViewById(R.id.slide_container);
        iv = (ImageView) view.findViewById(R.id.image_view);
        tv = (TextView) view.findViewById(R.id.text_slide);
        hv = (TextView) view.findViewById(R.id.hint_slide);
        layoutContainer.setBackgroundColor(color);
        tv.setText(upper);
        iv.setImageBitmap(decodedByte);
        hv.setText(lower);

        return view;
    }

}