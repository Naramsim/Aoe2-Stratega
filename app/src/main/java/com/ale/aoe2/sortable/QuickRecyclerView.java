package com.ale.aoe2.sortable;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class QuickRecyclerView extends RecyclerView {

    Context context;

    public QuickRecyclerView(Context context) {
        super(context);
        this.context = context;
    }

    public QuickRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QuickRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityY *= 1;
        velocityX *= 1;
        return super.fling(velocityX, velocityY);
    }

}