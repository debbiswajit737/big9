package com.epaymark.big9.data.model.sendData.onBoading

data class KycDetails(
    val businessType: String?,
    val businessCategory: String?,
    val partnerNameName: String?,
    val businessName: String?,
    val businessAddress: String?,
    val companyPanCardNumber: String?,
    val partnerPanCardNumber: String?,
    val kycAadharNumber: String?,
    val kycGSTNumber: String?
)
