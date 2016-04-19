package com.ale.aoe2.aoe2_stratega;

import android.os.Bundle;
import android.widget.ImageView;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by Ale on 17/03/2016.
 */
public class Intro extends AppIntro2 {
    // Please DO NOT override onCreate. Use init.
    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(AppIntroFragment.newInstance("Aoe2 Stratega", getString(R.string.first_description_intro), R.drawable.fog, R.color.md_blue_900));
        addSlide(AppIntroFragment.newInstance("Strategies", getString(R.string.second_description_intro), R.drawable.vicking_king, R.color.md_indigo_800));
        addSlide(AppIntroFragment.newInstance("Learn", getString(R.string.third_description_intro), R.drawable.attila_king, R.color.md_amber_800));
        addSlide(AppIntroFragment.newInstance("Win", getString(R.string.fourth_description_intro), R.drawable.king_of_the_kings, R.color.md_cyan_400));

        ImageView imageView = new ImageView(this);
                imageView.setImageResource(R.drawable.forest);
                //imageView.setBackgroundColor(Color.BLACK);
//                imageView.setLayoutParams(new FrameLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                setBackgroundView(imageView);
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
