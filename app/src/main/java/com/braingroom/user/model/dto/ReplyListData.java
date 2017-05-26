package com.braingroom.user.model.dto;

import com.braingroom.user.model.response.CommentListResp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Created by agrahari on 09/04/17.
 */
@Data
public class ReplyListData implements Serializable {

    List<ReplyData> replyDataList;

    public ReplyListData(List<CommentListResp.Reply> replies) {
        replyDataList = new ArrayList<>();
        for (CommentListResp.Reply reply : replies) {
            replyDataList.add(new ReplyData(reply.getReplyId(), reply.getUserName(), reply.getUserImage(), reply.getReply()
                    , reply.getReplyDate(), reply.getNumLikes(), reply.getLiked()));
        }
    }
}
