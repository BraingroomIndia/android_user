

package com.braingroom.user.view.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.braingroom.user.R;
import com.braingroom.user.utils.BindingUtils;
import com.braingroom.user.viewmodel.ShimmerItemViewModel;
import com.braingroom.user.viewmodel.ViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;


public class NonReactiveRecyclerViewAdapter extends RecyclerView.Adapter<NonReactiveRecyclerViewAdapter.DataBindingViewHolder> {


    private final
    @NonNull
    ViewModelBinder binder;

    ViewModel viewModel;

    ViewProvider viewProvider;

    public NonReactiveRecyclerViewAdapter(ViewModel viewModel,ViewProvider viewProvider) {
        this.binder = BindingUtils.getDefaultBinder();
        this.viewModel = viewModel;
        this.viewProvider = viewProvider;
    }


    @Override
    public int getItemViewType(int position) {
        return viewProvider.getView(viewModel.nonReactiveItems.get(position));
    }

    @Override
    public DataBindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false);
        return new DataBindingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(DataBindingViewHolder holder, int position) {
        binder.bind(holder.viewBinding, viewModel.nonReactiveItems.get(position));
        holder.viewBinding.executePendingBindings();
        if (viewModel.nonReactiveItems.get(position) instanceof ShimmerItemViewModel)
            ((ShimmerFrameLayout) (holder.viewBinding.getRoot()).findViewById(R.id.shimmer_container)).startShimmerAnimation();
    }

    @Override
    public void onViewRecycled(DataBindingViewHolder holder) {
        binder.bind(holder.viewBinding, null);
    }

    @Override
    public int getItemCount() {
        return viewModel.nonReactiveItems.size();
    }


    public static class DataBindingViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        final ViewDataBinding viewBinding;

        public DataBindingViewHolder(@NonNull ViewDataBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }
    }
}
