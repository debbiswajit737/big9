package com.epaymark.big9.data.viewMovel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.epaymark.big9.R

import com.epaymark.big9.data.genericmodel.BaseResponse
import com.epaymark.big9.data.model.CreditCardSendOtpModel
import com.epaymark.big9.data.model.CreditCardVerifyOtpModel
import com.epaymark.big9.data.model.DTHOperatorModel
import com.epaymark.big9.data.model.DTHTranspherModel
import com.epaymark.big9.data.model.DTHUserInfoModel
import com.epaymark.big9.data.model.EPotlyTranspherModel
import com.epaymark.big9.data.model.PrePaidMobileOperatorListModel
import com.epaymark.big9.data.model.PrepaidMobolePlainModel
import com.epaymark.big9.data.model.PrepaidMoboleTranspherModel
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
import com.epaymark.big9.data.model.otp.OtpResponse
import com.epaymark.big9.data.model.paymentReport.PaymentReportResponse
import com.epaymark.big9.data.model.profile.profileResponse
import com.epaymark.big9.network.ResponseState
import com.epaymark.big9.repository.AuthRepositoryRepository
import com.epaymark.big9.repository.TableRepository

import com.epaymark.big9.utils.helpers.helper.validate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DTHViewModel @Inject constructor(private val repository: AuthRepositoryRepository, private val tableRepository: TableRepository) : ViewModel() {
    //DTH Operator List
    val dthTransferResponseLiveData: MutableLiveData<ResponseState<DTHTranspherModel>>
        get() = repository.dthTransferResponseLiveData
    fun dthTransfer(token: String, data: String) {
        viewModelScope.launch {
            repository.dthTransfer(token,data)
        }
    }

    //DTH Operator List
    val dthUserInfoResponseLiveData: LiveData<ResponseState<DTHUserInfoModel>>
        get() = repository.dthUserInfoResponseLiveData
    fun dthUserInfo(token: String, data: String) {
        viewModelScope.launch {
            repository.dthUserInfo(token,data)
        }
    }




}