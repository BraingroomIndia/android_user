package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/*
 * Created by apple on 18/03/18.
 */

public class BookedSessionResp extends BaseResp {


    @Getter
    @SerializedName("braingroom")
    private List<Data> braingroom = null;

    public List<Session> getSession() {
        if (braingroom != null && !braingroom.isEmpty() && braingroom.get(0).sessions != null) {
            for (Session session : braingroom.get(0).sessions)
                session.txnId = braingroom.get(0).txnId;

            return braingroom.get(0).sessions;
        }
        return new ArrayList<>();
    }

    @Getter
    public static class Session {

        @SerializedName("session_id")
        private String sessionId;
        @SerializedName("session_no")
        private String sessionNo;
        @SerializedName("session_name")
        private String sessionName;
        private String txnId;


    }

    private class Data {

        @SerializedName("session_details")
        private List<Session> sessions = null;
        @Getter
        @SerializedName("txn_id")
        private String txnId;
        @SerializedName("id")
        private String classId;

    }
}
