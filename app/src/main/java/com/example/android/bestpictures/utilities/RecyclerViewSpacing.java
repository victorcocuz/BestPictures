package com.example.android.bestpictures.utilities;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/******
 * Created by Victor on 3/20/2018.
 ******/

public class RecyclerViewSpacing extends RecyclerView.ItemDecoration {

    public static final String LOG_TAG = RecyclerViewSpacing.class.getSimpleName();
    private final int spanCount;
    private final int spacing;

    public RecyclerViewSpacing (int spanCount, int spacing) {
        this.spanCount = spanCount;
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;
        outRect.left = spacing - column * spacing / spanCount;
        outRect.right = spacing - column * spacing / spanCount;

        if (position < spanCount) {
            outRect.top = spacing;
        }

        outRect.bottom = spacing * 2;
    }
}
