package com.ale.aoe2.sortable;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ShareEvent;
import com.koushikdutta.async.future.Future;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.octicons_typeface_library.Octicons;

import io.fabric.sdk.android.Fabric;
import java.io.File;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Ale on 01/03/2016.
 */
public class MainActivity extends AppCompatActivity {
    Toolbar mToolbar;

    ListView mListView;

    private ProgressBar mProgress;

    Future<File> downloading;
    Activity superActivity;

    List<Strategy> strategiesList = new ArrayList<Strategy>();
    List<File> strategiesFileList = new ArrayList<File>();
    LocalStrategiesListViewAdapter strategiesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
        Fabric.with(this, new Crashlytics());

        //Start the Intro
        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());
                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);
                //  If the activity has never started before...
                if (isFirstStart) {
                    Intent i = new Intent(MainActivity.this, Intro.class);
                    startActivity(i);
                    SharedPreferences.Editor e = getPrefs.edit();
                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);
                    String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
                    SecureRandom rnd = new SecureRandom();
                    e.putString("xdab", randomString(9, AB, rnd));
                    e.apply();
                }
            }
        });
        // Start the thread
        t.start();
        superActivity = this;
        //Show everything
        boolean isDark = setTheTheme();
        setContentView(R.layout.activity_main);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(0xFFFFFFFF);

        PrimaryDrawerItem item0 = new PrimaryDrawerItem().withName("Local Strategies")
                .withIcon(Octicons.Icon.oct_repo).withIconColor(getGray());
        PrimaryDrawerItem onlineStr = new PrimaryDrawerItem().withName("Online Strategies")
                .withIcon(Octicons.Icon.oct_repo_clone).withIconColor(getGray());
        PrimaryDrawerItem newStr = new PrimaryDrawerItem().withName("New Strategy")
                .withIcon(Octicons.Icon.oct_repo_force_push).withIconColor(getGray());
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("Tech tree secrets")
                .withIcon(Octicons.Icon.oct_beaker).withIconColor(getGray());
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName("Tips and tricks")
                .withIcon(Octicons.Icon.oct_repo_push).withIconColor(getGray());
        SwitchDrawerItem item3 = new SwitchDrawerItem().withName("Change theme").withSelectable(false).withChecked(isDark)
                .withOnCheckedChangeListener(onCheckedChangeListener)
                .withIcon(Octicons.Icon.oct_paintcan).withIconColor(getGray());
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withName("About").withSelectable(false)
                .withIcon(Octicons.Icon.oct_octoface).withIconColor(getGray());
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withName("Invite Friends").withSelectable(false)
                .withIcon(Octicons.Icon.oct_organization).withIconColor(getGray());

        AccountHeader headerResult = new AccountHeaderBuilder()
            .withActivity(this)
            .withHeaderBackground(R.drawable.bg_nd)
            .withSelectionListEnabledForSingleProfile(false)
            .withAlternativeProfileHeaderSwitching(false)
            .withSelectionSecondLineShown(true)
            .addProfiles(
                    new ProfileDrawerItem().withName("Aoe2 Stratega").withIcon(getResources().getDrawable(R.drawable.aoe2pro))
            )
            .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                @Override
                public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                    return false;
                }
            })
            .build();
        Drawer result = new DrawerBuilder()
            .withActivity(this)
            .withToolbar(mToolbar)
            .withAccountHeader(headerResult)
            .addDrawerItems(
                    item0,
                    onlineStr,
                    newStr,
                    item1,
                    item2,
                    new DividerDrawerItem(),
                    item3,
                    new DividerDrawerItem(),
                    item4,
                    item5
            )
            .withDelayDrawerClickEvent(200)
            .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    switch (position) {
                        case 1:
                            Fragment strategyFragment = new LocalStrategyFragment();
                            fragmentTransaction.replace(R.id.containerView, strategyFragment);
                            fragmentTransaction.commit();
                            break;
                        case 2:
                            Fragment viewPager = new OnlineStrategyFragment();
                            fragmentTransaction.replace(R.id.containerView, viewPager);
                            fragmentTransaction.commit();
                            break;
                        case 3:
                            Fragment newStrategyFragment = new CreateStrategyFragment();
                            fragmentTransaction.replace(R.id.containerView, newStrategyFragment);
                            fragmentTransaction.commit();
                            break;
                        case 4:
                            Fragment techFragment = new TechFragment();
                            fragmentTransaction.replace(R.id.containerView, techFragment);
                            fragmentTransaction.commit();
                            break;
                        case 5:
                            Fragment tipsFragment = new TipsFragment();
                            fragmentTransaction.replace(R.id.containerView, tipsFragment);
                            fragmentTransaction.commit();
                            break;
                        case 7://settings

                            break;
                        case 9:
                            Intent intent = new Intent(getApplicationContext(), About.class);
                            startActivity(intent);
                            break;
                        case 10:
                            Answers.getInstance().logShare(new ShareEvent().
                                    putMethod("Share in Drawer").
                            putContentName("Share"));
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT,
                                    "Hey check out my app at: https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            })
            .build();
        result.getDrawerLayout().setStatusBarBackgroundColor(fetchAccentColor());


        Fragment strategyFragment = new LocalStrategyFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.containerView, strategyFragment, null);
        fragmentTransaction.commit();
    }

    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
            SharedPreferences userDetails = getSharedPreferences(getString(R.string.theme_key), MODE_PRIVATE);
            SharedPreferences.Editor edit = userDetails.edit();
            edit.clear();
            if (drawerItem instanceof Nameable) {
                if(isChecked){
                    edit.putString("theme", "dark");
                }else {
                    edit.putString("theme", "light");
                }
                Log.d("material-drawer", "DrawerItem: " + ((Nameable) drawerItem).getName() + " - toggleChecked: " + isChecked);
            }
            edit.commit();
            finish();
            Intent intent = new Intent(superActivity, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Resources res = getResources();
        //noinspection SimplifiableIfStatement
        if (id == R.string.action_settings) {
            return true;
        }
        if (id == R.id.import_settings) {
            showImportFromURLDialog();
            return true;
        }
        if (id == R.id.import_storage_settings) {
            showDialog(res.getString(R.string.import_from_storage), res.getString(R.string.hint_import_from_storage));
            return true;
        }
        if (id == R.id.create_strategy) {
            showLearnDialog(res.getString(R.string.create_new_strategy), res.getString(R.string.hint_create_new_strategy));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    Boolean setTheTheme(){
        try{
            Context context = this;
            SharedPreferences userDetails = superActivity.getSharedPreferences(
                    getString(R.string.theme_key), MODE_PRIVATE);
            String theme = userDetails.getString("theme", "");

            if(Objects.equals(theme, "dark")){
                Log.d("DD", theme);
                setTheme(R.style.AppTheme);
                return true;
            }else{
                setTheme(R.style.AppThemeLightt);
            }

        }catch (Exception e){
            Log.d("DD", e.getMessage());
        }finally {
            Log.d("DD", "finally");
        }
        return false;
    }

    int getGray() {
        return getResources().getColor(R.color.md_grey_400);
    }

    void showImportFromURLDialog() {
        Resources res = getResources();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(res.getString(R.string.import_from_url));
        builder.setMessage(res.getString(R.string.hint_import_from_url));
        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        final View menuItemView = findViewById(R.id.import_settings);
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.import_from_url, (ViewGroup) menuItemView, false);
        // Set up the input
        final EditText input = (EditText) viewInflated.findViewById(R.id.inputImportUrl);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

        final String[] ImportURLS = new String[] {
                "http://aok.heavengames.com/", "http://www.aoczone.net/", "http://deep.ytlab.me/fast-castle.str"};
        ArrayAdapter<String> inputImportUrlAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, ImportURLS);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                viewInflated.findViewById(R.id.inputImportUrl);
        textView.setAdapter(inputImportUrlAdapter);

        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Log.d("DD", input.getText().toString());
                //downloading = downloadStrategy(input.getText().toString()); //TODO: put downloadStrategy in a class
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    void showDialog(String title, String content) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(content);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    void showLearnDialog(String title, String content) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(content);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Got it",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Browse",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String url = "http://naramsim.github.io/Aoe2-Stratega-Uploader";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
        alertDialog.show();
    }

    private int fetchAccentColor() {
        TypedValue typedValue = new TypedValue();
        TypedArray a = this.obtainStyledAttributes(typedValue.data, new int[]{R.attr.statusBarColor});
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

    String randomString(int len, String AB, SecureRandom rnd){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }
}
