package com.ale.aoe2.stratega;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import it.sephiroth.android.library.tooltip.Tooltip;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class StepperActivity extends AppCompatActivity implements RecognitionListener{

    private static final String PHRASES = "phrases";
    private static final String DIGITS_SEARCH = "digits";
    private SpeechRecognizer recognizer;
    Toolbar mToolbar;
    Button proceedButton;
    RecyclerView recyclerView;
    int position = 0;
    StepperActivity superActivity;
    String content;
    File strategyFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheTheme();
        setContentView(R.layout.stepper_activity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());

        Bundle extras = getIntent().getExtras();
        String strategyName = extras.getString("fileName");
        strategyFile = (File)extras.get("file");
        content = "";
        final boolean comesFromInternet = (String)extras.get("strategyString") != null;
        if(comesFromInternet){
            content = (String)extras.get("strategyString");
        }

        Answers.getInstance().logCustom(new CustomEvent("New Game")
                .putCustomAttribute("Name", strategyName)
                .putCustomAttribute("User", getPrefs.getString("xdab", "default")));

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Back");
        mToolbar.setTitleTextColor(0xFFFFFFFF);
        superActivity = this;

        final org.solovyev.android.views.llm.LinearLayoutManager layoutManager =
                new org.solovyev.android.views.llm.LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                Animation animFadeInBottom = AnimationUtils.loadAnimation(superActivity.getApplicationContext(), R.anim.abc_slide_in_bottom);
                Animation animFadeInTop = AnimationUtils.loadAnimation(superActivity.getApplicationContext(), R.anim.abc_slide_in_top);
                recyclerView.setAnimation(animFadeInBottom);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(layoutManager);
                if(comesFromInternet){
                    recyclerView.setAdapter(new StepsAdapter(content, superActivity));
                }else{
                    recyclerView.setAdapter(new StepsAdapter(strategyFile, superActivity));
                }
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addOnScrollListener(new StepperRecyclerViewOnScrollListener(layoutManager, getWindow()){});

                proceedButton = (Button)findViewById(R.id.proceed);
                proceedButton.setAnimation(animFadeInBottom);
                proceedButton.setVisibility(View.VISIBLE);
                proceedButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    scrollToAction(1);
                    }
                });
                proceedButton.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                    scrollToAction(-2);
                    return true;
                    }
                });
                Tooltip.make(superActivity,
                    new Tooltip.Builder(101)
                        .anchor(proceedButton, Tooltip.Gravity.TOP)
                        .closePolicy(new Tooltip.ClosePolicy()
                                .insidePolicy(true, false)
                                .outsidePolicy(true, false), 0)
                        .activateDelay(0)
                        .showDelay(500)
                        .text(getString(R.string.tooltip_next_step))
                        .maxWidth(700)
                        .withArrow(true)
                        .withOverlay(false).withStyleId(R.style.ToolTipLayoutCustomStyle)
                        .floatingAnimation(Tooltip.AnimationBuilder.DEFAULT)
                        .build()
                ).show();
            }
        },400);


        boolean isFirstStart = getPrefs.getBoolean("firstGame", true);
        if (isFirstStart) {
            showGameDialog();
            SharedPreferences.Editor e = getPrefs.edit();
            e.putBoolean("firstGame", false);
            e.apply();
        }

        //CMU Sphinx
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(StepperActivity.this);
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir);
                } catch (IOException e) {
                    return e;
                }
                return null;
            }
            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    Log.d("DD","Failed to init recognizer " + result.getMessage());
                }else{
                    //Log.d("DD", "setupRecognizer successfully");
                    startSearch();
                }
            }
        }.execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recognizer.cancel();
        recognizer.shutdown();
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;
        //String text = hypothesis.getHypstr();
        //Log.d("DD", "initial hypothesis is " + text);
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        //Log.d("DD", "finished");
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            Log.d("DD", "onResult final hypothesis: " + text);
            handleSpeechResult(text);
        }
    }

    private void startSearch() {
        recognizer.stop();
        //recognizer.startListening(PHRASES);
        recognizer.startListening(DIGITS_SEARCH);
        //Log.d("DD", "startSearch");
    }

    @Override
    public void onBeginningOfSpeech() {
        //Log.d("DD", "onBeginning");
    }

    @Override
    public void onEndOfSpeech() {
        //Log.d("DD", "onEndOfSpeech");
        startSearch();
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        recognizer = defaultSetup()
                .setFloat("-vad_threshold", 3.0)
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
                        // To disable logging of raw audio comment out this call (takes a lot of space on the device)
                //.setRawLogDir(assetsDir)
                        // Threshold to tune for keyphrase to balance between false alarms and misses
                .setKeywordThreshold(1e-5f)
                        // Use context-independent phonetic search, context-dependent is too slow for mobile
                .setBoolean("-allphone_ci", true)
                .getRecognizer();
        recognizer.addListener(this);

        //File phrasesFile = new File(assetsDir, "phrases.gram");
        //recognizer.addGrammarSearch(PHRASES, phrasesFile);
        File digitsGrammar = new File(assetsDir, "commands.gram");
        recognizer.addKeywordSearch(DIGITS_SEARCH, digitsGrammar);
    }

    @Override
    public void onError(Exception error) {
        Log.d("DD", "onError" + error.getMessage());
    }

    @Override
    public void onTimeout() {
        Log.d("DD", "onTimeout");
    }

    void handleSpeechResult(String result) {
        //Thanks java for not implementing switch with strings...
        result = result.trim();
        if(result.equals("back")){
            scrollToAction(-2);
        }else if(result.contains("proceed")){
            scrollToAction(1);
        }else if(result.contains("done")){
            scrollToAction(1);
        }else if(result.contains("step")){
            scrollToAction(1);
        }else if(result.equals("restart")){
            scrollToAction(0);
        }else {
            //do nothing
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    protected void scrollToAction(int direction) {
        LinearLayoutManager layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
        boolean smooth = true;
        if(direction == 0){
            recyclerView.smoothScrollToPosition(0);
        }else if (direction == -2){
            if(firstVisiblePosition>=1){
                recyclerView.smoothScrollToPosition(firstVisiblePosition - 1);
            }
        }else{
            recyclerView.smoothScrollToPosition(firstVisiblePosition + 1);
        }
    }

    Boolean setTheTheme(){
        try{
            Context context = this;
            SharedPreferences userDetails = context.getSharedPreferences(
                    getString(R.string.theme_key), MODE_PRIVATE);
            String theme = userDetails.getString("theme", "");

            if(Objects.equals(theme, "dark")){
                Log.d("DD", theme);
                setTheme(R.style.AppTheme);
                //getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.md_indigo_700));
                return true;
            }else{
                setTheme(R.style.AppThemeLightt);
                //getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.md_grey_900));
            }

        }catch (Exception e){
            Log.d("DD", e.getMessage());
        }
        return false;
    }

    void showGameDialog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(StepperActivity.this);
        alertDialog.setTitle(getString(R.string.first_game));
        alertDialog.setMessage(getString(R.string.game_instructions));
        alertDialog.setNeutralButton("Ok, let me go", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {}
        });

        alertDialog.show();
    }

}
