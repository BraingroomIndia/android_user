package com.braingroom.user.viewmodel;

import android.os.Bundle;

import com.braingroom.user.R;
import com.braingroom.user.model.response.BookedSessionResp;
import com.braingroom.user.view.FullScreenVideoActivity;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/*
 * Created by apple on 19/03/18.
 */

public class OnlineClassVideoListViewModel extends ViewModel {

    public final Observable<List<ViewModel>> items;


    public OnlineClassVideoListViewModel(final Navigator navigator, final MessageHelper messageHelper, String classId) {
        this.items = apiService.getOnlineVideoList(classId).doOnSubscribe(disposable -> messageHelper.showProgressDialog("Wait", "loading")).map(sessions -> {
            List<ViewModel> textIconViewModelList = new ArrayList<>();
            for (BookedSessionResp.Session session : sessions) {
                textIconViewModelList.add(new IconTextItemViewModel(R.drawable.ic_no_thumb_110dp, "", session.getSessionName(), var1 -> apiService.getCDNVideoUrl(session.getTxnId(), session.getSessionId()).doOnSubscribe(disposable -> messageHelper.showProgressDialog("Wait", "Fetching video from server")).doFinally(messageHelper::dismissActiveProgress).subscribe(s -> {
                    if (s != null && !s.isEmpty()) {
                        Bundle data = new Bundle();
                        data.putString("video_url", s);
                        navigator.navigateActivity(FullScreenVideoActivity.class, data);
                    } else messageHelper.show("Video not Found");
                })));
            }
            if (textIconViewModelList.isEmpty())
                textIconViewModelList.add(new EmptyItemViewModel(R.drawable.ic_no_post_64dp, "", "No Video", () -> {
                }));
            return textIconViewModelList;
        }).doFinally(messageHelper::dismissActiveProgress);
    }

}
