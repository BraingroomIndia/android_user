package com.braingroom.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.braingroom.user.R;
import com.braingroom.user.databinding.FragmentConnectFeedBinding;
import com.braingroom.user.utils.CommonUtils;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.view.SpacingDecoration;
import com.braingroom.user.view.adapters.NonReactiveRecyclerViewAdapter;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.ConnectFeedViewModel;

/**
 * Created by agrahari on 07/04/17.
 */

public class ConnectFeedFragment extends BaseFragment {

    public static ConnectFeedFragment newInstance(String majorCateg) {

        Bundle bundle = new Bundle();
        bundle.putString("majorCateg", majorCateg);
        ConnectFeedFragment fragment = new ConnectFeedFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private RecyclerView mRecyclerView;
    private NonReactiveRecyclerViewAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;

    private RecyclerView.OnScrollListener onScrollListener;
    SpacingDecoration linearDecor = new SpacingDecoration((int) CommonUtils.convertDpToPixel(10), 1);

    public ConnectFeedViewModel getViewModel() {
        return ((ConnectFeedViewModel) vm);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRecyclerView = ((FragmentConnectFeedBinding) binding).recyclerview;
        mAdapter = new NonReactiveRecyclerViewAdapter(vm, ((ConnectFeedViewModel) vm).getViewProvider());
        mRecyclerView.setHasFixedSize(true);
        setupRecyclerView();
        return binding.getRoot();
    }

    private void setupRecyclerView() {
//        mRecyclerView.removeItemDecoration(linearDecor);
        linearLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(linearLayoutManager);
//        mRecyclerView.addItemDecoration(linearDecor);
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
                    ((ConnectFeedViewModel) vm).paginate();
                }

            }
        };
        mRecyclerView.clearOnScrollListeners();
        mRecyclerView.addOnScrollListener(onScrollListener);
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        FragmentUiHelper uiHelper = new FragmentUiHelper() {
            @Override
            public void notifyDataChanged() {
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();
            }
        };
        return new ConnectFeedViewModel(getStringArguments("majorCateg"), activity.getNavigator(), activity.getHelperFactory(), activity.getMessageHelper(), activity.getIntentString("id")
                , ((ConnectUiHelper) activity), uiHelper);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_connect_feed;
    }

    public interface FragmentUiHelper {
        void notifyDataChanged();
    }

}
