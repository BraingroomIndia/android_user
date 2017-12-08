package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.text.TextUtils;

import com.braingroom.user.model.request.MessageReplyReq;
import com.braingroom.user.model.response.BaseResp;
import com.braingroom.user.model.response.ChatListResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.MessagesThreadActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

public class MessagesThreadViewModel extends ViewModel {

    public final Action onSendClicked;
    public Observable<List<ViewModel>> messageVmObservable;
    public final PublishSubject<List<ViewModel>> messagesVm;

    public ObservableField<String> reply = new ObservableField<>();

    public MessagesThreadViewModel(final String senderId, final MessageHelper messageHelper, final Navigator navigator, final MessagesThreadActivity.UiHelper uiHelper) {
        final String myUserId = pref.getString(Constants.BG_ID, "");
        apiService.changeMessageThreadStatus(senderId).observeOn(AndroidSchedulers.mainThread()).subscribe();
        messageVmObservable = apiService.getChatMessages(senderId)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<ChatListResp, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(ChatListResp resp) throws Exception {
                        List<ViewModel> results = new ArrayList<>();
                        for (final ChatListResp.Snippet elem : resp.getData()) {
                            elem.setTime(TextUtils.isEmpty(elem.getTime()) ? "" : elem.getTime());
                            results.add(new MessagesThreadItemViewModel(myUserId.equals(elem.getUserId()), elem.getText(), elem.getTime()));
                        }
                        return results;
                    }
                });

        messagesVm = PublishSubject.create();
        initSubscription();

        onSendClicked = new Action() {
            @Override
            public void run() throws Exception {
                if (reply.get().trim().isEmpty()) {
                    messageHelper.show("Can't send empty message");
                    reply.set("");
                    return;
                }
                MessageReplyReq.Snippet snippet = new MessageReplyReq.Snippet();
                snippet.setSenderId(myUserId);
                snippet.setMessageType("");
                snippet.setQuoteId("");
                snippet.setStatus("");
                snippet.setMessage(reply.get());
                snippet.setRecieverId(senderId);
                apiService.postMessage(snippet).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BaseResp>() {
                    @Override
                    public void accept(@NonNull BaseResp resp) throws Exception {
                        if (resp.getResCode().equals("1")) {
                            initSubscription();
                            reply.set("");
                        }

                    }
                });
                uiHelper.scrollToEnd();
            }
        };

    }

    public void initSubscription() {
        messageVmObservable.subscribe(new Consumer<List<ViewModel>>() {
            @Override
            public void accept(@NonNull List<ViewModel> viewModels) throws Exception {
                messagesVm.onNext(viewModels);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {

            }
        });
    }

}
