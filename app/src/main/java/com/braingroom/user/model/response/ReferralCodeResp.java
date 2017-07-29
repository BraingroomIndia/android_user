package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Created by godara on 29/07/17.
 */
@Getter
public class ReferralCodeResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    public static class Snippet {
        @SerializedName("referal_code")
        String referalCode;
    }
}
