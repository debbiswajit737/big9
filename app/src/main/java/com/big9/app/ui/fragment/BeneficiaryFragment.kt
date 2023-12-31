package com.big9.app.ui.fragment


import BeneficiaryListAdapter
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.big9.app.R

import com.big9.app.data.model.BeneficiaryListModel
import com.big9.app.data.viewMovel.MyViewModel
import com.big9.app.databinding.FragmentBeneficiaryBinding
import com.big9.app.ui.base.BaseFragment
import com.big9.app.utils.*
import com.big9.app.utils.`interface`.CallBack
import com.big9.app.utils.`interface`.CallBack4

import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter


class BeneficiaryFragment : BaseFragment() {
    lateinit var binding: FragmentBeneficiaryBinding
    private val viewModel: MyViewModel by activityViewModels()
    var beneficiaryList = ArrayList<BeneficiaryListModel>()
    var beneficiaryListAdapter :BeneficiaryListAdapter?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_beneficiary, container, false)
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
            tvAddBeneficiary.setOnClickListener {
                findNavController().navigate(R.id.action_beneficiaryFragment_to_addBeneficiaryFragment)
            }
          }

        }


    fun initView() {
        binding.recycleViewBeneficiary.apply {
            beneficiaryList.clear()
            beneficiaryList.add(BeneficiaryListModel("Test User1",R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
            beneficiaryList.add(BeneficiaryListModel("Test User2",R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
            beneficiaryList.add(BeneficiaryListModel("Test User3",R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
            beneficiaryList.add(BeneficiaryListModel("Test User4",R.drawable.axix_bank_logo,"AXIX BANK","A/C:91022112121212","IFSC:UTIB0000669"))
            beneficiaryListAdapter=BeneficiaryListAdapter(beneficiaryList, object : CallBack4 {
                override fun getValue4(s1: String, s2: String, s3: String, s4: String) {

                    activity?.let {act->
                        val selectTransactionTypeBottomSheetDialog = SelectTransactionTypeBottomSheetDialog(object :
                            CallBack {
                            override fun getValue(s: String) {
                                val tpinBottomSheetDialog = TpinBottomSheetDialog(object :
                                    CallBack {
                                    override fun getValue(s: String) {
                                        viewModel?.receiveStatus?.value="Success"
                                        val dialogFragment = ReceptDialogFragment()
                                        dialogFragment.show(childFragmentManager, dialogFragment.tag)
                                    }
                                })
                                tpinBottomSheetDialog.show(
                                    act.supportFragmentManager,
                                    tpinBottomSheetDialog.tag
                                )
                            }
                        })
                        selectTransactionTypeBottomSheetDialog.show(
                            act.supportFragmentManager,
                            selectTransactionTypeBottomSheetDialog.tag
                        )
                    }
                }
            })
            adapter=beneficiaryListAdapter
        }
    }

    fun setObserver() {
        binding.apply {
            etSearch.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    beneficiaryListAdapter?.filter?.filter(s)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
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