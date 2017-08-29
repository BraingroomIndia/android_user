package com.braingroom.user.viewmodel;

import com.braingroom.user.R;
import com.braingroom.user.model.request.ChangeNotificationStatusReq;
import com.braingroom.user.model.response.MessageListResp;
import com.braingroom.user.model.response.NotificationListResp;
import com.braingroom.user.utils.Constants;
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
                        if (resp.getData().isEmpty()) {
                            results.add(new EmptyItemViewModel(R.drawable.ic_notifications_none_black_48dp, null, "No Notification", null));
                            return results;
                        }
                        for (final NotificationListResp.Snippet elem : resp.getData()) {
                            results.add(new NotificationsItemViewModel(navigator, elem.getNotificationId(), elem.getDescription(), elem.getPostId(),
                                    "", "1".equals(elem.getStatus())));
                        }
                        if (resp.getResCode().equals("1"))
                            apiService.changeNotificationStatus("").subscribe();
                        return results;
                    }
                });
    }

}
