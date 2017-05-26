package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;

import com.braingroom.user.model.response.ChatListResp;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.MessagesThreadActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

public class MessagesThreadViewModel extends ViewModel {

    public final Action onSendClicked;
    public Observable<List<ViewModel>> messagesVm;

    public ObservableField<String> reply = new ObservableField<>();

    public MessagesThreadViewModel(final String senderId, final MessageHelper messageHelper, final Navigator navigator, MessagesThreadActivity.UiHelper uiHelper) {
        final String myUserId = "1";//pref.getString(Constants.BG_ID,"0");
        messagesVm = apiService.getChatMessages(senderId)
                .map(new Function<ChatListResp, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(ChatListResp resp) throws Exception {
                        List<ViewModel> results = new ArrayList<>();
                        for (final ChatListResp.Snippet elem : resp.getData()) {
                            results.add(new MessagesThreadItemViewModel(myUserId.equals(elem.getUserId()), elem.getText(), elem.getTime()));
                        }
                        return results;
                    }
                });

        onSendClicked = new Action() {
            @Override
            public void run() throws Exception {
            }
        };

    }

}
