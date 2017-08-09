package com.braingroom.user.view.activity;

import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.SegmentListViewModel;
import com.braingroom.user.viewmodel.ViewModel;
import java.util.HashMap;

/**
 * Created by godara on 08/08/17.
 */

public class SegmentListActivity extends BaseActivity {
    @NonNull
    @Override
    protected ViewModel createViewModel() {
        HashMap<String, Integer> categoryMap = (HashMap<String, Integer>) getIntentSerializable("categoryMap");
        if (categoryMap == null) {
            categoryMap = new HashMap<>();
            categoryMap.put("Fun & Recreation", 1);
        }
        return new SegmentListViewModel(getNavigator(), categoryMap);//new GridViewModel(getMessageHelper(),getNavigator(),ClassListActivity.class);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_segment_list;
    }
}
