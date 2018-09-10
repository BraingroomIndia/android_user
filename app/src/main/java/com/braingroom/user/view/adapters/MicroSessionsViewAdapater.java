package com.braingroom.user.view.adapters;


import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.braingroom.user.R;
import com.braingroom.user.utils.BindingUtils;
import com.braingroom.user.viewmodel.ClassSessionViewModel;
import com.braingroom.user.viewmodel.ViewModel;

public class MicroSessionsViewAdapater extends RecyclerView.Adapter<MicroSessionsViewAdapater.DataBindingViewHolder>{

    private final
    @NonNull
    ViewModelBinder binder;

    ViewModel viewModel;

    ViewProvider viewProvider;

    boolean microSessionItemsSelectable = true;

    public MicroSessionsViewAdapater(ViewModel viewModel, ViewProvider viewProvider) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_micro_sessions, parent, false);
        return new DataBindingViewHolder(binding,view);
    }

    @Override
    public void onBindViewHolder(DataBindingViewHolder holder, int position) {

        binder.bind(holder.viewBinding, viewModel.nonReactiveItems.get(position));
        holder.viewBinding.executePendingBindings();
       ((TextView)holder.viewBinding.getRoot().findViewById(R.id.txt_actual_price)).setPaintFlags(((TextView)holder.viewBinding.getRoot().findViewById(R.id.txt_actual_price)).getPaintFlags()|Paint.STRIKE_THRU_TEXT_FLAG);
        ((CheckBox)holder.viewBinding.getRoot().findViewById(R.id.cb_microsession_name)).setSelected(
                ((ClassSessionViewModel)viewModel.nonReactiveItems.get(position)).classSession.isSelected()
        );
        ((CheckBox)holder.viewBinding.getRoot().findViewById(R.id.cb_microsession_name)).setChecked(
                ((ClassSessionViewModel)viewModel.nonReactiveItems.get(position)).classSession.isSelected()
        );

        if (microSessionItemsSelectable == false)
        {
            ((CheckBox)holder.viewBinding.getRoot().findViewById(R.id.cb_microsession_name)).setClickable(false);
        }

        if (microSessionItemsSelectable)
        {
            ((CheckBox)holder.viewBinding.getRoot().findViewById(R.id.cb_microsession_name)).setClickable(true);
        }
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


        public DataBindingViewHolder(@NonNull ViewDataBinding viewBinding, View itemView) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }
    }

    public void preventItemsClickable()
    {
        microSessionItemsSelectable = false;
    }

    public void allowItemsClickable()
    {
        microSessionItemsSelectable = true;
    }
}
