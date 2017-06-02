package com.braingroom.user.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.ClassSimpleListViewModel;
import com.braingroom.user.viewmodel.ViewModel;

public class ClassSimpleListActivity extends BaseActivity {

//    private RecyclerView mRecyclerView;
//    private NonReactiveRecyclerViewAdapter mAdapter;
//    private RecyclerView.OnScrollListener onScrollListener;
//    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().setElevation(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mRecyclerView = ((ActivityClassSimplelistBinding) binding).classRecyclerview;
//        setRecyclerView();
    }


//    private void setRecyclerView() {
//
//        linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
//
//        onScrollListener = new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                int visibleItemCount = linearLayoutManager.getChildCount();
//                int totalItemCount = linearLayoutManager.getItemCount();
//                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
//
//                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
//                    ((ClassSimpleListViewModel) vm).paginate();
//                }
//
//            }
//        };
//        mRecyclerView.clearOnScrollListeners();
//        mRecyclerView.addOnScrollListener(onScrollListener);
//    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new ClassSimpleListViewModel(getMessageHelper(), getNavigator(), getIntentString("listType"));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_class_simplelist;
    }
}
