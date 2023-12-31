package com.big9.app.data.model.profile

import com.google.gson.annotations.SerializedName

class profileResponse {
    @SerializedName("Description")
    var Description: String? = null
    @SerializedName("response_code")
    var responseCode: String? = null
    @SerializedName("userid")
    var userid: String? = null
    @SerializedName("data")
    var data: Data? = Data()
    @SerializedName("timestamp")
    var timestamp: String? = null


}

data class Data(



@SerializedName("ID")
var ID: String? = null,
@SerializedName("name")
var name: String? = null,
@SerializedName("mobileNo")
var mobileNo: String? = null,
@SerializedName("AlternateNumber")
var AlternateNumber: String? = null,
@SerializedName("email_id")
var emailId: String? = null,
@SerializedName("address")
var address: String? = null,
@SerializedName("gender")
var gender: String? = null,
@SerializedName("pincode")
var pincode: String? = null,
@SerializedName("dob")
var dob: String? = null,
@SerializedName("userType")
var userType: String? = null,
@SerializedName("kyc_status")
var kycStatus: String? = null,
@SerializedName("aeps_kyc_status")
var aepsKycStatus: String? = null,
@SerializedName("kyc_step")
var kycStep: String? = null,
@SerializedName("curr_balance")
var currBalance: String? = null,
@SerializedName("payout_balance")
var payoutBalance: String? = null,
@SerializedName("payabhi_wallet")
var payabhiWallet: String? = null,
@SerializedName("users_status")
var usersStatus: String? = null,
@SerializedName("userold_status")
var useroldStatus: String? = null,
@SerializedName("credo_onboarding_flag")
var credoOnboardingFlag: String? = null,
@SerializedName("matm_onbording1")
var matmOnbording1: String? = null,
@SerializedName("matm_onbording2")
var matmOnbording2: String? = null,
@SerializedName("franid")
var franid: String? = null,
@SerializedName("dealid")
var dealid: String? = null,
@SerializedName("distid")
var distid: String? = null,
@SerializedName("PanImageName")
var PanImageName: String? = null,
@SerializedName("PanImageData")
var PanImageData: String? = null,
@SerializedName("AadharFrontImageName")
var AadharFrontImageName: String? = null,
@SerializedName("AadharFrontImageData")
var AadharFrontImageData: String? = null,
@SerializedName("AadharBackImageName")
var AadharBackImageName: String? = null,
@SerializedName("AadharBackImageData")
var AadharBackImageData: String? = null,
@SerializedName("SelfieImageName")
var SelfieImageName: String? = null,
@SerializedName("SelfieImageData")
var SelfieImageData: String? = null

)



