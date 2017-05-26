package com.braingroom.user.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by agrahari on 03/04/17.
 */

public class SpacingDecoration extends RecyclerView.ItemDecoration {
    private int space, span;

    public SpacingDecoration(int space, int span) {
        this.space = space;
        this.span = span;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (span == 2)
            if (parent.getChildAdapterPosition(view) == 0 || parent.getChildAdapterPosition(view) == 1)
                outRect.top = space;
        if (span == 1)
            if (parent.getChildAdapterPosition(view) == 0)
                outRect.top = space;
        if (span == 1) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
        } else {
            if (parent.getChildAdapterPosition(view) % span == 0) {
                outRect.left = space;
                outRect.bottom = space;
            }
            if (parent.getChildAdapterPosition(view) % span == 1) {
                outRect.left = space;
                outRect.right = space;
                outRect.bottom = space;
            }
        }

    }
}
