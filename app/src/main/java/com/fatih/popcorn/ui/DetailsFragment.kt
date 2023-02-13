package com.fatih.popcorn.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fatih.popcorn.R
import com.fatih.popcorn.databinding.DetailsBinding
import com.fatih.popcorn.databinding.FragmentDetailsBinding
import com.fatih.popcorn.other.Constants.colorMatrixColorFilter
import com.fatih.popcorn.viewmodel.DetailsFragmentViewModel

class DetailsFragment: Fragment(R.layout.details) {

    private lateinit var viewModel: DetailsFragmentViewModel
    private lateinit var binding: DetailsBinding
    private var selectedId:Int?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.details,container,false)
        doInitialization()
        //binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, _, _, _ ->
          //  binding.layoutHeader.y= binding.nestedScrollView.scrollY.toFloat()-binding.nestedScrollView.scrollY.toFloat()/2.6f })
        return binding.root
    }

    private fun doInitialization(){
        viewModel= ViewModelProvider(this)[DetailsFragmentViewModel::class.java]
        //binding.trailerImage.setOnClickListener { youtube() }
        //binding.watchList.setOnClickListener { watchList() }
        //binding.shareButton.setOnClickListener { findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToWatchListFragment()) }
        //binding.reviewImage.setOnClickListener { goWeb() }
       // binding.backButton.setOnClickListener { findNavController().popBackStack() }
        //binding.episodesImage.setOnClickListener {view-> goEpisodes(view) }
        //binding.backgroundImage.colorFilter = colorMatrixColorFilter
        arguments?.let {
            // isItInDatabase(selectedTvShowId!!)
            selectedId=DetailsFragmentArgs.fromBundle(it).id
            //vibrantColor=DetailsFragmentArgs.fromBundle(it).vibrantColor
        }
        observeLiveData()
    }

    private fun observeLiveData(){

    }
}