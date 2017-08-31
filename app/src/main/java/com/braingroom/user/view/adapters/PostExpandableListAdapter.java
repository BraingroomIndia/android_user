package com.braingroom.user.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.braingroom.user.databinding.DemoPostFragmentBinding;
import com.braingroom.user.databinding.ExpandableListParentItemBinding;
import com.braingroom.user.viewmodel.fragment.ClassDetailDemoPostViewModel;

/**
 * Created by agrahari on 30/08/17.
 */

public class PostExpandableListAdapter extends BaseExpandableListAdapter {

    public static final int NUM_GROUPS = 3;

    ClassDetailDemoPostViewModel classDemo1, classDemo2, classDemo3;
    Context context;

    public PostExpandableListAdapter(Context context, ClassDetailDemoPostViewModel classDemo1, ClassDetailDemoPostViewModel classDemo2, ClassDetailDemoPostViewModel classDemo3) {
        this.context = context;
        this.classDemo1 = classDemo1;
        this.classDemo2 = classDemo2;
        this.classDemo3 = classDemo3;
    }

    @Override
    public int getGroupCount() {
        return NUM_GROUPS;
    }

    @Override
    public int getChildrenCount(int parentPos) {
        return 1;
    }

    @Override
    public Object getGroup(int parentPos) {
        return null;
    }

    @Override
    public Object getChild(int parentPos, int childPos) {
        return null;
    }

    @Override
    public long getGroupId(int parentPos) {
        return parentPos;
    }

    @Override
    public long getChildId(int parentPos, int childPos) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int parentPos, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        ExpandableListParentItemBinding binding = ExpandableListParentItemBinding.inflate(LayoutInflater.from(context));
        switch (parentPos) {
            case 0:
                binding.setTitle("Knowledge & Nugget");
                break;
            case 1:
                binding.setTitle("Buy & Sell");
                break;
            case 2:
                binding.setTitle("Activity Partners");
                break;
        }
        return binding.getRoot();
    }

    @Override
    public View getChildView(int parentPos, int childPos, boolean isLastChild, View convertView, ViewGroup viewGroup) {
        DemoPostFragmentBinding binding = DemoPostFragmentBinding.inflate(LayoutInflater.from(context));
        switch (parentPos) {
            case 0:
                binding.setVm(this.classDemo1);
                break;
            case 1:
                binding.setVm(this.classDemo2);
                break;
            case 2:
                binding.setVm(this.classDemo3);
                break;
        }
        return binding.getRoot();


    }

    @Override
    public boolean isChildSelectable(int parentPos, int childPos) {
        return false;
    }
}
