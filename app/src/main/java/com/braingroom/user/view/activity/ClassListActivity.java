package com.braingroom.user.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.braingroom.user.R;
import com.braingroom.user.databinding.ActivityClassList1Binding;
import com.braingroom.user.model.dto.FilterData;
import com.braingroom.user.utils.CommonUtils;
import com.braingroom.user.view.SpacingDecoration;
import com.braingroom.user.view.adapters.NonReactiveRecyclerViewAdapter;
import com.braingroom.user.viewmodel.ClassListViewModel1;
import com.braingroom.user.viewmodel.ViewModel;

import java.util.HashMap;

public class ClassListActivity extends BaseActivity {

    public interface UiHelper {
        void changeLayout(int layoutType);

        void notifyDataChanged();
    }

    private RecyclerView mRecyclerView;
    private NonReactiveRecyclerViewAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;

    private RecyclerView.OnScrollListener onScrollListener;
    SpacingDecoration linearDecor = new SpacingDecoration((int) CommonUtils.convertDpToPixel(10), 1);
    SpacingDecoration gridDecor = new SpacingDecoration((int) CommonUtils.convertDpToPixel(10), 2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);
        mRecyclerView = ((ActivityClassList1Binding) binding).classRecyclerview;
        mAdapter = new NonReactiveRecyclerViewAdapter(vm, ((ClassListViewModel1) vm).getViewProvider());
        mRecyclerView.setHasFixedSize(true);
        setupRecyclerView(ClassListViewModel1.LAYOUT_TYPE_TILE);
    }

    private void setupRecyclerView(int layoutType) {
        mRecyclerView.removeItemDecoration(linearDecor);
        mRecyclerView.removeItemDecoration(gridDecor);

        if (layoutType == ClassListViewModel1.LAYOUT_TYPE_ROW) {
            linearLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.addItemDecoration(linearDecor);
            mRecyclerView.setAdapter(mAdapter);
            onScrollListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                        ((ClassListViewModel1) vm).paginate();
                    }

                }
            };
            mRecyclerView.clearOnScrollListeners();
            mRecyclerView.addOnScrollListener(onScrollListener);

        } else {
            gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mRecyclerView.addItemDecoration(gridDecor);
            mRecyclerView.setAdapter(mAdapter);
            onScrollListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int visibleItemCount = gridLayoutManager.getChildCount();
                    int totalItemCount = gridLayoutManager.getItemCount();
                    int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();

                    if (dy > 0 && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                        ((ClassListViewModel1) vm).paginate();
                    }

                }
            };
            mRecyclerView.clearOnScrollListeners();
            mRecyclerView.addOnScrollListener(onScrollListener);
        }
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        UiHelper uiHelper = new UiHelper() {
            @Override
            public void changeLayout(int layoutType) {
                setupRecyclerView(layoutType);
            }

            @Override
            public void notifyDataChanged() {
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();
            }
        };
        return new ClassListViewModel1(getMessageHelper(), getNavigator(), getHelperFactory(),
                (FilterData) getIntentSerializable("filterData"),
                (HashMap<String, Integer>) getIntentSerializable("category")
                , (HashMap<String, Integer>) getIntentSerializable("segment")
                , (HashMap<String, String>) getIntentSerializable("city")
                , (HashMap<String, String>) getIntentSerializable("locality")
                , (HashMap<String, Integer>) getIntentSerializable("community")
                , (HashMap<String, Integer>) getIntentSerializable("classType")
                , (HashMap<String, Integer>) getIntentSerializable("classSchedule")
                , (HashMap<String, String>) getIntentSerializable("vendorList"),
                /*getIntentString("categoryId"),
                getIntentString("searchQuery"), getIntentString("communityId"), getIntentString("segmentId"),
                getIntentString("catalogId"), getIntentString("giftId"),*/
                getIntentString("origin"), uiHelper);
    }
/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    protected int getLayoutId() {
        return R.layout.activity_class_list1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
