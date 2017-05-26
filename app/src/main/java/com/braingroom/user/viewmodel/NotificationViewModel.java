package com.braingroom.user.viewmodel;

import com.braingroom.user.model.response.MessageListResp;
import com.braingroom.user.model.response.NotificationListResp;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class NotificationViewModel extends ViewModel {

    public final Observable<List<ViewModel>> notificationsVm;

    public NotificationViewModel(final HelperFactory helperFactory, final MessageHelper messageHelper, final Navigator navigator) {

        notificationsVm = apiService.getNotifications()
                .map(new Function<NotificationListResp, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(NotificationListResp resp) throws Exception {
                        List<ViewModel> results = new ArrayList<>();
                        for (final NotificationListResp.Snippet elem : resp.getData()) {
                            results.add(new NotificationsItemViewModel(navigator, elem.getDescription(), elem.getPostId(),
                                    "", "1".equals(elem.getStatus())));
                        }
                        return results;
                    }
                });
    }

}
