package com.braingroom.user.viewmodel;

import android.databinding.ObservableArrayList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.braingroom.user.model.dto.ListDialogData;
import com.braingroom.user.view.DialogHelper;
import com.braingroom.user.view.MessageHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ListDialogViewModel extends DialogViewModel {
    public List<String> listItems = new ArrayList<>();
    public final ObservableArrayList<Integer> selectedIndexes = new ObservableArrayList<>();
    public Map<String, String> keyIdMap = new HashMap<>();
    public Map<String, String> newSelectedIdMap = new HashMap<>();
    public Map<String, String> oldSelectedIdMap = new HashMap<>();
    public Observable<ListDialogData> source;
    Disposable disposable;
    MessageHelper messageHelper;
    DialogHelper dialogHelper;
    Consumer<List<String>> resultConsumer;

    public ListDialogViewModel(@NonNull final DialogHelper dialogHelper, final String title, @NonNull final MessageHelper messageHelper, Observable<ListDialogData> sourceObservable, final boolean isMultiSelect,@Nullable Consumer<List<String>> resultConsumer) {
        super(dialogHelper, title);
        this.messageHelper = messageHelper;
        selectedItemsText.set("select filter values");
        this.dialogHelper = dialogHelper;
        this.resultConsumer = resultConsumer;
        setSourceObservable(sourceObservable, isMultiSelect, title);
    }

    public void setSourceObservable(Observable<ListDialogData> sourceObservable, final boolean isMultiSelect, final String title) {
        source = sourceObservable.doOnNext(new Consumer<ListDialogData>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull ListDialogData items) throws Exception {
                messageHelper.dismissActiveProgress();
                listItems = new ArrayList<>(items.getItems().keySet());
                keyIdMap = items.getItems();
                if (selectedIndexes.size() == 0)
                    selectedIndexes.addAll(Arrays.asList(items.getSelectedItems()));
                if (isMultiSelect)
                    dialogHelper.showMultiselectList(title, listItems, selectedIndexes.toArray(new Integer[0]));
                else
                    dialogHelper.showSingleSelectList(title, listItems, selectedIndexes.toArray(new Integer[0]));
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("ListDialogViewmodel", "onError in source observable", throwable);
            }
        }).onErrorResumeNext(Observable.<ListDialogData>empty())
                .share();
    }

    public void reInit(Observable<ListDialogData> sourceObservable, final boolean isMultiSelect, final String title) {
        listItems.clear();
        selectedIndexes.clear();
        keyIdMap.clear();
        selectedItemsText.set("select filter values");
        setSourceObservable(sourceObservable, isMultiSelect, title);

    }

    public void reset() {
        listItems.clear();
        selectedIndexes.clear();
        keyIdMap.clear();
        selectedItemsText.set("select filter values");
    }

    @Override
    public void handleOkClick() {
        super.handleOkClick();
        try {
            if (resultConsumer != null)
                resultConsumer.accept(getSelectedIds());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        setSelectedItemsText();
    }

    @Override
    public void show() {
        messageHelper.showProgressDialog(null, "Loading...");
        disposable = source.subscribe();
    }

    @Override
    public void dismiss() {
        if (!disposable.isDisposed())
            disposable.dispose();
    }

    @Override
    public void setSelectedItemsText() {
        if (selectedIndexes.size() > 0 && selectedIndexes.get(0) != -1) {
            selectedItemsText.set(listItems.get(selectedIndexes.get(0)) + " & others");
        }
    }

    public List<String> getSelectedIds() {
        List<String> idxList = new ArrayList<>();
        if (selectedIndexes.size() > 0 && selectedIndexes.get(0) != -1) {
            for (Integer selectedIndex : selectedIndexes) {
                idxList.add(keyIdMap.get(listItems.get(selectedIndex)));
            }
        } else {
            idxList.add("");
        }
        return idxList;

    }

}


