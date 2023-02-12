package com.fatih.popcorn.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.fatih.popcorn.R
import com.fatih.popcorn.databinding.FragmentDetailsBinding
import com.fatih.popcorn.viewmodel.DetailsFragmentViewModel

class DetailsFragment: Fragment(R.layout.fragment_details) {

    private lateinit var viewModel: DetailsFragmentViewModel
    private lateinit var binding: FragmentDetailsBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_details,container,false)
        doInitialization()
        binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, _, _, _ ->
            binding.layoutHeader.y= binding.nestedScrollView.scrollY.toFloat()-binding.nestedScrollView.scrollY.toFloat()/2.6f })
        return binding.root
    }

    private fun doInitialization(){

    }
}