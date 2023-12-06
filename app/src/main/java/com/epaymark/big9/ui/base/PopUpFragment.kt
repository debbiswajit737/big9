package com.epaymark.big9.ui.base

import android.R
import android.app.Dialog
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.DialogFragment


open class PopUpFragment: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        // Set the window animation
        dialog.window?.attributes?.windowAnimations = R.style.Animation_Dialog

        return dialog
    }
    override fun getTheme(): Int {
        return R.style.SheetDialog
    }
    fun ImageView.back(){
        this.setOnClickListener{
            dismiss()
        }
    }
}