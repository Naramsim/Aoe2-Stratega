package com.ale.aoe2.sortable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Button;

import java.io.File;

public class Stepper extends AppCompatActivity {

    Toolbar mToolbar;
    Button proceedButton;
    RecyclerView recyclerView;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Bundle extras = getIntent().getExtras();
        String strategyName = extras.getString("fileName");
        File strategyFile = (File)extras.get("file");
        String content = "";
        boolean comesFromInternet = (String)extras.get("strategyString") != null;
        if(comesFromInternet){
            content = (String)extras.get("strategyString");
        }

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

        //Recycleviewer
        final org.solovyev.android.views.llm.LinearLayoutManager layoutManager = new org.solovyev.android.views.llm.LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        if(comesFromInternet){
            recyclerView.setAdapter(new StepsAdapter(content, this));
        }else{
            recyclerView.setAdapter(new StepsAdapter(strategyFile, this));
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new StepperRecyclerViewOnScrollListener(layoutManager, getWindow()){});

        //Button
        this.proceedButton = (Button)this.findViewById(R.id.proceed);
        this.proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToAction(1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    protected void scrollToAction(int direction) {
        LinearLayoutManager layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
        boolean smooth = true;
        position = position + direction;
        position = position <= 0 ? 0 : position;
        if (smooth) {
            recyclerView.smoothScrollToPosition(firstVisiblePosition + 1);
        } else {
            recyclerView.scrollToPosition(firstVisiblePosition + 1);
        }
    }

}
