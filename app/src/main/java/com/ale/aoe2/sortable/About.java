package com.ale.aoe2.sortable;

import android.os.Bundle;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsActivity;

/**
 * Created by Ale on 09/03/2016.
 */
public class About extends LibsActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        /*
        Intent intent = new Intent();
        intent.putExtra(Libs.BUNDLE_FIELDS, Libs.toStringArray(R.string.class.getFields()));
        intent.putExtra(Libs.BUNDLE_LIBS, new String[]{"activeandroid", "caldroid"});
        setIntent(intent);
        */
        setIntent(new LibsBuilder().withActivityStyle(Libs.ActivityStyle.LIGHT).intent(this));
        super.onCreate(savedInstanceState);
    }
}