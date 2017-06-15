package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
@EqualsAndHashCode(callSuper = false)
public class GiftcardResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class DataSnippet {

        @SerializedName("id")
        public String id;

        @SerializedName("title")
        public String cardName;

        @SerializedName("gift_image")
        public String cardImage;

//        @SerializedName("card_type")
//        public String cardType;
//

    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class Snippet {

        @SerializedName("Individual")
        List<DataSnippet> individual;

        @SerializedName("Corporate")
        List<DataSnippet> corporate;

        @SerializedName("NGO")
        List<DataSnippet> ngo;

    }
}
