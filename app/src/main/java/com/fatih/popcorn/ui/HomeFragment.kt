package com.fatih.popcorn.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.fatih.popcorn.R
import com.fatih.popcorn.adapter.HomeFragmentAdapter
import com.fatih.popcorn.databinding.FragmentHomeBinding
import com.fatih.popcorn.other.Constants.checkIsItInMovieListOrNot
import com.fatih.popcorn.other.Constants.movieGenreMap
import com.fatih.popcorn.other.Constants.movieSearch
import com.fatih.popcorn.other.Constants.movie_booleanArray
import com.fatih.popcorn.other.Constants.movie_genre_list
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
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding:FragmentHomeBinding?=null
    private val binding:FragmentHomeBinding
    get() = _binding!!
    private var totalAvailablePages=1
    private var job: Job?=null
    private var isSearching=false
    private var sortString= sortList[0]
    private var searchText=""
    private var genres=""
    private var indexPosition=0
    private var recyclerView:RecyclerView?=null
    private var searchCategory= movieSearch
    private var tvShowSortPosition=0
    private lateinit var onScrollListener: OnScrollListener
    private lateinit var adapter: HomeFragmentAdapter
    private var view:View?=null
    private var movieSortPosition=0
    private lateinit var viewModel:HomeFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding=DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        view=binding.root
        viewModel=ViewModelProvider(requireActivity())[HomeFragmentViewModel::class.java]
        genres=savedInstanceState?.getString("genres",genres)?:genres
        sortString=savedInstanceState?.getString("sort",sortString)?:sortString
        doInitialization()
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {

        super.onSaveInstanceState(outState)
        outState.putString("genres",genres)
        outState.putString("sort",sortString)
    }
    private fun doInitialization(){
        setStatusBarPadding()
        adapter=HomeFragmentAdapter(R.layout.fragment_main_rv_row)
        searchCategory=if(checkIsItInMovieListOrNot()) movieSearch else tvSearch
        if(viewModel.searchQuery.value!!.isNotEmpty()){
            searchText= viewModel.searchQuery.value!!
            searchImageClicked(binding.searchImage)
            binding.searchText.setText(searchText)
            val name=if(checkIsItInMovieListOrNot()) movieSearch else tvSearch
            viewModel.search(name,searchText,false)
        }
        binding.searchText.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(it.context,R.anim.fade_scale_animation))
        }
        setupRecyclerView()
        setIndicatorColor(checkIsItInMovieListOrNot())
        binding.watchImage.setOnClickListener { findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWatchListFragment()) }
        adapter.setMyOnClickLambda { url,id, pair ,isTvShow->
            pair?.let {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(id,pair.first,pair.second,url,null))
            }?: findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(id,R.color.white,R.color.black2,url,null))
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
                    viewModel.resetData()
                    visibility=View.GONE
                    binding.headerText.visibility=View.VISIBLE
                    if(checkIsItInMovieListOrNot()) {
                        viewModel.getMovies(sortString,genres)
                    }else{
                        viewModel.getTvShows(sortString,genres)
                    }
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
                    viewModel.currentPage.value=1
                    job=lifecycleScope.launch{
                        delay(200L)
                        viewModel.search(searchCategory,searchText,false)
                        totalAvailablePages=1
                    }
                }else{

                    job?.cancel()

                    when(stateList.last()){
                        State.SEARCH->{
                            stateList.removeLast()
                        }
                        State.TV_SHOW->{tvShowButtonClicked()}
                        State.MOVIE->{movieButtonClicked()}

                    }
                }
            }

        })
    }
    private fun setupRecyclerView(){
        recyclerView =binding.moviesRecyclerView
        recyclerView!!.adapter=adapter
        recyclerView!!.layoutManager= GridLayoutManager(requireContext(), Resources.getSystem().displayMetrics.widthPixels/200)
        onScrollListener=object:OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1) && viewModel.currentPage.value!! < totalAvailablePages) {
                    viewModel.currentPage.value= viewModel.currentPage.value!! +1
                    when(stateList.last()){
                        State.SEARCH->{
                            if(searchText.isNotEmpty()){
                                viewModel.search(searchCategory,searchText,false)
                            }
                        }
                        State.MOVIE->{ viewModel.getMovies(sortString, genres) }
                        State.TV_SHOW->{ viewModel.getTvShows(sortString, genres)}
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        }
        recyclerView!!.addOnScrollListener(onScrollListener)
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
            viewModel.getMovies(sortString,genres)
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
            viewModel.getTvShows(sortString,genres)
        }else{
            viewModel.search(searchCategory,searchText,true)
        }
        setIndicatorColor(checkIsItInMovieListOrNot())
    }

    private fun setIndicatorColor(isItInMovie:Boolean){
        if(isItInMovie){
            binding.movieButtonIndicator.setBackgroundResource(R.drawable.view_divider_bg)
            binding.tvShowButtonIndicator.setBackgroundColor(
                ContextCompat.getColor(requireContext(),
                    android.R.color.transparent))
        }else{
            binding.tvShowButtonIndicator.setBackgroundResource(R.drawable.view_divider_bg)
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
                    adapter.list=it.data?.results?: listOf()
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
                        viewModel.resetData()
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
                        genres=""
                        viewModel.resetData()
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
            R.id.like->{
                binding.drawableLayout.closeDrawer(GravityCompat.START)
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWatchListFragment())
            }
            else ->{
                Snackbar.make(requireView(),"Coming soon",Snackbar.LENGTH_INDEFINITE).show()
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

    override fun onDestroyView() {
        recyclerView?.removeOnScrollListener(onScrollListener)
        recyclerView?.adapter=null
        recyclerView=null
        view=null
        _binding=null
        super.onDestroyView()
    }

}