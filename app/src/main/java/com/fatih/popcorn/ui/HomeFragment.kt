package com.fatih.popcorn.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.fatih.popcorn.R
import com.fatih.popcorn.adapter.HomeFragmentAdapter
import com.fatih.popcorn.databinding.FragmentHomeBinding
import com.fatih.popcorn.other.Constants.checkIsItInMovieListOrNot
import com.fatih.popcorn.other.Constants.movieGenreMap
import com.fatih.popcorn.other.Constants.movieSearch
import com.fatih.popcorn.other.Constants.movie_booleanArray
import com.fatih.popcorn.other.Constants.movie_genre_list
import com.fatih.popcorn.other.Constants.qualityArray
import com.fatih.popcorn.other.Constants.qualityBooleanArray
import com.fatih.popcorn.other.Constants.sortArray
import com.fatih.popcorn.other.Constants.sortList
import com.fatih.popcorn.other.Constants.stateList
import com.fatih.popcorn.other.Constants.tvSearch
import com.fatih.popcorn.other.Constants.tvShowGenreMap
import com.fatih.popcorn.other.Constants.tv_show_booleanArray
import com.fatih.popcorn.other.Constants.tv_show_genre_list
import com.fatih.popcorn.other.State
import com.fatih.popcorn.other.Status
import com.fatih.popcorn.viewmodel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment @Inject constructor( private val adapter:HomeFragmentAdapter): Fragment(R.layout.fragment_home) {

    private lateinit var binding:FragmentHomeBinding
    private var totalAvailablePages=1
    private var job: Job?=null
    private var isSearching=false
    private var sortString= sortList[0]
    private var searchText=""
    private var genres=""
    private var indexPosition=0
    private var searchCategory= movieSearch
    private var tvShowSortPosition=0
    private var movieSortPosition=0
    private val viewModel:HomeFragmentViewModel by lazy{
        MainActivity.viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=DataBindingUtil.bind(view)!!
        doInitialization()
    }
    private fun doInitialization(){
        setStatusBarPadding()
        if(viewModel.searchQuery.value!!.isNotEmpty()){
            searchText= viewModel.searchQuery.value!!
            binding.searchText.setText(searchText)
            searchImageClicked(binding.searchImage)
            val name=if(checkIsItInMovieListOrNot()) movieSearch else tvSearch
            viewModel.search(name,searchText,false)
        }
        binding.searchText.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(it.context,R.anim.fade_scale_animation))
        }
        setupRecyclerView()
        setIndicatorColor(checkIsItInMovieListOrNot())
        binding.watchImage.setOnClickListener { findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWatchListFragment()) }
        adapter.setMyOnClickLambda { id, pair ->
            pair?.let {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(id,pair.first,pair.second))
            }?: findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(id,R.color.black2,R.color.white))
        }
        binding.navigationView.setNavigationItemSelectedListener {
            setNavigation(it)
            return@setNavigationItemSelectedListener false
        }

        binding.movieButton.setOnClickListener {
            movieButtonClicked()
        }
        binding.tvShowButton.setOnClickListener {
            tvShowButtonClicked()
        }
        binding.searchImage.setOnClickListener {
           searchImageClicked(it)
        }
        binding.menuImage.setOnClickListener {
            if(isSearching){
                binding.searchText.apply {
                    text.clear()
                    clearFocus()
                    viewModel.searchQuery.value=""
                    visibility=View.GONE
                    binding.headerText.visibility=View.VISIBLE
                }
                binding.menuImage.apply {
                    startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.button_animation))
                    setImageResource(R.drawable.ic_menu)
                }
                isSearching=false
            }else{
                binding.drawableLayout.openDrawer(GravityCompat.START)
            }
        }
        observeLiveData()
        setTextChangeListener()
    }

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    private fun setStatusBarPadding(){
        val statusBarHeightId = resources.getIdentifier("status_bar_height", "dimen", "android")
        val statusBarHeight = resources.getDimensionPixelSize(statusBarHeightId) +10
        binding.layoutHeader.updatePadding(top = statusBarHeight)
    }

    private fun searchImageClicked(view:View){
        binding.headerText.visibility=View.GONE
        binding.searchText.apply {
            visibility=View.VISIBLE
            requestFocus()
            isFocusableInTouchMode=true
            view.startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.fade_scale_animation))
            startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.fade_scale_animation))
        }
        binding.menuImage.apply {
            startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.button_animation))
            setImageResource(R.drawable.ic_back)
        }
        isSearching=true
    }

    override fun onPause() {
        super.onPause()
        binding.searchText.text?.clear()
        binding.searchText.clearFocus()
    }

    private fun setTextChangeListener(){
        binding.searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)=Unit
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) =Unit
            override fun afterTextChanged(p0: Editable?) {
                searchText=p0.toString().trim().lowercase()
                job?.cancel()
                if(searchText.isNotEmpty()){
                    job=lifecycleScope.launch{
                        delay(200L)
                        viewModel.search(searchCategory,searchText,false)
                        viewModel.currentPage.value=1
                        totalAvailablePages=1
                    }
                }else{
                    job?.cancel()
                    if (stateList.last()==State.SEARCH){
                        stateList.removeLast()
                    }
                    when(stateList.last()){
                        State.TV_SHOW->{tvShowButtonClicked()}
                        State.MOVIE->{movieButtonClicked()}
                        else -> Unit
                    }
                }
            }

        })
    }
    private fun setupRecyclerView(){
        binding.moviesRecyclerView.adapter=adapter
        binding.moviesRecyclerView.layoutManager= GridLayoutManager(requireContext(),
            Resources.getSystem().displayMetrics.widthPixels/200)
        binding.moviesRecyclerView.setOnScrollChangeListener { _, _, _, _, _ ->
            if (!binding.moviesRecyclerView.canScrollVertically(1) && viewModel.currentPage.value!! <= totalAvailablePages) {
                viewModel.currentPage.value= viewModel.currentPage.value!! +1
                when(stateList.last()){
                    State.SEARCH->{
                        if(searchText.isNotEmpty()){
                            viewModel.search(searchCategory,searchText,false)
                        }
                    }
                    State.MOVIE->{ viewModel.getMovies(sortString, genres,false) }
                    State.TV_SHOW->{viewModel.getTvShows(sortString, genres,false)}
                }
            }
        }
    }


    private fun movieButtonClicked(){
        if(stateList.last()!=State.MOVIE){
            genres=""
            sortString= sortList[0]
            tv_show_booleanArray.fill(false)
            movie_booleanArray.fill(false)
            viewModel.currentPage.value=1
        }
        searchCategory= movieSearch

        if(searchText.isEmpty()){
            println("isempty")
            viewModel.getMovies(sortString,genres,true)
        }else{
            viewModel.search(searchCategory,searchText,true)
        }
        setIndicatorColor(checkIsItInMovieListOrNot())
    }

    private fun tvShowButtonClicked(){

        if(stateList.last()!=State.TV_SHOW){
            genres=""
            sortString= sortList[0]
            tv_show_booleanArray.fill(false)
            movie_booleanArray.fill(false)
            viewModel.currentPage.value=1
        }
        searchCategory= tvSearch
        if(searchText.isEmpty()){
            viewModel.getTvShows(sortString,genres,true)
        }else{
            viewModel.search(searchCategory,searchText,true)
        }
        setIndicatorColor(checkIsItInMovieListOrNot())
    }

    private fun setIndicatorColor(isItInMovie:Boolean){
        if(isItInMovie){
            binding.movieButtonIndicator.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.indicatorColor))
            binding.tvShowButtonIndicator.setBackgroundColor(
                ContextCompat.getColor(requireContext(),
                    android.R.color.transparent))
        }else{
            binding.tvShowButtonIndicator.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.indicatorColor))
            binding.movieButtonIndicator.setBackgroundColor(
                ContextCompat.getColor(requireContext(),
                    android.R.color.transparent))
        }
    }

    private fun observeLiveData(){

        viewModel.discoverData.observe(viewLifecycleOwner){
            when(it.status){
                Status.LOADING->{
                    setProgressBarVisibility(true)
                }
                Status.SUCCESS->{
                    setProgressBarVisibility(false)
                    adapter.discoverList=it.data?.results?: listOf()
                    totalAvailablePages=it.data?.total_pages?:1
                }
                Status.ERROR->{
                    setProgressBarVisibility(false)
                }
            }
        }

    }

    private fun setNavigation(it: MenuItem){
        when(it.itemId){
            R.id.movies->{

                val list= arrayOf("Movie","Tv Show")
                val alertDialog=AlertDialog.Builder(requireContext())
                alertDialog.setTitle("Index")
                alertDialog.setSingleChoiceItems(list,indexPosition
                ) { _, p1 -> indexPosition = p1 }
                alertDialog.setNegativeButton("Cancel"
                ) { _, _ -> }.setPositiveButton("OK"){_,_->
                    if(indexPosition==0){
                        movieButtonClicked()
                        binding.drawableLayout.closeDrawer(GravityCompat.START)
                    }else{
                        tvShowButtonClicked()
                        binding.drawableLayout.closeDrawer(GravityCompat.START)
                    }
                }.show()
            }
            R.id.movie_filter->{

                val alertDialog=AlertDialog.Builder(requireContext())
                if(checkIsItInMovieListOrNot()){
                    alertDialog.setMultiChoiceItems(
                        movie_genre_list, movie_booleanArray
                    ) { _, p1, p2 -> movie_booleanArray[p1] = p2 }

                    alertDialog.setTitle("Genre").setNegativeButton("CANCEL"
                    ) { _, _ -> }.setPositiveButton("OK") { _, _ ->
                        genres = ""
                        movie_booleanArray.forEachIndexed { index, _ ->
                            if (movie_booleanArray[index]) {
                                val value = movie_genre_list[index]
                                genres = if (genres.isEmpty()) {
                                    movieGenreMap[value].toString()
                                } else {
                                    genres + "," + movieGenreMap[value].toString()
                                }
                            }
                        }
                        movieButtonClicked()
                        binding.drawableLayout.closeDrawer(GravityCompat.START)
                    }
                        alertDialog.setNeutralButton("Reset"
                        ) { _, _ ->
                            movie_booleanArray.fill(false)
                            genres=""
                            movieButtonClicked()
                        }.show()
                }else{
                    alertDialog.setMultiChoiceItems(
                        tv_show_genre_list, tv_show_booleanArray
                    ) { _, p1, p2 -> tv_show_booleanArray[p1] = p2 }

                    alertDialog.setTitle("Genre").setNegativeButton("CANCEL"
                    ) { _, _ -> }.setPositiveButton("OK") { _, _ ->
                        genres= ""
                        tv_show_booleanArray.forEachIndexed { index, _ ->
                            if (tv_show_booleanArray[index]) {
                                val value = tv_show_genre_list[index]
                                genres = if (genres.isEmpty()) {
                                    tvShowGenreMap[value].toString()
                                } else {
                                    genres + "," + tvShowGenreMap[value].toString()
                                }
                            }
                        }
                        tvShowButtonClicked()
                        binding.drawableLayout.closeDrawer(GravityCompat.START)
                    }
                    alertDialog.setNeutralButton("Reset"
                    ) { _, _ ->
                        tv_show_booleanArray.fill(false)
                        tvShowButtonClicked()
                    }.show()
                }


            }
            R.id.sort->{
                val alertDialog=AlertDialog.Builder(requireContext())
                if(checkIsItInMovieListOrNot()){
                    alertDialog.setTitle("Sort By").setSingleChoiceItems(
                        sortArray,movieSortPosition
                    ) { _, p1 -> movieSortPosition = p1 }.setNegativeButton("CANCEL"
                    ) { _, _ -> }.setPositiveButton("OK") { _, _ ->
                        when(movieSortPosition){
                            0->sortString= sortList[0]
                            1->sortString= sortList[3]
                            2->sortString= sortList[2]
                        }
                        movieButtonClicked()
                    }.show()
                }else{
                    alertDialog.setTitle("Sort By").setSingleChoiceItems(
                        sortArray,tvShowSortPosition
                    ) { _, p1 -> tvShowSortPosition = p1 }.setNegativeButton("CANCEL"
                    ) { _, _ -> }.setPositiveButton("OK") { _, _ ->
                        when(tvShowSortPosition){
                            0->sortString= sortList[0]
                            1->sortString= sortList[1]
                            2->sortString= sortList[2]
                        }
                        tvShowButtonClicked()
                    }.show()
                }

            }
            R.id.quality->{
                val alertDialog=AlertDialog.Builder(requireContext())
                alertDialog.setTitle("Quality").setMultiChoiceItems(qualityArray, qualityBooleanArray
                ) { _, p1, p2 -> qualityBooleanArray[p1] = p2 }.setNegativeButton("CANCEL"
                ) { _, _ ->
                    qualityBooleanArray.forEachIndexed { index, _->
                        qualityBooleanArray[index] = false
                    }
                }.setPositiveButton("OK") { _, _ ->
                    //search for quality
                }.show()
            }
            R.id.like->{
                binding.drawableLayout.closeDrawer(GravityCompat.START)
               // findNavController().navigate(MainFragmentDirections.actionMainFragmentToWatchListFragment())
            }
            else ->{
                Toast.makeText(requireContext(),"Coming Soon Ins",Toast.LENGTH_SHORT).show()
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