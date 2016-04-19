package com.ale.aoe2.aoe2_stratega;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsActivity;

import java.util.Objects;

/**
 * Created by Ale on 09/03/2016.
 */
public class About extends LibsActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        boolean isDark = checkTheme();
        if (isDark) {
            setIntent(new LibsBuilder().withActivityStyle(Libs.ActivityStyle.DARK).intent(this));
        }else{
            setIntent(new LibsBuilder().withActivityStyle(Libs.ActivityStyle.LIGHT).intent(this));
        }

        super.onCreate(savedInstanceState);
    }
    Boolean checkTheme(){
        try{
            SharedPreferences userDetails = getSharedPreferences(
                    getString(R.string.theme_key), MODE_PRIVATE);
            String theme = userDetails.getString("theme", "");

            if(Objects.equals(theme, "dark")){
                return true;
            }
        }catch (Exception e){
            Log.d("DD", e.getMessage());
        }
        return false;
    }
}