package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.braingroom.user.model.dto.ListDialogData1;
import com.braingroom.user.view.DialogHelper;
import com.braingroom.user.view.MessageHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class ListDialogViewModel1 extends ViewModel {

    public ListDialogData1 dialogData;

    public HashMap<String, Integer> selectedItemsMap;

    public Observable<ListDialogData1> source;

    Disposable disposable;
    MessageHelper messageHelper;
    DialogHelper dialogHelper;
    Consumer<HashMap<String, Integer>> resultConsumer;
    public final boolean isMultiSelect;
    public final String title;
    public final ObservableField<String> selectedItemsText = new ObservableField<>("");
    public final Action onOpenerClick;
    private String TAG = getClass().getCanonicalName();

    public ListDialogViewModel1(@NonNull final DialogHelper dialogHelper, final String title, @NonNull final MessageHelper messageHelper
            , Observable<ListDialogData1> sourceObservable, @NonNull HashMap<String, Integer> selectedItemsMap, final boolean isMultiSelect
            , @Nullable Consumer<HashMap<String, Integer>> resultConsumer) {
        dialogHelper.setViewModel(this);
        this.title = title;
        this.messageHelper = messageHelper;
        this.selectedItemsMap = selectedItemsMap;
        this.dialogHelper = dialogHelper;
        this.resultConsumer = resultConsumer;
        this.isMultiSelect = isMultiSelect;
        setSourceObservable(sourceObservable);
        onOpenerClick = new Action() {
            @Override
            public void run() throws Exception {
                show();
            }
        };
        setSelectedItemsText();

    }

    public void setSourceObservable(Observable<ListDialogData1> sourceObservable) {
        source = sourceObservable.doOnNext(new Consumer<ListDialogData1>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull ListDialogData1 items) throws Exception {
                messageHelper.dismissActiveProgress();
                dialogData = items;
                if (dialogData.getItems().isEmpty()) {
                    dialogHelper.showListDialog(title, new ArrayList<String>(Arrays.asList("Not Available")));

                } else if (isMultiSelect)
                    dialogHelper.showMultiselectList(title, new ArrayList<>(dialogData.getItems().keySet())
                            , getSelectedIndex());
                else
                    dialogHelper.showSingleSelectList(title, new ArrayList<>(dialogData.getItems().keySet())
                            , getSelectedIndex());
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("ListDialogViewmodel", "onError in source observable", throwable);
            }
        }).onErrorResumeNext(Observable.<ListDialogData1>empty())
                .share();
    }

    public void reInit(Observable<ListDialogData1> sourceObservable) {
        selectedItemsText.set("select item/items");
        setSourceObservable(sourceObservable);

    }

    public void handleOkClick() {
        dismiss();
    }

    public void show() {
        messageHelper.showProgressDialog(null, "Loading...");
        disposable = source.subscribe();
    }

    public void dismiss() {
        if (!disposable.isDisposed())
            disposable.dispose();
    }

    public Integer[] getSelectedIndex() {
        if (dialogData == null) return new Integer[]{-1};
        List<String> itemList = new ArrayList<>(dialogData.getItems().keySet());
        List<Integer> selectedIdx = new ArrayList<>();
        for (String name : selectedItemsMap.keySet()) {
            selectedIdx.add(itemList.indexOf(name));
        }
        if (selectedIdx.size() == 0)
            return new Integer[]{-1};
        else
            return selectedIdx.toArray(new Integer[selectedIdx.size()]);
    }

    public void setSelectedItemsText() {

        List<String> itemList = new ArrayList<>(selectedItemsMap.keySet());
        if (itemList.size() == 0) selectedItemsText.set("select item/items");
        else if (itemList.size() == 1) selectedItemsText.set(itemList.get(0));
        else selectedItemsText.set(itemList.get(0) + " & " + (itemList.size() - 1) + " others");

    }

    public void setSelectedItems(Integer[] idxs) {
        List<String> itemList = new ArrayList<>(dialogData.getItems().keySet());
        if (idxs[0] == -1) return;
        selectedItemsMap.clear();
        for (int idx : idxs) {
            selectedItemsMap.put(itemList.get(idx), dialogData.getItems().get(itemList.get(idx)));
        }
        setSelectedItemsText();
        // this will be called when atleast one item is selected
        try {
            if (resultConsumer != null)
                resultConsumer.accept(selectedItemsMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setSelectedItemsMap(HashMap<String, Integer> selectedItemsMap) {
        this.selectedItemsMap = selectedItemsMap;
        setSelectedItemsText();
        try {
            if (resultConsumer != null)
                resultConsumer.accept(selectedItemsMap);
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    public List<String> getSelectedItemsId() {
        List<String> retVals = new ArrayList<>();
        for (Integer id : selectedItemsMap.values()) {
            retVals.add(id + "");
        }
        return retVals;
    }


    public void reset() {
        selectedItemsMap.clear();
        selectedItemsText.set("select filter values");
    }


//    public List<String> getSelectedIds() {
//        List<String> idxList = new ArrayList<>();
//        if (selectedIndexes.size() > 0 && selectedIndexes.get(0) != -1) {
//            for (Integer selectedIndex : selectedIndexes) {
//                idxList.add(keyIdMap.get(listItems.get(selectedIndex)));
//            }
//        } else {
//            idxList.add("");
//        }
//        return idxList;
//
//    }

}


