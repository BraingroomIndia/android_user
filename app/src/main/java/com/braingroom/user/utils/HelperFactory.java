package com.braingroom.user.utils;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.DatePicker;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.braingroom.user.R;
import com.braingroom.user.view.DialogHelper;
import com.braingroom.user.view.activity.BaseActivity;
import com.braingroom.user.viewmodel.CustomDialogViewModel;
import com.braingroom.user.viewmodel.DatePickerViewModel;
import com.braingroom.user.viewmodel.ListDialogViewModel1;

import java.util.Arrays;
import java.util.List;

import static com.braingroom.user.utils.BindingUtils.getDefaultBinder;

/**
 * Created by agrahari on 06/04/17.
 */

public class HelperFactory {
    BaseActivity activity;

    public HelperFactory(BaseActivity activity) {
        this.activity = activity;
    }

    public DialogHelper createDialogHelper() {

        return new DialogHelper() {
            @Override
            public void showDatePicker() {
                new MaterialDialog.Builder(activity)
                        .title("")
                        .customView(R.layout.date_picker, false)
                        .positiveText(android.R.string.ok)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                DatePicker datePicker = (DatePicker) dialog.getCustomView().findViewById(R.id.datePicker);
                                int month= datePicker.getMonth()+1;
                                ((DatePickerViewModel) viewModel).date.set(datePicker.getYear() + "-" + month + "-" + datePicker.getDayOfMonth());
                                ((DatePickerViewModel) viewModel).handleOkClick();
                            }
                        })
                        .show();
            }

            @Override
            public void showMultiselectList(String title, List<String> items, Integer[] selectedItems) {
                new MaterialDialog.Builder(activity)
                        .title(title)
                        .items(items)
                        .itemsCallbackMultiChoice(selectedItems, new MaterialDialog.ListCallbackMultiChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                if (which.length > 1 && which[0] == -1)
                                    which = Arrays.copyOfRange(which, 1, which.length);
                                ((ListDialogViewModel1) viewModel).setSelectedItems(which);
                                return true;
                            }
                        })
                        .positiveText("done")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                                ((ListDialogViewModel1) viewModel).handleOkClick();
                            }
                        })
                        .show();


            }

            @Override
            public void showSingleSelectList(String title, List<String> items, final Integer[] selectedItems) {
                new MaterialDialog.Builder(activity)
                        .title(title)
                        .items(items)
                        .itemsCallbackSingleChoice(selectedItems.length > 0 ? selectedItems[0] : -1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog materialDialog, View view, int selectedIdx, CharSequence charSequence) {
                                ((ListDialogViewModel1) viewModel).setSelectedItems(new Integer[]{selectedIdx});
                                return true;

                            }
                        })
                        .positiveText("Done")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                                ((ListDialogViewModel1) viewModel).handleOkClick();
                            }
                        })
                        .show();

            }

            @Override
            public void showCustomView(@LayoutRes int layoutId, final CustomDialogViewModel viewModel) {
                ViewDataBinding binding = DataBindingUtil.inflate(activity.getLayoutInflater(), layoutId, null, false);
                getDefaultBinder().bind(binding, viewModel);
                viewModel.setDialogInstance(new MaterialDialog.Builder(activity).customView(binding.getRoot(), false)
                        .alwaysCallSingleChoiceCallback()
                        .showListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                viewModel.init();
                            }
                        }).show());

            }

            @Override
            public void showListDialog(String title, List<String> items) {
                new MaterialDialog.Builder(activity)
                        .title(title)
                        .items(items)
                        .show();

            }
        };


    }

}
