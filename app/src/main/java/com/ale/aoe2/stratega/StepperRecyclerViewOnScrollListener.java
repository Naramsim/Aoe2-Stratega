package com.ale.aoe2.stratega;

import org.solovyev.android.views.llm.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Ale on 03/03/2016.
 */
public abstract class StepperRecyclerViewOnScrollListener extends RecyclerView.OnScrollListener {
    //public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    //private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 0; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1;

    private LinearLayoutManager mLinearLayoutManager;
    private Window mActivityWindow;

    public StepperRecyclerViewOnScrollListener(LinearLayoutManager linearLayoutManager, Window activityWindow) {
        this.mLinearLayoutManager = linearLayoutManager;
        this.mActivityWindow = activityWindow;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();


        if ((totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached
            mActivityWindow.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }
}

