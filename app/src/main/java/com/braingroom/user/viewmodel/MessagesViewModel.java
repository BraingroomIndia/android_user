package com.braingroom.user.viewmodel;

import com.braingroom.user.R;
import com.braingroom.user.model.response.MessageListResp;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

public class MessagesViewModel extends ViewModel {

    public final Observable<List<ViewModel>> messagesVm;

    public MessagesViewModel(final HelperFactory helperFactory, final MessageHelper messageHelper, final Navigator navigator) {
        messageHelper.showProgressDialog("Wait", "loading");

        messagesVm = apiService.getMessages()
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<MessageListResp, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(MessageListResp resp) throws Exception {
                        List<ViewModel> results = new ArrayList<>();
                        if (resp.getData().isEmpty())
                            results.add(new EmptyItemViewModel(R.drawable.ic_message_black_48dp, null, "No Message", null));
                        for (final MessageListResp.Snippet elem : resp.getData()) {
                            results.add(new MessagesItemViewModel(navigator, elem.getMessage().getMessageType(), elem.getMessage().getQuoteId(), elem.getSenderId(), elem.getSenderName(),
                                    elem.getSenderPic(), elem.getMessage().getMessage(), getHumanDate(elem.getMessage().getModifyDate())));
                        }
                        messageHelper.dismissActiveProgress();
                        return results;
                    }
                });
    }


    private String getHumanDate(String timeStamp) {

        try {
            long time = Integer.valueOf(timeStamp);
            DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date netDate = (new Date(time * 1000));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }

}



