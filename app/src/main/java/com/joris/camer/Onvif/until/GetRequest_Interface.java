package com.joris.camer.Onvif.until;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface GetRequest_Interface {
    @POST
    @FormUrlEncoded
    Call<MonitorBean> getCall(@Url String url, @Field("secret") String secret);
}