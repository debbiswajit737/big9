package com.epaymark.big9.data.model.sendData.onBoading

data class BankDetails(
    val beneficiaryName: String?,
    val accountNumber: String?,
    val confirmAccountNumber: String?,
    val ifscCode: String?,
    val employeeCode: String?,
    val cancleCheckBase64: String?,
    val bankName: String?
)
