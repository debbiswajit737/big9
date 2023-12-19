package com.epaymark.big9.network

import com.epaymark.big9.data.model.sample.Test
import com.epaymark.big9.data.genericmodel.BaseResponse

import com.epaymark.big9.data.model.AEPSReportModel
import com.epaymark.big9.data.model.ChangeUserPasswordModel
import com.epaymark.big9.data.model.ChangeUserTPINPasswordModel
import com.epaymark.big9.data.model.CheckServiceModel
import com.epaymark.big9.data.model.CreditCardSendOtpModel
import com.epaymark.big9.data.model.CreditCardVerifyOtpModel
import com.epaymark.big9.data.model.DMTReportModel
import com.epaymark.big9.data.model.DTHOperatorModel
import com.epaymark.big9.data.model.DTHTranspherModel
import com.epaymark.big9.data.model.DTHUserInfoModel
import com.epaymark.big9.data.model.EPotlyTranspherModel
import com.epaymark.big9.data.model.MatmeportModel
import com.epaymark.big9.data.model.PrePaidMobileOperatorListModel
import com.epaymark.big9.data.model.PrepaidMobolePlainModel
import com.epaymark.big9.data.model.PrepaidMoboleTranspherModel
import com.epaymark.big9.data.model.ResetTPINModel
import com.epaymark.big9.data.model.TransactionReportModel
import com.epaymark.big9.data.model.allReport.Bank_settle_reportModel
import com.epaymark.big9.data.model.allReport.Cashout_ledger_reportModel
import com.epaymark.big9.data.model.allReport.DmtReportReportModel
import com.epaymark.big9.data.model.allReport.MicroatmReportModel
import com.epaymark.big9.data.model.allReport.PostPaidMobileOperatorListModel
import com.epaymark.big9.data.model.allReport.PostPaidMobileTranspherModel
import com.epaymark.big9.data.model.allReport.TransactionReportResponse
import com.epaymark.big9.data.model.allReport.receipt.Transcation_report_receiptReportModel
import com.epaymark.big9.data.model.allReport.WalletLedgerModel
import com.epaymark.big9.data.model.allReport.WalletSettleReportModel
import com.epaymark.big9.data.model.allReport.aepsReportModel
import com.epaymark.big9.data.model.allReport.commissionReportModel
import com.epaymark.big9.data.model.allReport.complaints_reportMode
import com.epaymark.big9.data.model.allReport.loadRequestModel
import com.epaymark.big9.data.model.allReport.receipt.Dmt_report_receiptModel
import com.epaymark.big9.data.model.allReport.receipt.Microatm_report_receipt
import com.epaymark.big9.data.model.login.LoginResponse
import com.epaymark.big9.data.model.onBoading.DocumentUploadModel
import com.epaymark.big9.data.model.onBoading.RegForm
import com.epaymark.big9.data.model.otp.OtpResponse
import com.epaymark.big9.data.model.paymentReport.PaymentReportResponse
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
        @Field("authData") authData: String
    ): Response<BaseResponse<LoginResponse>>


    @POST("otpverify")
    suspend fun otpverify(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<OtpResponse>

    @POST("v1/users/profile")
    suspend fun profile2(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<profileResponse>


    @POST("https://big9.payabhi.net/restapi/v1/users/profile")
    suspend fun profile(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<profileResponse>

    @POST("https://big9.payabhi.net/restapi/v1/users/profile")
    suspend fun testing(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<OtpResponse>


    @POST("v1/reports/payment_report")
    suspend fun paymentReport(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<PaymentReportResponse>

    @POST("v1/reports/transcation_report")
    suspend fun transcationReport(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<TransactionReportResponse>

    @POST("v1/reports/dmt_report")
    suspend fun dmtReport(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<DmtReportReportModel>

    @POST("v1/reports/load_request_report")
    suspend fun loadRequestReport(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<loadRequestModel>

    @POST("v1/reports/wallet_ledger_report")
    suspend fun walletLedgerReport(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<WalletLedgerModel>

    @POST("v1/reports/aeps_report")
    suspend fun aepsReport(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<aepsReportModel>

    @POST("v1/reports/microatm_report")
    suspend fun microatmReport(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<MicroatmReportModel>

    @POST("v1/reports/commission_report")
    suspend fun commissionReport(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<commissionReportModel>

   /* @POST("v1/reports/complaints_report")
    suspend fun complaints_report(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<complaints_reportMode>*/

    @POST("v1/reports/complaints_report")
    suspend fun complaints_report(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<complaints_reportMode>

    @POST("v1/reports/wallet_settle_report")
    suspend fun walletSettleReport(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<WalletSettleReportModel>

    @POST("v1/reports/bank_settle_report")
    suspend fun bank_settle_report(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<Bank_settle_reportModel>

    @POST("v1/reports/cashout_ledger_report")
    suspend fun cashout_ledger_report(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<Cashout_ledger_reportModel>

    @POST("v1/reports/transcation_report_receipt")
    suspend fun transcation_report_receipt(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<Transcation_report_receiptReportModel>

    @POST("v1/reports/dmt_report_receipt")
    suspend fun dmt_report_receipt(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<Dmt_report_receiptModel>

    @POST("v1/reports/microatm_report_receipt")
    suspend fun microatm_report_receipt(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<Microatm_report_receipt>

    @POST("v1/reports/aeps_report_receipt")
    suspend fun aeps_report_receipt(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<Dmt_report_receiptModel>

    @POST("v1/services/operatorlist")
    suspend fun MobilePostPaidOperatorList(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<PostPaidMobileOperatorListModel>

    @POST("v1/services/mobile/post_transfer")
    suspend fun PostPaidMobileTranspher(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<PostPaidMobileTranspherModel>


    @POST("v1/services/operatorlist")
    suspend fun MobilePrePaidOperatorList(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<PrePaidMobileOperatorListModel>

    @POST("v1/services/mrcplan")
    suspend fun MobilePrePaidPlainList(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<PrepaidMobolePlainModel>



    @POST("v1/services/mobile/pre_transfer")
    suspend fun MobilePrePaidpreTransfer(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<PrepaidMoboleTranspherModel>


    @POST("v1/services/creditcard/send_otp")
    suspend fun creditCardSendOtp(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<CreditCardSendOtpModel>


    @POST("v1/services/creditcard/verify_otp")
    suspend fun creditCardverifyOtp(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<CreditCardVerifyOtpModel>



    @POST("v1/services/epotly/epotly_transfer")
    suspend fun epotlyTransfer(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<EPotlyTranspherModel>

    @POST("v1/services/dth/transfer")
    suspend fun dthTransfer(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<DTHTranspherModel>



    @POST("v1/services/dth/info")
    suspend fun dthUserInfo(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<DTHUserInfoModel>



    @POST("v1/password/change_pin")
    suspend fun changePin(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<ChangeUserPasswordModel>

    @POST("v1/password/change_tpin")
    suspend fun changeTpin(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<ChangeUserTPINPasswordModel>

    @POST("v1/reports/transcation_report_receipt")
    suspend fun transcationReportReceipt(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<TransactionReportModel>

    @POST("v1/reports/dmt_report_receipt")
    suspend fun dmtReportReceipt(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<DMTReportModel>

    @POST("v1/reports/aeps_report_receipt")
    suspend fun aepsReportReceipt(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<AEPSReportModel>

    @POST("v1/check-service")
    suspend fun checkService(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<CheckServiceModel>

    @POST("v1/reports/microatm_report_receipt")
    suspend fun matmREportRECEPT(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<MatmeportModel>


    @POST("v1/password/reset_tpin")
    suspend fun resetTPIN(
        @Header("Authtoken") token: String,
        @Body data: String
    ): Response<ResetTPINModel>






}