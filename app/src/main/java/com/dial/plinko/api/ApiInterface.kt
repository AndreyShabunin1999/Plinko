package com.dial.plinko.api

import com.dial.plinko.model.InfoUrl
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiInterface {

    @FormUrlEncoded
    @POST("/splash.php")
    suspend fun setInfoUser(@Field("phone_name") phoneName: String, @Field("locale") locale: String,
                             @Field("unique") unique: String) : Response<InfoUrl>
}
