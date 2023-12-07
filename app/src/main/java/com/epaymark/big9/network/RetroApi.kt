package com.epaymark.big9.network

import com.epaymark.big9.data.model.sample.Test
import com.epaymark.big9.data.genericmodel.BaseResponse
import com.epaymark.big9.data.model.login.LoginResponse
import com.epaymark.big9.data.model.onBoading.DocumentUploadModel
import com.epaymark.big9.data.model.onBoading.RegForm
import com.epaymark.big9.data.model.otp.OtpResponse
import com.epaymark.big9.data.model.profile.profileResponse
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface RetroApi {

    /*{ "response": { "data": [ { "name":"Test User 1" }, { "name":"Test User 2" } ], "status": { "msg": "Sample message.", "action_status": false }, "publish": { "version": "Api.0.0.0", "developer": "bdas" } } }*/
    @POST("abcd.php")
    suspend fun login(@Body loginRequest: JsonObject): Response<BaseResponse<Test>>//ResponseState<Test>

    @POST("form.php")
    suspend fun formReg(@Body regModel: RegForm): Response<BaseResponse<Test>>//ResponseState<Test>
//
    @POST("form_doc.php")
        suspend fun docUpload(@Body documentUploadModel: DocumentUploadModel): Response<BaseResponse<Test>>

       /* @POST("auth")
        suspend fun epayLogin(@Body authData: String): Response<BaseResponse<Test>>*/

    @FormUrlEncoded
    @POST("auth")
    suspend fun epayLogin(
        @Header("Authorize") header: String,
        @Field("authData") authData: String): Response<BaseResponse<LoginResponse>>


    @POST("otpverify")
    suspend fun otpverify(
        @Header("Authtoken") token: String,
        @Body data: String): Response<OtpResponse>

    @POST("https://big9.payabhi.net/restapi/v1/users/profile")
    suspend fun profile(
        @Header("Authtoken") token: String,
        @Body data: String): Response<profileResponse>

    @POST("https://big9.payabhi.net/restapi/v1/users/profile")
    suspend fun testing(
        @Header("Authtoken") token: String,
        @Body data: String): Response<OtpResponse>
}