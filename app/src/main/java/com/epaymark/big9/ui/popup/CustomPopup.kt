package com.epaymark.epay.ui.popup

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.epaymark.epay.databinding.PopupBalenceBinding
import com.epaymark.epay.databinding.PopupDebitAlertBinding
import com.epaymark.epay.databinding.PopupDthUserDetailsBinding
import com.epaymark.epay.utils.`interface`.CallBack


object CustomPopup {

    fun showBindingPopup(context: Context/*, userInfoModel: UserInfoModel*/) {
        val binding = PopupDthUserDetailsBinding.inflate(LayoutInflater.from(context))

        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setView(binding.root)
        //binding.model=userInfoModel


        /*alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }*/

        val alertDialog = alertDialogBuilder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        binding.imgClose.setOnClickListener {
            alertDialog.dismiss()
        }
    }
    fun showDebitPopup(
        context: Context,/*, userInfoModel: UserInfoModel*/
        callBack: CallBack
    ) {
        val binding = PopupDebitAlertBinding.inflate(LayoutInflater.from(context))
        //popup_debit_alert
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setView(binding.root)
        //binding.model=userInfoModel


        /*alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }*/

        val alertDialog = alertDialogBuilder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        binding.btnOk.setOnClickListener {
            alertDialog.dismiss()
            callBack.getValue("ok")
        }
        alertDialog.show()
    }



    fun showBalencePopup(context: Context) {
        val binding = PopupBalenceBinding.inflate(LayoutInflater.from(context))
        //popup_balence
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setView(binding.root)
        //binding.model=userInfoModel


        /*alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }*/

        val alertDialog = alertDialogBuilder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        binding.imgClose.setOnClickListener {
            alertDialog.dismiss()
        }
    }


}