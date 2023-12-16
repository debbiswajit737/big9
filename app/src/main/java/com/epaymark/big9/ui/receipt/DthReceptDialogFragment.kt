package com.epaymark.big9.ui.receipt


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.epaymark.big9.R
import com.epaymark.big9.data.model.DTHOperatorData
import com.epaymark.big9.data.model.DTHTranspherModel
import com.epaymark.big9.data.viewMovel.MyViewModel
import com.epaymark.big9.databinding.FragmentDthReceptDialogBinding
import com.epaymark.big9.ui.activity.DashboardActivity
import com.epaymark.big9.ui.base.BaseCenterSheetFragment
import com.epaymark.big9.utils.`interface`.CallBack
import kotlinx.coroutines.launch


class DthReceptDialogFragment(val callBack: CallBack, val dTHTranspherModel: DTHTranspherModel) : BaseCenterSheetFragment() {
    lateinit var binding: FragmentDthReceptDialogBinding
    private val viewModel: MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dth_recept_dialog, container, false)
        binding.viewModel = viewModel
        binding.model = dTHTranspherModel
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
            imgHome.backToHome()
            fabShare.setOnClickListener{
                shareImage()
            }
            imgHome.setOnClickListener{
                callBack.getValue("back")
                /*(activity as? DashboardActivity)?.let {
                    it.startActivity(Intent(it,DashboardActivity::class.java).putExtra(isRecept,true).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                }*/
            }
          }
        }



    private fun shareImage() {

        activity?.let {
            binding.apply {

                var screenshotBitmap =cardView2.takeScreenshot()
                (activity as? DashboardActivity)?.shareImage(screenshotBitmap)
            }
        }


    }

    fun initView() {
        setCrdViewMinHeight()

        lifecycleScope.launch {
            //delay(1000)
            Glide.with(binding.root.context)
                .load(viewModel.selectrdOperator.value)
                //.transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imgOperator)
        }
    }

    private fun setCrdViewMinHeight() {
    }

    fun setObserver() {
        binding.apply {
            viewModel?.selectrdOperator?.observe(viewLifecycleOwner){operatorImage->
                if (operatorImage!=null){
                   try {
                       imgOperator.setImageResource(operatorImage.toInt())
                   } catch (e:Exception){}
                }
            }
        }

    }


}