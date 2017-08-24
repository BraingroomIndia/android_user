package com.braingroom.user.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.braingroom.user.R;
import com.braingroom.user.databinding.ActivityMessageThreadBinding;
import com.braingroom.user.viewmodel.MessagesThreadViewModel;
import com.braingroom.user.viewmodel.ViewModel;

public class MessagesThreadActivity extends BaseActivity {

    public interface UiHelper {
        void scrollToEnd();

        void scrollToTop();
    }

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String temp = "new";
        recyclerView = ((ActivityMessageThreadBinding) binding).recyclerview;

    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        String senserId = getIntentString("sender_id");
        String senderName = getIntentString("sender_name");
        getSupportActionBar().setTitle(senderName);
        return new MessagesThreadViewModel(senserId, getMessageHelper(), getNavigator(), new UiHelper() {
            @Override
            public void scrollToEnd() {
                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount()-1 );
            }

            @Override
            public void scrollToTop() {
                recyclerView.scrollToPosition(0);
            }
        });

    }

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_thread;
    }
}
