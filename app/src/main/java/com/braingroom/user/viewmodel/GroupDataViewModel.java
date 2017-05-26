package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.braingroom.user.model.response.CategoryTreeResp;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class GroupDataViewModel extends CustomDialogViewModel {

    public List<ViewModel> titleItems ;
    public List<ViewModel> subtitleItems;

    public Map<String,List<ViewModel>> dataMap = new HashMap<>();

    PublishSubject<DataItemViewModel> titleSelectorSubject = PublishSubject.create();
    PublishSubject<IconTextItemViewModel> subtitleSelectorSubject = PublishSubject.create();

    public final ObservableField<String> searchQuery = new ObservableField<>();

//    Map<Integer,List<IconTextItemViewModel>>

    public GroupDataViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator) {
          apiService.getCategoryTree().subscribe(new Consumer<CategoryTreeResp>() {
              @Override
              public void accept(@io.reactivex.annotations.NonNull CategoryTreeResp categoryTreeResp) throws Exception {
                  for (CategoryTreeResp.Snippet snippet : categoryTreeResp.getData()) {
//                      dataMap.put(snippet.getCategory())
                  }
              }
          }, new Consumer<Throwable>() {
              @Override
              public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

              }
          });
//        titleItems = getDefaultTitleVm().mergeWith(getTitleApiObservable()).publish();
//        initSubtitleObservable();
//        fetchAllItems();
    }
//
//    public void initSubtitleObservable() {
//        subtitleItems = apiService.getSegments(selectedTitleId).map(new Function<SegmentResp, List<ViewModel>>() {
//            @Override
//            public List<ViewModel> apply(@io.reactivex.annotations.NonNull SegmentResp segmentResp) throws Exception {
//                List<ViewModel> results = new ArrayList<>();
//                for (final SegmentResp.Snippet snippet : segmentResp.getData()) {
//                    results.add(new IconTextItemViewModel(snippet.getSegmentImage(), snippet.getSegmentName(), new MyConsumer<IconTextItemViewModel>() {
//                        @Override
//                        public void accept(@io.reactivex.annotations.NonNull IconTextItemViewModel vm) {
//                            segmentSelectorSubject.onNext(vm);
//                            selectedTitleId = snippet.getId();
//                        }
//                    }, selectedTitleId.equals(snippet.getId()) ? true : false, segmentSelectorSubject));
//
//                }
//                return results;
//            }
//        }).publish();
//    }

//    public Observable<List<ViewModel>> getTitleApiObservable() {
//        return apiService.getCategory().map(new Function<CategoryResp, List<ViewModel>>() {
//            @Override
//            public List<ViewModel> apply(CategoryResp resp) throws Exception {
//                List<ViewModel> results = new ArrayList<>();
//                for (final CategoryResp.Snippet elem : resp.getData()) {
//                    results.add(new DataItemViewModel(elem.getCategoryName(), selectedTitleId.equals(elem.getId()) ? true : false, new MyConsumer<DataItemViewModel>() {
//                        @Override
//                        public void accept(@io.reactivex.annotations.NonNull DataItemViewModel viewModel) {
//                            selectedTitleId = elem.getId();
////                                    zoom = 11.0f;
//                            categorySelectorSubject.onNext(viewModel);
//                            initSubtitleObservable();
//                            subtitleItems.connect();
//                        }
//                    }, categorySelectorSubject));
//                }
//                return results;
//            }
//        });
//    }
//
//    @Override
//    public void init() {
//        fetchAllItems();
//    }

//    public void fetchAllItems() {
//        titleItems.connect();
//        subtitleItems.connect();
//    }

    private Observable<List<ViewModel>> getDefaultTitleVm() {
        List<ViewModel> data = new ArrayList<>();
        data.add(new DataItemViewModel("Getting categories...", false, null, null));
        return Observable.just(data);
    }


}
