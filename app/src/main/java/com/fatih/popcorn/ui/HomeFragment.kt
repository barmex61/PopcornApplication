package com.fatih.popcorn.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.fatih.popcorn.R
import com.fatih.popcorn.adapter.HomeFragmentAdapter
import com.fatih.popcorn.databinding.FragmentHomeBinding
import com.fatih.popcorn.other.Constants.isItInMovieList
import com.fatih.popcorn.other.Constants.sortList
import com.fatih.popcorn.other.Status
import com.fatih.popcorn.viewmodel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment @Inject constructor( private val adapter:HomeFragmentAdapter): Fragment(R.layout.fragment_home) {

    private lateinit var binding:FragmentHomeBinding
    private lateinit var viewModel:HomeFragmentViewModel
    private var totalAvailablePages=1
    private var currentPage=1
    private var sortString= sortList[0]
    private var searchText=""
    private var genres="28"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=DataBindingUtil.bind(view)!!
        viewModel=ViewModelProvider(requireActivity())[HomeFragmentViewModel::class.java]
        setupRecyclerView()
        viewModel.getMovies(1,sortString,genres)
        observeLiveData()
    }

    private fun setupRecyclerView(){
        binding.moviesRecyclerView.layoutManager= GridLayoutManager(requireContext(),3)
        binding.moviesRecyclerView.setOnScrollChangeListener { _, _, _, _, _ ->

            if (!binding.moviesRecyclerView.canScrollVertically(1) && currentPage < totalAvailablePages) {
                currentPage++
                if (searchText.isEmpty()) {
                    if (isItInMovieList) {
                        viewModel.getMovies(currentPage,sortString, genres)

                    } else {
                        viewModel.getTvShows(currentPage,sortString, genres)
                    }
                } else {
                    if (isItInMovieList) {
                        //TODO SEARCHLIVEDATA observeSearchLiveData(searchName!!, searchText, currentPage)
                    } else {
                        //TODO SEARCH observeSearchLiveData(searchName!!, searchText, currentPage)
                    }
                }
            }
        }
    }

    private fun observeLiveData(){
        viewModel.movieList.observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING->{
                    setProgressBarVisibility(true)
                }
                Status.SUCCESS->{
                    setProgressBarVisibility(false)
                    adapter.movieList = it.data ?: listOf()
                    binding.moviesRecyclerView.adapter=adapter

                }
                Status.ERROR->{
                    setProgressBarVisibility(false)
                }
            }
        }
    }

    private fun setProgressBarVisibility(isVisible:Boolean){
        if (isVisible){
            binding.progressBar.visibility=View.VISIBLE
        }else{
            binding.progressBar.visibility=View.GONE
        }
    }
}