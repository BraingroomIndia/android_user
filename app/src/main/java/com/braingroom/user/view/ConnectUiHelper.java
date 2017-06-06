package com.braingroom.user.view;

public interface ConnectUiHelper {


    void openCommentsFragment(String postId);

    void openReplyFragment(String postId, String commentId);

    void openLikesFragment(String postId, String commentId, String replyId);
    void openAcceptedUsersFragment(String postId);

    void openConnectPost();

    void popFragment();
}
