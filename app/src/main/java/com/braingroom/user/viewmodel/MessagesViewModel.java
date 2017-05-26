package com.braingroom.user.viewmodel;

import com.braingroom.user.model.response.MessageListResp;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class MessagesViewModel extends ViewModel {

    public final Observable<List<ViewModel>> messagesVm;

    public MessagesViewModel(final HelperFactory helperFactory, final MessageHelper messageHelper, final Navigator navigator) {

        messagesVm = apiService.getMessages()
                .map(new Function<MessageListResp, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(MessageListResp resp) throws Exception {
                        List<ViewModel> results = new ArrayList<>();
                        for (final MessageListResp.Snippet elem : resp.getData()) {
                            results.add(new MessagesItemViewModel(navigator, elem.getSenderId(), elem.getSenderName(),
                                    elem.getSenderPic(), elem.getMessage().getMessage(), elem.getMessage().getModifyDate()));
                        }
                        return results;
                    }
                });
    }

}
