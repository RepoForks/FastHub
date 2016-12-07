package com.fastaccess.data.dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kosh on 09 Nov 2016, 11:28 PM
 */

public class AccessTokenModel {

    @SerializedName("access_token") private String accessToken;
    @SerializedName("token_type") private String tokenType;

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return "bearer";
    }

    @Override public String toString() {
        return "AccessTokenModel{" +
                "accessToken='" + accessToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                '}';
    }
}
