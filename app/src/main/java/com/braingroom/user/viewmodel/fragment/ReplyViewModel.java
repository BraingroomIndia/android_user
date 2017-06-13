package com.braingroom.user.viewmodel.fragment;

import android.databinding.ObservableField;
import android.os.Bundle;

import com.braingroom.user.model.request.CommentViewReply;
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

public class ReplyViewModel extends ViewModel {

    public final Action onSendClicked, onBackClicked;
    public final Observable<List<ViewModel>> repliesVmObservable;
    public final PublishSubject<List<ViewModel>> repliesVm;
    public ObservableField<String> reply = new ObservableField<>();

    public ReplyViewModel(final String postId, final String commentId, final HelperFactory helperFactory, final MessageHelper messageHelper, final Navigator navigator, final ConnectUiHelper uiHelper, final CommentFragment.UiHelper fragmentUiHelper) {

        repliesVmObservable=apiService.getReplies(commentId).map(new Function<CommentViewReply, List<ViewModel>>() {
            @Override
            public List<ViewModel> apply(@NonNull CommentViewReply resp) throws Exception {
                List<ViewModel> results = new ArrayList<>();
                for (CommentViewReply.Snippet elem:resp.getData())
                    results.add(new ReplyItemViewModel(postId,commentId,elem,navigator,uiHelper));
                return results;
            }
        });
        repliesVm = PublishSubject.create();
        initSubscription();
        onSendClicked = new Action() {
            @Override
            public void run() throws Exception {
                if (!loggedIn.get()) {

                    Bundle data =new Bundle();
                    data.putString("backStackActivity", ConnectHomeActivity.class.getSimpleName());
                    messageHelper.showLoginRequireDialog("Please login before reply",data);
                    return;
                }

                if (reply.get().length() < 5) {
                    messageHelper.show("comment must be more than 5 characters");
                    return;
                }
                apiService.commentReply(postId, commentId, reply.get()).subscribe(new Consumer<CommentReplyResp>() {
                    @Override
                    public void accept(@NonNull CommentReplyResp resp) throws Exception {
                        if ("1".equals(resp.getResCode())) {
//
                            initSubscription();
                            reply.set("");
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

        onBackClicked = new Action() {
            @Override
            public void run() throws Exception {
                uiHelper.popFragment();
//                navigator.launchPlaceSearchIntent(PLACE_AUTOCOMPLETE_REQUEST_CODE);
            }
        };



    }
    public void initSubscription() {
        repliesVmObservable.subscribe(new Consumer<List<ViewModel>>() {
            @Override
            public void accept(@NonNull List<ViewModel> viewModels) throws Exception {
                repliesVm.onNext(viewModels);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {

            }
        });
    }

}
