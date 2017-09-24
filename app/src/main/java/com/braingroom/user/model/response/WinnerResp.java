package com.braingroom.user.model.response;

import android.text.Html;
import android.text.Spanned;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by godara on 14/09/17.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
@Getter
public class WinnerResp extends BaseResp {


    @SerializedName("braingroom")
    List<Snippet> data;

    @AllArgsConstructor(suppressConstructorProperties = true)
    @Getter
    public static class Snippet {

        @SerializedName("user_id")
        private String userId;

        @SerializedName("user_name")
        private String userName;

        @SerializedName("user_image")
        private String userImage;

        @SerializedName("user_college")
        private String userCollege;

        @SerializedName("prize_rank")
        private String prizeRank;

        @SerializedName("prize_text")
        private String prizeText;

        public Spanned getPrizeRank(){
            return Html.fromHtml(prizeRank);
        }

    }




}
