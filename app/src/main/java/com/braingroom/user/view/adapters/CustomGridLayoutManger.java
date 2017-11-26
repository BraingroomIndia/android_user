package com.braingroom.user.view.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

/**
 * Created by godara on 15/11/17.
 */

public class CustomGridLayoutManger extends GridLayoutManager {
    private int totalCount;

    public CustomGridLayoutManger(Context context, final int totalCount) {
        super(context, 2);
        this.totalCount = totalCount;
        this.setSpanSizeLookup(new SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                position = position + 1;
                if (position == totalCount && position % 2 == 1)
                    return 2;
                else
                    return 1;
            }
        });

    }

    public void setTotalCount(final int totalCount) {
        this.totalCount = totalCount;
        this.setSpanSizeLookup(new SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                position = position + 1;
                if (position == totalCount && position % 2 == 1)
                    return 2;
                else
                    return 1;
            }
        });

    }
}
