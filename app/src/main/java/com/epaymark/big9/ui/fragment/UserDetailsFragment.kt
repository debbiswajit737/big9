package com.epaymark.big9.ui.fragment


import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.epaymark.big9.R

import com.epaymark.big9.adapter.UserDetailsAdapter
import com.epaymark.big9.data.model.UserDetails
import com.epaymark.big9.data.viewMovel.MyViewModel
import com.epaymark.big9.databinding.FragmentUserDetailsBinding

import com.epaymark.big9.ui.base.BaseFragment
import com.epaymark.epay.utils.*
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter


class UserDetailsFragment : BaseFragment() {
    lateinit var binding: FragmentUserDetailsBinding
    private val viewModel: MyViewModel by activityViewModels()
    var userDetailsList = ArrayList<UserDetails>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_details, container, false)
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

          }
        }


    fun initView() {
        binding.recycleViewUserdetails.apply {
            try {
                val bitmap = encodeAsBitmap("HELLO")
                binding.imgQrcode.setImageBitmap(bitmap)
            } catch (ex: WriterException) {
                ex.printStackTrace()
            }
            userDetailsList.add(UserDetails("Name","Test User"))
            userDetailsList.add(UserDetails("Business Name","Test Business Name"))
            userDetailsList.add(UserDetails("Registered Mobile Number","9999999999"))
            userDetailsList.add(UserDetails("Registered Email Id","test@test.com"))
            userDetailsList.add(UserDetails("Address","123, Park Street,Kolkata - 700001,West Bengal, India"))
            userDetailsList.add(UserDetails("District/City","Kolkata"))
            userDetailsList.add(UserDetails("State","West Bengal"))
            userDetailsList.add(UserDetails("Account Type","Distributor"))
            userDetailsList.add(UserDetails("Super Distributor","9999999999"))
            userDetailsList.add(UserDetails("Distributor","9999999999"))
            adapter= UserDetailsAdapter(userDetailsList)
        }
    }

    fun setObserver() {
        binding.apply {

        }

    }

    @Throws(WriterException::class)
    fun encodeAsBitmap(str: String): Bitmap? {
        val writer = QRCodeWriter()
        val bitMatrix: BitMatrix = writer.encode(str, BarcodeFormat.QR_CODE, 400, 400)
        val w: Int = bitMatrix.getWidth()
        val h: Int = bitMatrix.getHeight()
        val pixels = IntArray(w * h)
        for (y in 0 until h) {
            for (x in 0 until w) {
                pixels[y * w + x] = if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
        return bitmap
    }
}