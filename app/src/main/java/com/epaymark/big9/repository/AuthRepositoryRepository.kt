package com.epaymark.big9.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.epaymark.big9.data.genericmodel.BaseResponse
import com.epaymark.big9.data.model.AEPSReportData
import com.epaymark.big9.data.model.AEPSReportModel
import com.epaymark.big9.data.model.ChangeUserPasswordModel
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
import com.epaymark.big9.data.model.allReport.receipt.Microatm_report_receipt
import com.epaymark.big9.data.model.login.LoginResponse
import com.epaymark.big9.data.model.onBoading.DocumentUploadModel
import com.epaymark.big9.data.model.onBoading.RegForm
import com.epaymark.big9.data.model.otp.OtpResponse
import com.epaymark.big9.data.model.paymentReport.PaymentReportResponse
import com.epaymark.big9.data.model.profile.profileResponse
import com.epaymark.big9.data.model.sample.Test
import com.epaymark.big9.network.ResponseState
import com.epaymark.big9.network.RetroApi
import javax.inject.Inject

class AuthRepositoryRepository @Inject constructor(private val api: RetroApi) {
    private val _formResponseLiveData =
        MutableLiveData<ResponseState<BaseResponse<Test>>>()
    val formResponseLiveData: LiveData<ResponseState<BaseResponse<Test>>>
        get() = _formResponseLiveData


    suspend fun formReg(requestBody: RegForm) {
        _formResponseLiveData.postValue(ResponseState.Loading())
        try {

            val response = api.formReg(requestBody)
            _formResponseLiveData.postValue(ResponseState.create(response, "aa"))
            Log.e("TAG_error", "login:OK " + response)

        } catch (throwable: Throwable) {
            _formResponseLiveData.postValue(ResponseState.create(throwable))
            Log.e("TAG_error", "login: " + throwable.message)
        }

    }


    //Doc upload

    private val _docUploadResponseLiveData =
        MutableLiveData<ResponseState<BaseResponse<Test>>>()
    val docUploadResponseLiveData: LiveData<ResponseState<BaseResponse<Test>>>
        get() = _docUploadResponseLiveData


    suspend fun docUpload(requestBody: DocumentUploadModel) {
        _docUploadResponseLiveData.postValue(ResponseState.Loading())
        try {

            val response = api.docUpload(requestBody)
            _docUploadResponseLiveData.postValue(ResponseState.create(response, "aa"))
            //Log.e("TAG_error", "login:OK "+response)

        } catch (throwable: Throwable) {
            _docUploadResponseLiveData.postValue(ResponseState.create(throwable))
            //Log.e("TAG_error", "login: "+throwable.message)
        }

    }


    //Login Model

    private val _loginResponseLiveData =
        MutableLiveData<ResponseState<BaseResponse<LoginResponse>>>()
    val loginResponseLiveData: LiveData<ResponseState<BaseResponse<LoginResponse>>>
        get() = _loginResponseLiveData


