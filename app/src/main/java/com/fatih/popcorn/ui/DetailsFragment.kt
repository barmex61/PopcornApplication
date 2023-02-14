package com.fatih.popcorn.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fatih.popcorn.R
import com.fatih.popcorn.databinding.FragmentDetailsBinding
import com.fatih.popcorn.viewmodel.DetailsFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment: Fragment(R.layout.fragment_details) {

    private lateinit var viewModel: DetailsFragmentViewModel
    private lateinit var binding:FragmentDetailsBinding
    private var selectedId:Int?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= FragmentDetailsBinding.inflate(inflater,container,false)
        doInitialization()
        //binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, _, _, _ ->
        //binding.layoutHeader.y= binding.nestedScrollView.scrollY.toFloat()-binding.nestedScrollView.scrollY.toFloat()/2.6f })
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
            selectedId=DetailsFragmentArgs.fromBundle(it).id
            // isItInDatabase(selectedTvShowId!!)
            //vibrantColor=DetailsFragmentArgs.fromBundle(it).vibrantColor
        }
        viewModel.getDetails("movie",505642,"")
        observeLiveData()
    }

    private fun observeLiveData(){

        viewModel.detailResponse.observe(viewLifecycleOwner){
            it.let {
                println(it.status)
            }
        }

    }
}