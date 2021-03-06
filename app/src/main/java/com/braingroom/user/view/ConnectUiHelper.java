package com.braingroom.user.view;

import android.databinding.ObservableField;


import java.util.List;

public interface ConnectUiHelper {


    void openCommentsFragment(String postId);

    void openReplyFragment(String postId, String commentId);

    void openLikesFragment(String postId, String commentId, String replyId);

    void openAcceptedUsersFragment(String postId);

    void setFilterData(String keyword, String categoryId, String segmentId, String myGroupId, String allGroupId,
                       String instituteId, String authorId, List<String> location);

    void setSearchQuery(String searchQuery);

    void openConnectPost();

    void popFragment();

    void setCount(int notificationCount, int messageCount);

    void retry();

    void openFollowers();

    void openFollowing();

    void openFilter();
}
