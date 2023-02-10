package com.fatih.popcorn.ui

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.fatih.popcorn.R
import com.fatih.popcorn.adapter.HomeFragmentAdapter
import com.fatih.popcorn.databinding.FragmentHomeBinding
import com.fatih.popcorn.entities.remote.movie.MovieResult
import com.fatih.popcorn.other.Constants.isItInMovieList
import com.fatih.popcorn.other.Constants.sortList
import com.fatih.popcorn.other.Status
import com.fatih.popcorn.viewmodel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment @Inject constructor( private val adapter:HomeFragmentAdapter): Fragment(R.layout.fragment_home) {

    private lateinit var binding:FragmentHomeBinding
    private var totalAvailablePagesMovie=1
    private var totalAvailablePagesTvShow=1
    private var currentPage=1
    private var oldCount=0
    private var sortString= sortList[0]
    private var searchText=""
    private var genres=""
    private var movieList= mutableListOf<MovieResult>()
    private val viewModel:HomeFragmentViewModel by lazy{
        MainActivity.viewModel
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=DataBindingUtil.bind(view)!!
        doInitialization()
    }

    private fun doInitialization(){
        setupRecyclerView()
        if(isItInMovieList){
            movieButtonClicked()
        }else{
            tvShowButtonClicked()
        }
        binding.movieButton.setOnClickListener {
            movieButtonClicked()
        }
        binding.tvShowButton.setOnClickListener {
            tvShowButtonClicked()
        }
        observeLiveData()
    }
    override fun onPause() {
        super.onPause()
        binding.searchText.text.clear()
        binding.searchText.clearFocus()
    }

    private fun setupRecyclerView(){
        binding.moviesRecyclerView.layoutManager= GridLayoutManager(requireContext(),3)
        binding.moviesRecyclerView.setOnScrollChangeListener { _, _, _, _, _ ->
        val totalAvailablePages=if(isItInMovieList) totalAvailablePagesMovie else totalAvailablePagesTvShow
            if (!binding.moviesRecyclerView.canScrollVertically(1) && currentPage < totalAvailablePages) {
                currentPage++
                if (searchText.isEmpty()) {
                    if (isItInMovieList) {
                        println(currentPage)
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


    private fun movieButtonClicked(){

        isItInMovieList=true
        adapter.apply {
            tvShowList= listOf()
            movieList= listOf()
        }
        currentPage=1

        if(searchText.isEmpty()){
            viewModel.getMovies(currentPage,sortString,genres)
        }else{
            //observeSearchLiveData(searchName!!,searchText,currentPage)
        }

        binding.movieButtonIndicator.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.teal_700))
        binding.tvShowButtonIndicator.setBackgroundColor(
            ContextCompat.getColor(requireContext(),
                android.R.color.transparent))
    }
    private fun tvShowButtonClicked(){

        isItInMovieList=false
        adapter.apply {
            movieList= listOf()
            tvShowList= listOf()
        }
        currentPage=1
        if(searchText.isEmpty()){
            viewModel.getTvShows(currentPage,sortString,genres)
        }else{
           // observeSearchLiveData(searchName!!,searchText,currentPage)
        }

        binding.tvShowButtonIndicator.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.teal_700))
        binding.movieButtonIndicator.setBackgroundColor(
            ContextCompat.getColor(requireContext(),
                android.R.color.transparent))
    }

    private fun observeLiveData(){
        viewModel.movieList.observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING->{
                    setProgressBarVisibility(true)
                }
                Status.SUCCESS->{
                    setProgressBarVisibility(false)

                    it.data?.let {movieResponse->

                        oldCount=adapter.movieList.size
                        adapter.movieList=adapter.movieList+movieResponse.results
                        adapter.notifyItemRangeInserted(oldCount,adapter.movieList.size)
                        binding.isLoading=false
                        totalAvailablePagesMovie=movieResponse.total_pages

                    }

                }
                Status.ERROR->{
                    setProgressBarVisibility(false)
                }
            }
        }
        viewModel.tvShowList.observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING->{
                    setProgressBarVisibility(true)
                }
                Status.SUCCESS->{
                    setProgressBarVisibility(false)
                    adapter.tvShowList = it.data?.results?: listOf()
                    binding.moviesRecyclerView.adapter=adapter
                    totalAvailablePagesTvShow=it.data?.total_pages?:1

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