package com.epaymark.big9.ui.base

import android.animation.ObjectAnimator
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.InputFilter
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.epaymark.big9.R
import com.epaymark.big9.utils.helpers.Constants.INPUT_FILTER_MAX_VALUE
import com.epaymark.big9.utils.helpers.Constants.INPUT_FILTER_POINTER_LENGTH
import com.epaymark.big9.utils.helpers.DecimalDigitsInputFilter
import com.epaymark.big9.utils.helpers.SharedPreff
import com.epaymark.big9.utils.`interface`.CallBack
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
open class BaseFragment: Fragment(){
    @Inject
    open lateinit var sharedPreff: SharedPreff



    fun Uri.uriToBase64(contentResolver: ContentResolver): String? {
        var inputStream: InputStream? = null
        try {
            inputStream = contentResolver.openInputStream(this)
            if (inputStream != null) {
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val byteArray = byteArrayOutputStream.toByteArray()
                return Base64.encodeToString(byteArray, Base64.DEFAULT)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun String.videoToBase64(context: Context): String? {
        val contentResolver: ContentResolver = context.contentResolver

        try {
            // Open an input stream to the content URI
            val inputStream: InputStream? = contentResolver.openInputStream(Uri.parse(this))

            if (inputStream != null) {
                // Read the video data into a byte array
                val buffer = ByteArray(1024)
                var bytesRead: Int
                val output = ByteArrayOutputStream()

                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    output.write(buffer, 0, bytesRead)
                }

                // Convert the byte array to Base64
                val videoData = output.toByteArray()
                val base64Video = Base64.encodeToString(videoData, Base64.DEFAULT)

                inputStream.close()
                output.close()
                Log.d("kol", "videoToBase64: ")
                return base64Video
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("kol", "videoToBase64: "+e.message)
        }catch (e: Exception) {
            e.printStackTrace()
            Log.d("kol", "videoToBase642: "+e.message)
        }

        return null
    }


    fun String.pdfToBase64(): String? {
        try {
            val file = File(this)
            val length = file.length().toInt()
            val bytes = ByteArray(length)

            val input = FileInputStream(file)
            input.read(bytes)
            input.close()

            val base64String = Base64.encodeToString(bytes, Base64.DEFAULT)
            return base64String
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun View.back(){
        this.setOnClickListener{
            findNavController().popBackStack()
        }

    }


    fun EditText.setupAmount() {
        /*val inputFilter = InputFilter { source, start, end, dest, dstart, dend ->
            // Define your filter logic here
            val newText = dest.toString().substring(0, dstart) + source.subSequence(start, end) + dest.toString().substring(dend)
            if (newText.matches(Regex("^\\d{0,7}(\\.\\d{0,2})?\$"))) {
                null // Accept the input
            } else {
                "" // Reject the input
            }
        }*/

        //this.filters = arrayOf(inputFilter)
        this.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(INPUT_FILTER_MAX_VALUE, INPUT_FILTER_POINTER_LENGTH))

    }
    fun View.showDatePickerDialog(callBack: CallBack) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this.context,
            R.style.MyDatePickerDialogTheme,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val selectedDate = "$year-${month + 1}-$dayOfMonth" // +1 because months are zero-based
                callBack.getValue(selectedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }
    fun String.currentdate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = dateFormat.format(calendar.time)
        return currentDate
    }


    fun ImageView.setImage(uri: Uri) {
        Glide.with(this.context)
            .load(uri)
            .into(this)
    }

    fun rotateView(view: View, degrees: Float) {
        val rotation = ObjectAnimator.ofFloat(view, "rotation", degrees)
        rotation.duration = 500 // Adjust the duration as needed
        rotation.start()
    }

    fun WebView.set(url:String,postData:String){
        this.webViewClient = MyWebViewClient()
        //val url = "https://www.gibl.in/wallet/validate2/"
        //val postData = "ret_data=eyJ1cmMiOiI5MzkxMTU1OTEwIiwidW1jIjoiNTE1ODM5IiwiYWsiOiI2NTA0MjA2MWQ4MTRhIiwiZm5hbWUiOiJzb3VteWEiLCJsbmFtZSI6InNvdW15YSIsImVtYWlsIjoiYmlnOWl0QGdtYWlsLmNvbSIsInBobm8iOiI5MjMxMTA5ODI5IiwicGluIjoiODg4ODg4In0="
        this.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
                return true
            }
        }
        this.postUrl(url, postData.toByteArray())
    }

    private inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return false
        }
    }


    fun Uri.getFileNameFromUri(): String? {
        var fileName: String? = null
        context?.let { ctx ->
            when {
                "content".equals(this.scheme, ignoreCase = true) -> {
                    // If the URI scheme is "content"
                    val cursor: Cursor? = ctx.contentResolver.query(this, null, null, null, null)
                    cursor?.use {
                        if (it.moveToFirst()) {
                            val displayNameIndex: Int =
                                it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                            if (displayNameIndex != -1) {
                                fileName = it.getString(displayNameIndex)
                            }
                        }
                    }
                }

                "file".equals(this.scheme, ignoreCase = true) -> {
                    // If the URI scheme is "file"
                    fileName = this.lastPathSegment
                }

                else -> {

                        // For other URI schemes
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                            DocumentsContract.isDocumentUri(ctx, this)
                        ) {
                            // If it's a DocumentsProvider URI
                            val docId: String = DocumentsContract.getDocumentId(this)
                            val split: List<String> = docId.split(":")
                            if (split.size > 1) {
                                val contentUri: Uri = when (split[0]) {
                                    "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                    "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                                    "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                                    else -> throw IllegalArgumentException("Unsupported URI scheme")
                                }
                                val selection: String = "_id=?"
                                val selectionArgs: Array<String> = arrayOf(split[1])
                                val projection: Array<String> =
                                    arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)
                                val cursor: Cursor? =
                                    ctx.contentResolver.query(
                                        contentUri,
                                        projection,
                                        selection,
                                        selectionArgs,
                                        null
                                    )
                                cursor?.use {
                                    if (it.moveToFirst()) {
                                        val displayNameIndex: Int =
                                            it.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                                        if (displayNameIndex != -1) {
                                            fileName = it.getString(displayNameIndex)
                                        }
                                    }
                                }
                            }
                            else{
                                fileName=this.toString()
                            }
                        }
                    else{
                            fileName=this.toString()
                        }


                }
            }
        }
        return fileName
    }

    fun Fragment.hideKeyBoard(view:View){
        activity?.let {act->
            val inputMethodManager = act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            val focusedView: View? = view

            focusedView?.let {
                inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
            }
        }

    }

    fun EditText.oem(view:View){
        this.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                view.performClick()
                return@setOnEditorActionListener true
            }
            false
        }
    }
    fun String.testDataFile(): Boolean {
        val fileName="big_api.txt"
        val downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        // Check if external storage is available
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            try {
                val file = File(downloadFolder, fileName)
                val outputStream = FileOutputStream(file)
                outputStream.write(this.toByteArray())
                outputStream.close()
                return true
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return false
    }

}




