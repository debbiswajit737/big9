package com.epaymark.epay.utils.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import androidx.core.content.ContextCompat
import com.epaymark.epay.R
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PdfGenerator {

    /*fun generatePdfWithBackground(context: Context, fileName: String, content: String) {
        val filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(filePath, fileName)

        try {
            val pdfWriter = PdfWriter(FileOutputStream(file))
            val pdfDocument = PdfDocument(pdfWriter)
            val document = Document(pdfDocument)

            // Load the drawable resource as a Bitmap
            val drawableId = R.drawable.ep_certificate // Replace with your drawable resource ID
            val bitmap = (ContextCompat.getDrawable(context, drawableId) as BitmapDrawable).bitmap

            // Create an Image object from the Bitmap
            val backgroundImage = Image(ImageDataFactory.create(bitmap, ImageDataFactory.getImageBytesForRawBytes(bitmapToByteArray(bitmap))))

            backgroundImage.scaleToFit(pdfDocument.defaultPageSize.width, pdfDocument.defaultPageSize.height)
            document.add(backgroundImage)

            // Add content on top of the background
            document.add(Paragraph(content))

            document.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }*/



}