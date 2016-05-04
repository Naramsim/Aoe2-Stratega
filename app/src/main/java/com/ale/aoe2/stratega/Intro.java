package com.ale.aoe2.stratega;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.ContentFrameLayout;
import android.util.Base64;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Ale on 17/03/2016.
 */
public class Intro extends AppIntro2 {
    SharedPreferences getPrefs;
    @Override
    public void init(Bundle savedInstanceState) {
        getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        boolean firstDownloadRes = getPrefs.getBoolean("first_download_res", true);
        ContentFrameLayout ll =  (ContentFrameLayout) this.findViewById(android.R.id.content);
        ProgressBar a = new ProgressBar(this, null,  android.R.attr.progressBarStyleHorizontal);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        ll.addView(a, lp);
        if(firstDownloadRes){
            final File myDir = getDir("raw", Context.MODE_PRIVATE);
            Ion.with(this)
                .load("https://github.com/Naramsim/Aoe2-Stratega-Uploader/raw/gh-pages/zip/res.zip")
                .progressBar(a)
                .write(new File(myDir, "content.zip"))
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        try {
                            unzip(file, getDir("images", Context.MODE_PRIVATE));
                            startSnackBar(R.string.first_add_download_end);
                        } catch (IOException ee) {
                            Log.d("DD", ee.getMessage());
                        }
                    }
                });
            Ion.with(this)
                .load("https://github.com/Naramsim/Aoe2-Stratega-Uploader/raw/gh-pages/zip/sprites.zip")
                .progressBar(a)
                .write(new File(myDir, "content2.zip"))
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        try {
                            unzip(file, getDir("images", Context.MODE_PRIVATE));
                            startSnackBar(R.string.second_add_download_end);
                        } catch (IOException ee) {
                            Log.d("DD", ee.getMessage());
                        }
                    }
                });
        }
        addSlide(CustomSlide.newInstance(getString(R.string.fog64), "Aoe2 Strategist", getString(R.string.first_description_intro), R.color.md_orange_300));
        addSlide(CustomSlide.newInstance(getString(R.string.viking_king64), "Strategies", getString(R.string.second_description_intro), R.color.md_cyan_400));
        addSlide(CustomSlide.newInstance(getString(R.string.attila_king64), "Learn", getString(R.string.third_description_intro), R.color.md_indigo_400));
        addSlide(CustomSlide.newInstance(getString(R.string.king64), "Win", getString(R.string.fourth_description_intro), R.color.md_green_700));
        startSnackBar(R.string.add_res_download_start);
    }

    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        File file = new File(getDir("images", Context.MODE_PRIVATE), "alabardier.jpg");
        if(file.exists()){
            finish();
        }else {
            startSnackBar(R.string.wait_for_additional_download_end);
        }
    }

    @Override
    public void onSlideChanged() {
        // Do something when the slide changes.
    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }

    void startSnackBar(int id) {
        Resources res = getResources();
        Snackbar snackbar = Snackbar
                .make(this.findViewById(android.R.id.content), res.getString(id), Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void unzip(File zipFile, File targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            }
            Log.d("DD", "Additional res downloaded");
            SharedPreferences.Editor e = getPrefs.edit();
            e.putBoolean("first_download_res", false);
            e.apply();
        } finally {
            zis.close();
        }
    }
}
