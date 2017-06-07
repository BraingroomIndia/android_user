package com.braingroom.user.viewmodel;

import com.braingroom.user.model.response.CompetitionStatusResp;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.SignUpActivityCompetition;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


/**
 * Created by himan on 6/7/2017.
 */

public class CompetitionDialogViewModel extends CustomDialogViewModel {
    public final Action onContinue, onDissmis;

    public CompetitionDialogViewModel(@NonNull final Navigator navigator) {
        onContinue = new Action() {
            @Override
            public void run() throws Exception {
                navigator.navigateActivity(SignUpActivityCompetition.class,null);
            }
        };
        onDissmis = new Action() {
            @Override
            public void run() throws Exception {
                dismissDialog();
            }
        };

        apiService.getCompetitionStatus().subscribe(new Consumer<CompetitionStatusResp>() {
            @Override
            public void accept(@NonNull CompetitionStatusResp resp) throws Exception {
                if (resp.getData().get(0).getStatus()==0)
                    dismissDialog();

            }
        });
    }

}