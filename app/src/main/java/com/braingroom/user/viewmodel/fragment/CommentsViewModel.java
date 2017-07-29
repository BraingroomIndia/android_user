package com.braingroom.user.viewmodel.fragment;

import android.databinding.ObservableField;
import android.os.Bundle;

import com.braingroom.user.model.response.CommentListResp;
import com.braingroom.user.model.response.CommentReplyResp;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ConnectHomeActivity;
import com.braingroom.user.view.fragment.CommentFragment;
import com.braingroom.user.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

public class CommentsViewModel extends ViewModel {

    public final Action onSendClicked;
    public Observable<List<ViewModel>> commentsVmObservable;
    public final PublishSubject<List<ViewModel>> commentsVm;


    public ObservableField<String> comment = new ObservableField<>();

    public CommentsViewModel(final String postId, final HelperFactory helperFactory, final MessageHelper messageHelper, final Navigator navigator, final ConnectUiHelper uiHelper, final CommentFragment.UiHelper fragmentUiHelper) {

        commentsVmObservable = apiService.getComments(postId)
                .map(new Function<CommentListResp, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(CommentListResp resp) throws Exception {
                        List<ViewModel> results = new ArrayList<>();
                        for (final CommentListResp.Snippet elem : resp.getData()) {
                            results.add(new CommentsItemViewModel(postId,elem, navigator,uiHelper));
                        }
                        return results;
                    }
                });

        commentsVm = PublishSubject.create();
        initSubscription();
        onSendClicked = new Action() {
            @Override
            public void run() throws Exception {

                if (!getLoggedIn()) {

                    Bundle data =new Bundle();
                    data.putString("backStackActivity", ConnectHomeActivity.class.getSimpleName());
                    messageHelper.showLoginRequireDialog("Please login before comment",data);
                    return;
                }

                if (comment.get().length() < 1) {
                    messageHelper.show("comment must be more than 5 characters");
                    return;
                }
                apiService.commentReply(postId, "", comment.get()).subscribe(new Consumer<CommentReplyResp>() {
                    @Override
                    public void accept(@NonNull CommentReplyResp resp) throws Exception {
                        if ("1".equals(resp.getResCode())) {
                            initSubscription();
                            comment.set("");
                            fragmentUiHelper.scrollToEnd();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                });
//                navigator.launchPlaceSearchIntent(PLACE_AUTOCOMPLETE_REQUEST_CODE);
            }
        };

    }

    public void initSubscription() {
        commentsVmObservable.subscribe(new Consumer<List<ViewModel>>() {
            @Override
            public void accept(@NonNull List<ViewModel> viewModels) throws Exception {
                commentsVm.onNext(viewModels);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {

            }
        });
    }

}