    suspend fun userLogin(loginModel: String) {
        _loginResponseLiveData.postValue(ResponseState.Loading())
        try {

            val response =
                api.epayLogin(loginModel.replace("\n", "").replace("\r", ""), "loginModel")
            _loginResponseLiveData.postValue(ResponseState.create(response, "aa"))


        } catch (throwable: Throwable) {
            _loginResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //OTP

    private val _otpResponseLiveData =
        MutableLiveData<ResponseState<OtpResponse>>()
    val otpResponseLiveData: LiveData<ResponseState<OtpResponse>>
        get() = _otpResponseLiveData


    suspend fun otp(token: String, loginModel: String) {
        _otpResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.otpverify(token, loginModel.replace("\n", "").replace("\r", ""))
            _otpResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _otpResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //Profile2
    private val _profile2ResponseLiveData =
        MutableLiveData<ResponseState<profileResponse>>()
    val profile2ResponseLiveData: LiveData<ResponseState<profileResponse>>
        get() = _profile2ResponseLiveData


    suspend fun profile2(token: String, loginModel: String) {
        _profile2ResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.profile2(token, loginModel.replace("\n", "").replace("\r", ""))
            _profile2ResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _profile2ResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //profile

    private val _profileResponseLiveData =
        MutableLiveData<ResponseState<profileResponse>>()
    val profileResponseLiveData: LiveData<ResponseState<profileResponse>>
        get() = _profileResponseLiveData


    suspend fun profile(token: String, loginModel: String) {
        _profileResponseLiveData.postValue(ResponseState.Loading())
        try {
            var token =
                "eyJ1c2VyX2lkIjoiIiwidGltZXN0YW1wIjoxNzAxOTM5MTk2LCJyYW5kb20iOiJhN2ZjZWRjMjM1NzkxOGJlZDdjNjY2OGJjYmVhYmNhMmU4NWNhYWIwODE2ODg5ZjdhODY5YTY3MzBmNWY3Y2MzIn0="
            var data = "ZqHbVba0bT2zVD2pxkGEH9tsoSvGvl18BD8ZpvkXvtM="
            val response = api.profile(token, data.replace("\n", "").replace("\r", ""))
            _profileResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _profileResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //paymentReport
    private val _paymentReportResponseLiveData =
        MutableLiveData<ResponseState<PaymentReportResponse>>()
    val paymentReportResponseLiveData: LiveData<ResponseState<PaymentReportResponse>>
        get() = _paymentReportResponseLiveData


    suspend fun paymentReport(token: String, data: String) {
        _paymentReportResponseLiveData.postValue(ResponseState.Loading())
        try {

            val response = api.paymentReport(token, data.replace("\n", "").replace("\r", ""))
            _paymentReportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _paymentReportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //transcationReport
    private val _transcationReportResponseLiveData =
        MutableLiveData<ResponseState<TransactionReportResponse>>()
    val _ranscationReportResponseLiveData: LiveData<ResponseState<TransactionReportResponse>>
        get() = _transcationReportResponseLiveData


    suspend fun transcationReport(token: String, loginModel: String) {
        _transcationReportResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.transcationReport(token, loginModel.replace("\n", "").replace("\r", ""))
            _transcationReportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _transcationReportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //dmtReport
    private val _dmtReportResponseLiveData =
        MutableLiveData<ResponseState<DmtReportReportModel>>()
    val dmtReportResponseLiveData: LiveData<ResponseState<DmtReportReportModel>>
        get() = _dmtReportResponseLiveData


    suspend fun dmtReport(token: String, loginModel: String) {
        _dmtReportResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.dmtReport(token, loginModel.replace("\n", "").replace("\r", ""))
            _dmtReportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _dmtReportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //loadRequestReport
    private val _loadRequestReportResponseLiveData =
        MutableLiveData<ResponseState<loadRequestModel>>()
    val loadRequestReportResponseLiveData: LiveData<ResponseState<loadRequestModel>>
        get() = _loadRequestReportResponseLiveData


    suspend fun loadRequestReport(token: String, loginModel: String) {
        _loadRequestReportResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.loadRequestReport(token, loginModel.replace("\n", "").replace("\r", ""))
            _loadRequestReportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _loadRequestReportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //walletLedgerReport
    private val _walletLedgerReportResponseLiveData =
        MutableLiveData<ResponseState<WalletLedgerModel>>()
    val walletLedgerReportResponseLiveData: LiveData<ResponseState<WalletLedgerModel>>
        get() = _walletLedgerReportResponseLiveData


    suspend fun walletLedgerReport(token: String, loginModel: String) {
        _walletLedgerReportResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.walletLedgerReport(token, loginModel.replace("\n", "").replace("\r", ""))
            _walletLedgerReportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _walletLedgerReportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //aepsReport
    private val _aepsReportResponseLiveData =
        MutableLiveData<ResponseState<aepsReportModel>>()
    val aepsReportResponseLiveData: LiveData<ResponseState<aepsReportModel>>
        get() = _aepsReportResponseLiveData


    suspend fun aepsReport(token: String, loginModel: String) {
        _aepsReportResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.aepsReport(token, loginModel.replace("\n", "").replace("\r", ""))
            _aepsReportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _aepsReportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //microatmReport
    private val _microatmReportResponseLiveData =
        MutableLiveData<ResponseState<MicroatmReportModel>>()
    val microatmReportResponseLiveData: LiveData<ResponseState<MicroatmReportModel>>
        get() = _microatmReportResponseLiveData


    suspend fun microatmReport(token: String, loginModel: String) {
        _microatmReportResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.microatmReport(token, loginModel.replace("\n", "").replace("\r", ""))
            _microatmReportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _microatmReportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //commissionReport
    private val _commissionReportResponseLiveData =
        MutableLiveData<ResponseState<commissionReportModel>>()
    val commissionReportResponseLiveData: LiveData<ResponseState<commissionReportModel>>
        get() = _commissionReportResponseLiveData


    suspend fun commissionReport(token: String, loginModel: String) {
        _commissionReportResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.commissionReport(token, loginModel.replace("\n", "").replace("\r", ""))
            _commissionReportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _commissionReportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    /*//complaints_report
    private val _complaints_reportResponseLiveData =
        MutableLiveData<ResponseState<WalletSettleReportModel>>()
    val complaints_reportResponseLiveData: LiveData<ResponseState<WalletSettleReportModel>>
        get() = _complaints_reportResponseLiveData


    suspend fun complaints_report(token: String, loginModel: String) {
        _complaints_reportResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.walletSettleReportTest(token, loginModel.replace("\n", "").replace("\r", ""))
            _complaints_reportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _complaints_reportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }*/


    //Complaints
    private val _complaints_reportReportResponseLiveData =
        MutableLiveData<ResponseState<complaints_reportMode>>()
    val complaints_reportReportResponseLiveData: LiveData<ResponseState<complaints_reportMode>>
        get() = _complaints_reportReportResponseLiveData


    suspend fun complaints_report(token: String, loginModel: String) {
        _complaints_reportReportResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.complaints_report(token, loginModel.replace("\n", "").replace("\r", ""))
            _complaints_reportReportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _complaints_reportReportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //walletSettleReport
    private val _walletSettleReportResponseLiveData =
        MutableLiveData<ResponseState<WalletSettleReportModel>>()
    val walletSettleReportResponseLiveData: LiveData<ResponseState<WalletSettleReportModel>>
        get() = _walletSettleReportResponseLiveData


    suspend fun walletSettleReport(token: String, loginModel: String) {
        _walletSettleReportResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.walletSettleReport(token, loginModel.replace("\n", "").replace("\r", ""))
            _walletSettleReportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _walletSettleReportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }




    //bank_settle_report
    private val _bank_settle_reportResponseLiveData =
        MutableLiveData<ResponseState<Bank_settle_reportModel>>()
    val bank_settle_reportResponseLiveData: LiveData<ResponseState<Bank_settle_reportModel>>
        get() = _bank_settle_reportResponseLiveData


    suspend fun bank_settle_report(token: String, loginModel: String) {
        _bank_settle_reportResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.bank_settle_report(token, loginModel.replace("\n", "").replace("\r", ""))
            _bank_settle_reportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _bank_settle_reportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //cashout_ledger_report
    private val _cashout_ledger_reportResponseLiveData =
        MutableLiveData<ResponseState<Cashout_ledger_reportModel>>()
    val cashout_ledger_reportResponseLiveData: LiveData<ResponseState<Cashout_ledger_reportModel>>
        get() = _cashout_ledger_reportResponseLiveData


    suspend fun cashout_ledger_report(token: String, loginModel: String) {
        _cashout_ledger_reportResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response =
                api.cashout_ledger_report(token, loginModel.replace("\n", "").replace("\r", ""))
            _cashout_ledger_reportResponseLiveData.postValue(ResponseState.create(response, "aa"))
        } catch (throwable: Throwable) {
            _cashout_ledger_reportResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }


    //transcation_report_receipt
    private val _transcation_report_receiptResponseLiveData =
        MutableLiveData<ResponseState<Transcation_report_receiptReportModel>>()
    val transcation_report_receiptResponseLiveData: LiveData<ResponseState<Transcation_report_receiptReportModel>>
        get() = _transcation_report_receiptResponseLiveData


    suspend fun transcation_report_receipt(token: String, loginModel: String) {
        _transcation_report_receiptResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.transcation_report_receipt(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _transcation_report_receiptResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _transcation_report_receiptResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //Aeps_report_receipt
    private val _aeps_report_receiptResponseLiveData =
        MutableLiveData<ResponseState<Transcation_report_receiptReportModel>>()
    val aeps_report_receiptResponseLiveData: LiveData<ResponseState<Transcation_report_receiptReportModel>>
        get() = _aeps_report_receiptResponseLiveData


    suspend fun aeps_report_receiptResponseLiveData(token: String, loginModel: String) {
        _aeps_report_receiptResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.transcation_report_receipt(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _aeps_report_receiptResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _aeps_report_receiptResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //Dmt_report_receiptModel
    private val _dmt_report_receiptModelResponseLiveData =
        MutableLiveData<ResponseState<Transcation_report_receiptReportModel>>()
    val dmt_report_receiptModelResponseLiveData: LiveData<ResponseState<Transcation_report_receiptReportModel>>
        get() = _dmt_report_receiptModelResponseLiveData


    suspend fun dmt_report_receiptModelResponseLiveData(token: String, loginModel: String) {
        _dmt_report_receiptModelResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.transcation_report_receipt(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _dmt_report_receiptModelResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _dmt_report_receiptModelResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //Microatm_report_receipt
    private val _microatm_report_receiptResponseLiveData =
        MutableLiveData<ResponseState<Microatm_report_receipt>>()
    val microatm_report_receiptResponseLiveData: LiveData<ResponseState<Microatm_report_receipt>>
        get() = _microatm_report_receiptResponseLiveData


    suspend fun microatm_report_receiptResponseLiveData(token: String, loginModel: String) {
        _microatm_report_receiptResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.microatm_report_receipt(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _microatm_report_receiptResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _microatm_report_receiptResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //Postpaid mobile operator list
    private val _postpaid_mobile_operator_listResponseLiveData =
        MutableLiveData<ResponseState<PostPaidMobileOperatorListModel>>()
    val postpaid_mobile_operator_listResponseLiveData: LiveData<ResponseState<PostPaidMobileOperatorListModel>>
        get() = _postpaid_mobile_operator_listResponseLiveData


    suspend fun postpaid_mobile_operator_list(token: String, loginModel: String) {
        _postpaid_mobile_operator_listResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.MobilePostPaidOperatorList(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _postpaid_mobile_operator_listResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _postpaid_mobile_operator_listResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //Prepaid mobile operator list
    private val _prepaid_mobile_operator_listResponseLiveData =
        MutableLiveData<ResponseState<PrePaidMobileOperatorListModel>>()
    val prepaid_mobile_operator_listResponseLiveData: LiveData<ResponseState<PrePaidMobileOperatorListModel>>
        get() = _prepaid_mobile_operator_listResponseLiveData


    suspend fun prepaid_mobile_operator_list(token: String, loginModel: String) {
        _prepaid_mobile_operator_listResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.MobilePrePaidOperatorList(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _prepaid_mobile_operator_listResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            Log.d("TAGjsondata", "prepaid_mobile_operator_list: "+throwable.message)
            _prepaid_mobile_operator_listResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }



    //Postpaid mobile transpher
    private val _postPaidMobileTranspherResponseLiveData =
        MutableLiveData<ResponseState<PostPaidMobileTranspherModel>>()
    val postPaidMobileTranspherResponseLiveData: MutableLiveData<ResponseState<PostPaidMobileTranspherModel>>
        get() = _postPaidMobileTranspherResponseLiveData


    suspend fun PostPaidMobileTranspher(token: String, loginModel: String) {
        _postPaidMobileTranspherResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.PostPaidMobileTranspher(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _postPaidMobileTranspherResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _postPaidMobileTranspherResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }



    //Prepaid mobile transpher
    private val _prePaidMobilePlainListResponseLiveData =
        MutableLiveData<ResponseState<PrepaidMobolePlainModel>>()
    val prePaidMobilePlainListResponseLiveData: LiveData<ResponseState<PrepaidMobolePlainModel>>
        get() = _prePaidMobilePlainListResponseLiveData


    suspend fun prePaidMobilePlainList(token: String, loginModel: String) {
        _prePaidMobilePlainListResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.MobilePrePaidPlainList(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _prePaidMobilePlainListResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _prePaidMobilePlainListResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }



    //Prepaid mobile transpher
    private val _prePaidMobileTranspherResponseLiveData =
        MutableLiveData<ResponseState<PrepaidMoboleTranspherModel>>()
    val prePaidMobileTranspherResponseLiveData: MutableLiveData<ResponseState<PrepaidMoboleTranspherModel>>
        get() = _prePaidMobileTranspherResponseLiveData

    suspend fun PrePaidMobileTranspher(token: String, loginModel: String) {
        _prePaidMobileTranspherResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.MobilePrePaidpreTransfer(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _prePaidMobileTranspherResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _prePaidMobileTranspherResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //Credit card send otp
    private val _creditCardSendOtpResponseLiveData =
        MutableLiveData<ResponseState<CreditCardSendOtpModel>>()
    val creditCardSendOtpResponseLiveData: MutableLiveData<ResponseState<CreditCardSendOtpModel>>
        get() = _creditCardSendOtpResponseLiveData

    suspend fun creditSendVerifyOtp(token: String, loginModel: String) {
        _creditCardSendOtpResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.creditCardSendOtp(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _creditCardSendOtpResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _creditCardSendOtpResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //Credit card verify otp
    private val _creditCardVeryfyOTPResponseLiveData =
        MutableLiveData<ResponseState<CreditCardVerifyOtpModel>>()
    val creditCardVeryfyOTPResponseLiveData: LiveData<ResponseState<CreditCardVerifyOtpModel>>
        get() = _creditCardVeryfyOTPResponseLiveData

    suspend fun creditCardVeryfyOTP(token: String, loginModel: String) {
        _creditCardVeryfyOTPResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.creditCardverifyOtp(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _creditCardVeryfyOTPResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _creditCardVeryfyOTPResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }



    //Epotly Transpher
    private val _epotlyTranspherResponseLiveData =
        MutableLiveData<ResponseState<EPotlyTranspherModel>>()
    val epotlyTranspherResponseLiveData: LiveData<ResponseState<EPotlyTranspherModel>>
        get() = _epotlyTranspherResponseLiveData

    suspend fun epotlyTranspher(token: String, loginModel: String) {
        _epotlyTranspherResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.epotlyTransfer(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _epotlyTranspherResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _epotlyTranspherResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //DTH Operator List
    private val _dthTransferResponseLiveData =
        MutableLiveData<ResponseState<DTHTranspherModel>>()
    val dthTransferResponseLiveData: MutableLiveData<ResponseState<DTHTranspherModel>>
        get() = _dthTransferResponseLiveData

    suspend fun dthTransfer(token: String, loginModel: String) {
        _dthTransferResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.dthTransfer(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _dthTransferResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _dthTransferResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //DTH USER INFO
    private val _dthUserInfoResponseLiveData =
        MutableLiveData<ResponseState<DTHUserInfoModel>>()
    val dthUserInfoResponseLiveData: LiveData<ResponseState<DTHUserInfoModel>>
        get() = _dthUserInfoResponseLiveData

    suspend fun dthUserInfo(token: String, loginModel: String) {
        _dthUserInfoResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.dthUserInfo(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _dthUserInfoResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _dthUserInfoResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //Change Passdord
    private val _changePinResponseLiveData =
        MutableLiveData<ResponseState<ChangeUserPasswordModel>>()
    val changePinResponseLiveData: LiveData<ResponseState<ChangeUserPasswordModel>>
        get() = _changePinResponseLiveData

    suspend fun changePin(token: String, loginModel: String) {
        _changePinResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.changePin(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _changePinResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _changePinResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //Change changeTpin
    private val _changeTPinResponseLiveData =
        MutableLiveData<ResponseState<ChangeUserPasswordModel>>()
    val changeTPinResponseLiveData: LiveData<ResponseState<ChangeUserPasswordModel>>
        get() = _changeTPinResponseLiveData

    suspend fun changeTPin(token: String, loginModel: String) {
        _changeTPinResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.changePin(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _changeTPinResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _changeTPinResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //transcation Report Receipt
    private val _transcationReportReceiptResponseLiveData =
        MutableLiveData<ResponseState<TransactionReportModel>>()
    val transcationReportReceiptResponseLiveData: MutableLiveData<ResponseState<TransactionReportModel>>
        get() = _transcationReportReceiptResponseLiveData

    suspend fun transcationReportReceipt(token: String, loginModel: String) {
        _transcationReportReceiptResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.transcationReportReceipt(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _transcationReportReceiptResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _transcationReportReceiptResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //DMT Report Receipt
    private val _dmtReportReceiptResponseLiveData =
        MutableLiveData<ResponseState<DMTReportModel>>()
    val dmtReportReceiptResponseLiveData: MutableLiveData<ResponseState<DMTReportModel>>
        get() = _dmtReportReceiptResponseLiveData

    suspend fun dmtReportReceipt(token: String, loginModel: String) {
        _dmtReportReceiptResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.dmtReportReceipt(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _dmtReportReceiptResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _dmtReportReceiptResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //AEPS Report Receipt
    private val _aepsReportReceiptReceiptResponseLiveData =
        MutableLiveData<ResponseState<AEPSReportModel>>()
    val aepsReportReceiptReceiptResponseLiveData: MutableLiveData<ResponseState<AEPSReportModel>>
        get() = _aepsReportReceiptReceiptResponseLiveData

    suspend fun aepsReportReceiptReceipt(token: String, loginModel: String) {
        _aepsReportReceiptReceiptResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.aepsReportReceipt(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _aepsReportReceiptReceiptResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _aepsReportReceiptReceiptResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //Check service
    private val _checkServiceResponseLiveData =
        MutableLiveData<ResponseState<AEPSReportModel>>()
    val checkServiceResponseLiveData: MutableLiveData<ResponseState<AEPSReportModel>>
        get() = _checkServiceResponseLiveData

    suspend fun checkService(token: String, loginModel: String) {
        _checkServiceResponseLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.checkService(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _checkServiceResponseLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _checkServiceResponseLiveData.postValue(ResponseState.create(throwable))
        }

    }

    //MATM
    private val _microatm_report_receiptResponseReceptLiveData =
        MutableLiveData<ResponseState<MatmeportModel>>()
    val microatm_report_receiptResponseReceptLiveData: MutableLiveData<ResponseState<MatmeportModel>>
        get() = _microatm_report_receiptResponseReceptLiveData

    suspend fun microatmReportReceipt(token: String, loginModel: String) {
        _microatm_report_receiptResponseReceptLiveData.postValue(ResponseState.Loading())
        try {
            val response = api.matmREportRECEPT(
                token,
                loginModel.replace("\n", "").replace("\r", "")
            )
            _microatm_report_receiptResponseReceptLiveData.postValue(
                ResponseState.create(
                    response,
                    "aa"
                )
            )
        } catch (throwable: Throwable) {
            _microatm_report_receiptResponseReceptLiveData.postValue(ResponseState.create(throwable))
        }

    }




}

