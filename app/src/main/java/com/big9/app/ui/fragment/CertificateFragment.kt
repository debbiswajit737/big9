package com.big9.app.ui.fragment


import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.big9.app.R

import com.big9.app.data.model.ContactModel
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentCertificateBinding

import com.big9.app.ui.base.BaseFragment
import com.big9.app.utils.common.MethodClass
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class CertificateFragment : BaseFragment() {
    lateinit var binding: FragmentCertificateBinding
    private val viewModel: MyViewModel by activityViewModels()
    private var loader: Dialog? = null
    private lateinit var pickPdfLauncher: ActivityResultLauncher<Array<String>>
    var contactModelList = ArrayList<ContactModel>()
    var bitmap :Bitmap?=null
        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_certificate, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setObserver()
        onViewClick()
    }

    private fun onViewClick() {

        binding.apply {

            imgBack.back()
            sharedPreff?.getUserData()?.let{
                var userType=when(it.userType){
                "R"->"Retailer"
                "D"->"Distributor"
                "SD"->"Super Distributor"
                "F"->"Franchisee"
                "A"->"Admin"
                "COM"->"Compliance"
                 else->{""}
                }
                setImageCertificate(it.ID,it.name,userType)

            }

            imgDownlode.setOnClickListener{
                loader?.show()
                bitmap?.let {generatePdfWithBackground("big9_certificate.pdf","",it)  }
            }
          }
        }

    private fun setImageCertificate(id: String?, name: String?, userType: String) {
        // Load the drawable resource as a Bitmap
        val drawableId = R.drawable.ep_certificate // Replace with your drawable resource ID
        //val bitmap = (ContextCompat.getDrawable(context, drawableId) as BitmapDrawable).bitmap
        bitmap=addTextToDrawable(binding.root.context,drawableId,name.toString(),"${id}(${userType})")
        bitmap?.let { binding.imgCertificate.setImageBitmap(it) }

        //generatePdfWithBackground(binding.root.context,"epay_certificate.pdf","",bitmap)
    }

    fun generatePdfWithBackground(fileName: String, content: String, bitmap: Bitmap) {
        val filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(filePath, fileName)

        try {

            val pdfWriter = PdfWriter(FileOutputStream(file))
            val pdfDocument = PdfDocument(pdfWriter)
            //val pdfDocument = PdfDocument(pdfWriter,PageSize(customPageSize))
            val document = Document(pdfDocument)

            /*// Load the drawable resource as a Bitmap
            val drawableId = R.drawable.ep_certificate // Replace with your drawable resource ID
            //val bitmap = (ContextCompat.getDrawable(context, drawableId) as BitmapDrawable).bitmap
            val bitmap = addTextToDrawable(context,drawableId,"Biswajit")*/

            // Convert the Bitmap to a byte array
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()

            // Create an Image object from the byte array
            val backgroundImage = Image(ImageDataFactory.create(byteArray))

            backgroundImage.scaleToFit(500F, 800f)
            document.add(backgroundImage)

            // Add content on top of the background
            document.add(Paragraph(content))

            document.close()
            loader?.dismiss()

            openPdfFile()
        } catch (e: java.io.IOException) {
            e.printStackTrace()
            loader?.dismiss()
        }
        catch (e:Exception){
            loader?.dismiss()
        }
    }

    private fun openPdfFile() {
        binding.imgDownlodeMessage.visibility=View.VISIBLE
    }

    fun initView() {
        binding.root.context?.let {
            loader = MethodClass.custom_loader(it, getString(R.string.please_wait))
        }
    }

    fun setObserver() {
        binding.apply {

        }

    }


    fun addTextToDrawable(context: Context, drawableId: Int, name: String="", details: String=""): Bitmap {
        // Load the drawable resource as a Bitmap
        val drawable = ContextCompat.getDrawable(context, drawableId)
        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        // Create a canvas to draw on the bitmap
        val canvas = Canvas(bitmap)

        // Create a paint object for drawing text
        val paint = Paint().apply {
            color = requireActivity().getColor(R.color.title_header2)  // Set text color
            textSize = 60f // Set text size
            textAlign = Paint.Align.CENTER
        }
        val paint2 = Paint().apply {
            color = requireActivity().getColor(R.color.black) // Set text color
            textSize = 40f // Set text size
            textAlign = Paint.Align.CENTER

        }


        // Calculate text position (centered)
        //val x = (bitmap.width - paint.measureText(text)) / 2
        val x = bitmap.width   / 2
        val y = bitmap.height / 2

        // Draw text on the canvas
        canvas.drawText(name, x.toFloat(), y.toFloat()+70, paint)
        canvas.drawText(details, x.toFloat(), y.toFloat()+130, paint2)

        return bitmap
    }

}