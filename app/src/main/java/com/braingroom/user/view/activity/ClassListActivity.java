package com.braingroom.user.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.braingroom.user.R;
import com.braingroom.user.databinding.ActivityClassList1Binding;
import com.braingroom.user.model.dto.FilterData;
import com.braingroom.user.utils.CommonUtils;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.view.FragmentHelper;
import com.braingroom.user.view.SpacingDecoration;
import com.braingroom.user.view.adapters.NonReactiveRecyclerViewAdapter;
import com.braingroom.user.view.fragment.SearchSelectListFragment;
import com.braingroom.user.viewmodel.ClassListViewModel1;
import com.braingroom.user.viewmodel.ViewModel;

import java.util.HashMap;

import lombok.Getter;

import static com.braingroom.user.view.activity.FilterActivity.FRAGMENT_TITLE_CITY;
import static com.braingroom.user.view.activity.FilterActivity.FRAGMENT_TITLE_LOCALITY;
import static com.braingroom.user.view.activity.FilterActivity.FRAGMENT_TITLE_VENDORLIST;

public class ClassListActivity extends BaseActivity {

    public interface UiHelper {
        void changeLayout(int layoutType);

        void notifyDataChanged();

        void show(String tag);

        void remove(String tag);
    }

    @Getter
    private ClassListViewModel1 viewModel;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            ((ClassListViewModel1) vm).hideSearchBar.set(!((ClassListViewModel1) vm).hideSearchBar.get());
            return true;
        }
/*        if (item.getItemId() == R.id.home) {
            onBackPressed();
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_class_list1, menu);
        return super.onCreateOptionsMenu(menu);
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

            @Override
            public void show(String tag) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_container, SearchSelectListFragment.newInstance(tag)).addToBackStack(tag).commit();
            }

            @Override
            public void remove(String tag) {
                popBackstack(tag);
            }
        };
        viewModel = new ClassListViewModel1(getMessageHelper(), getNavigator(), getHelperFactory(),
                (FilterData) getIntentSerializable(Constants.classFilterData),
                (HashMap<String, Integer>) getIntentSerializable(Constants.categoryFilterMap)
                , (HashMap<String, Integer>) getIntentSerializable(Constants.segmentsFilterMap)
                , (HashMap<String, String>) getIntentSerializable(Constants.cityFilterMap)
                , (HashMap<String, String>) getIntentSerializable(Constants.localityFilterMap)
                , (HashMap<String, Integer>) getIntentSerializable(Constants.communityFilterMap)
                , (HashMap<String, Integer>) getIntentSerializable(Constants.classTypeFilterMap)
                , (HashMap<String, Integer>) getIntentSerializable(Constants.classScheduleFilterMap)
                , (HashMap<String, String>) getIntentSerializable(Constants.vendorListFilterMap),
                /*getIntentString("categoryId"),
                getIntentString("searchQuery"), getIntentString("communityId"), getIntentString("segmentId"),
                getIntentString("catalogId"), getIntentString("giftId"),*/
                getIntentString(Constants.origin), uiHelper, new FragmentHelper() {
            @Override
            public void show(String tag) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_container, SearchSelectListFragment.newInstance(tag)).addToBackStack(tag).commit();
            }

            @Override
            public void remove(String tag) {
                popBackstack(tag);
            }
        });
        return viewModel;
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

    @Override
    public ViewModel getFragmentViewmodel(String title) {
        if (FRAGMENT_TITLE_LOCALITY.equals(title))
            return viewModel.localityVm;
        return null;
    }
}
