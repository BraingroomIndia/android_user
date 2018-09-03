package com.braingroom.user.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.ClassSessionViewModel;
import com.braingroom.user.viewmodel.ViewModel;

import java.util.List;

public class MicroSessionsViewAdapater extends RecyclerView.Adapter<MicroSessionsViewAdapater.ViewHolder> {
    private List<ViewModel> microSessionsList;
    private Context context;

     public MicroSessionsViewAdapater(List<ViewModel> msList, Context cxt) {
        microSessionsList = msList;
        cxt = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_micro_sessions, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ClassSessionViewModel vmMicrosession = (ClassSessionViewModel) microSessionsList.get(position);
        holder.cks_name.setText(vmMicrosession.name);
        holder.mOfferPrice.setText(vmMicrosession.OfferPrice);
        holder.mActualPrice.setText(vmMicrosession.ActualPrice);
        holder.mDescription.setText(vmMicrosession.Description);
        if (vmMicrosession.isChecked()) {
            holder.cks_name.setChecked(true);
        } else {
            holder.cks_name.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {

        return microSessionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mDescription;
        public TextView mOfferPrice;
        public TextView mActualPrice;
        public CheckBox cks_name;

        public ViewHolder(View itemView) {
            super(itemView);
            mDescription = (TextView) itemView.findViewById(R.id.txt_session_description);
            mActualPrice = (TextView) itemView.findViewById(R.id.txt_actual_price);
            mOfferPrice = (TextView) itemView.findViewById(R.id.txt_offer_price);
            cks_name = (CheckBox) itemView.findViewById(R.id.cb_microsession_name);

            itemView.setOnClickListener(this);

            cks_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if (isChecked) {
                        /*Toast.makeText(MicroSessionsViewAdapater.this.context,
                                "selected microsession is " + cks_name.getText(),
                                Toast.LENGTH_LONG).show();*/
                    } else {

                    }
                }
            });

        }

        @Override
        public void onClick(View view) {

        }
    }
}