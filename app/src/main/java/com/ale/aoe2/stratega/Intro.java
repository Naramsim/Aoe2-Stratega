package com.ale.aoe2.stratega;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

/**
 * Created by Ale on 17/03/2016.
 */
public class Intro extends AppIntro2 {
    // Please DO NOT override onCreate. Use init.
    String king = "";
    @Override
    public void init(Bundle savedInstanceState) {
//        final File mydir = getDir("intro", Context.MODE_PRIVATE);
//        Ion.with(this)
//                .load("url")
//                .write(new File(mydir, "content.zip"))
//                .setCallback(new FutureCallback<File>() {
//                    @Override
//                    public void onCompleted(Exception e, File file) {
//                        try {
//
//                        } catch (IOException ee) {
//                            Log.d("DD", ee.getMessage());
//                        }
//                    }
//                });

        addSlide(CustomSlide.newInstance(getString(R.string.fog64), "Aoe2 Strategist", getString(R.string.first_description_intro), R.color.md_orange_300));
        addSlide(CustomSlide.newInstance(getString(R.string.viking_king64), "Strategies", getString(R.string.second_description_intro), R.color.md_cyan_400));
        addSlide(CustomSlide.newInstance(getString(R.string.attila_king64), "Learn", getString(R.string.third_description_intro), R.color.md_indigo_400));
        addSlide(CustomSlide.newInstance(getString(R.string.king64), "Win", getString(R.string.fourth_description_intro), R.color.md_green_700));

        //ImageView imageView = new ImageView(this);
                //imageView.setImageResource(R.drawable.forest);
                //imageView.setBackgroundColor(Color.BLACK);
//                imageView.setLayoutParams(new FrameLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT));
                //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                //setBackgroundView(imageView);
    }

    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        finish();
    }

    @Override
    public void onSlideChanged() {
        // Do something when the slide changes.
    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }

}
