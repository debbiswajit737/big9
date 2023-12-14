package com.epaymark.big9.ui.base

import android.R
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.epaymark.big9.ui.activity.DashboardActivity
import com.epaymark.big9.utils.helpers.SharedPreff
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


open class BaseCenterSheetFragment: DialogFragment() {


    override fun getTheme(): Int {
        return R.style.Theme_NoTitleBar_Fullscreen
    }
    fun ImageView.back(){
        this.setOnClickListener{
            dismiss()
        }
    }

    fun ImageView.backToHome(){
        this.setOnClickListener{
            (activity as? DashboardActivity)?.navigate()

            //findNavController().popBackStack(R.id.homeFragment2,false)
        }
    }
    fun View.takeScreenshot(): Bitmap {
        // Create a Bitmap with the same dimensions as the View
        val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)

        // Create a Canvas to draw the View onto the Bitmap
        val canvas = Canvas(bitmap)

        // Draw the View onto the Canvas
        this.draw(canvas)

        return bitmap
    }

    // Function to save the screenshot to a file
    fun saveScreenshot(bitmap: Bitmap, file: File): Boolean {
        try {
            // Create a FileOutputStream to write the Bitmap to the file
            val outputStream = FileOutputStream(file)

            // Compress the Bitmap and write it to the file
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

            // Close the FileOutputStream
            outputStream.close()

            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

}