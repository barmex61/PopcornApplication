package com.fatih.popcorn.ui

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat

import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2

import com.fatih.popcorn.R
import com.fatih.popcorn.adapter.DetailsFragmentViewPagerAdapter
import com.fatih.popcorn.adapter.PosterImageViewPagerAdapter
import com.fatih.popcorn.databinding.FragmentDetailsBinding
import com.fatih.popcorn.entities.local.RoomEntity
import com.fatih.popcorn.entities.remote.detailresponse.DetailResponse
import com.fatih.popcorn.entities.remote.imageresponse.ImageResponse
import com.fatih.popcorn.other.Constants.checkIsItInMovieListOrNot
import com.fatih.popcorn.other.Constants.language
import com.fatih.popcorn.other.Constants.movieSearch
import com.fatih.popcorn.other.Constants.tvSearch
import com.fatih.popcorn.other.Status
import com.fatih.popcorn.ui.tabfragments.*
import com.fatih.popcorn.viewmodel.DetailsFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.Runnable
import java.math.RoundingMode
import java.util.*
import kotlin.Exception

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {


    private var _binding: FragmentDetailsBinding?=null
    private val binding:FragmentDetailsBinding
        get() = _binding!!
    private var viewPagerHandler:Handler?=null
    private var runnable:Runnable?=null
    private var selectedId: Int? = null
    private var _myFragmentManager:FragmentManager?=null
    private val myFragmentManager: FragmentManager
    get() = _myFragmentManager!!
    private var selectedResponse: DetailResponse? = null
    private var selectedImageResponse:ImageResponse?=null
    private var isItInDatabase = false
    private var isItInFavorite = false
    private var job:Job?=null
    private var fragmentViewPagerAdapter:DetailsFragmentViewPagerAdapter?=null
    private var job2:Job?=null
    private var posterList= listOf<String>()
    private var backgroundList= listOf<String>()
    private var searchLanguage= language
    private var fragmentViewPager:ViewPager2?=null
    private lateinit var viewModel: DetailsFragmentViewModel
    private var selectedUrl=""
    private var isSingleUrl=false
    private var animatorListener:Animator.AnimatorListener?=null

    companion object{
       var vibrantColor : Int ?=null
       var darkMutedColor : Int ?=null
       var seasonGenres: String?=null
       var seasonRating :String?=null
       var seasonRatingFloat:Float?=null
       var isItInMovieList=false

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        animatorListener= object :Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator) {
                binding.favoriteLottie.visibility=View.VISIBLE
            }
            override fun onAnimationEnd(animation: Animator) {
                println("end")
                binding.favoriteLottie.visibility=View.GONE
            }
            override fun onAnimationCancel(animation: Animator) {
                _binding?.let {
                    it.favoriteLottie.visibility=View.GONE
                }
            }
            override fun onAnimationRepeat(animation: Animator) = Unit
        }
        binding.favoriteLottie.addAnimatorListener(animatorListener)
        doInitialization()
        return binding.root
    }

    private fun doInitialization() {
        viewModel = ViewModelProvider(requireActivity())[DetailsFragmentViewModel::class.java]
        setStatusBarPadding()
        arguments?.let {
            selectedUrl=DetailsFragmentArgs.fromBundle(it).url?:""
            selectedId = DetailsFragmentArgs.fromBundle(it).id
            darkMutedColor = DetailsFragmentArgs.fromBundle(it).darkMutedColor
            vibrantColor = DetailsFragmentArgs.fromBundle(it).vibrantColor
            isItInMovieList=if (DetailsFragmentArgs.fromBundle(it).isTvShow != null){
                DetailsFragmentArgs.fromBundle(it).isTvShow == movieSearch
            }else{
                checkIsItInMovieListOrNot()
            }
        }
        if (isItInMovieList) {
            viewModel.getDetails(movieSearch, selectedId!!, searchLanguage)
        } else {
            viewModel.getDetails(tvSearch, selectedId!!, searchLanguage)
        }

        _myFragmentManager=childFragmentManager
        fragmentViewPagerAdapter=DetailsFragmentViewPagerAdapter(listOf(),myFragmentManager,viewLifecycleOwner.lifecycle)
        fragmentViewPager=binding.detailsViewPager.apply {
            this.offscreenPageLimit=1
        }
        setupPosterViewPager(posterList,backgroundList)
        binding.likeButton.setOnClickListener { favorite() }
        binding.saveButton.setOnClickListener { watchList(isItInFavorite) }
        binding.watchListButton.setOnClickListener { findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToWatchListFragment()) }
        binding.backButton.setOnClickListener { findNavController().navigateUp() }

        binding.trailerButton.setOnClickListener {
            findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToMoviePlayFragment(selectedId!!))
        }
        observeLiveData()
    }

    private fun setupFragmentViewPager(){
        val bundle=Bundle()
        bundle.putSerializable("detailResponse",selectedResponse!!)
        val fragmentList= mutableListOf(AboutFragment().apply {
            arguments=bundle
        },CastFragment(),ReviewFragment().apply {
            val castBundle=Bundle()
            castBundle.putInt("vibrantColor", vibrantColor!!)
            castBundle.putInt("id",selectedId!!)
            arguments=castBundle
        },RecommendFragment().apply {
            val recommendBundle=Bundle()
            recommendBundle.putInt("id",selectedId!!)
            arguments=recommendBundle
        },FamiliarFragment().apply{
            val familiarBundle=Bundle()
            familiarBundle.putInt("id",selectedId!!)
            arguments=familiarBundle
        })
        if(!isItInMovieList){
            fragmentList.add(SeasonsFragment())
        }

        fragmentViewPagerAdapter?.fragmentList=fragmentList
        fragmentViewPager!!.adapter=fragmentViewPagerAdapter

        TabLayoutMediator(binding.tabLayout,binding.detailsViewPager,true,true){tab,position->
            when(position){
                0->{tab.text=resources.getString(R.string.hakkinda)}
                1->{tab.text=resources.getString(R.string.actor)}
                2->{tab.text=resources.getString(R.string.comment)}
                3->{tab.text=resources.getString(R.string.recommendation)}
                4->{tab.text=resources.getString(R.string.familiar)}
                5->{tab.text=resources.getString(R.string.seasons)}
            }

        }.attach()
    }

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    private fun setStatusBarPadding() {
        val statusBarHeightId = resources.getIdentifier("status_bar_height", "dimen", "android")
        val statusBarHeight = resources.getDimensionPixelSize(statusBarHeightId)
        binding.linearLayout.updatePadding(top = statusBarHeight)
    }

    private fun observeLiveData() {

        viewModel.detailResponse.observe(viewLifecycleOwner) { resource ->
            if (resource != null) {
                when (resource.status) {
                    Status.LOADING -> {
                        setLayoutVisibility(show = false, showToast = false, null)
                    }
                    Status.ERROR -> {
                        setLayoutVisibility(show = false, showToast = true, resource.message)
                    }
                    Status.SUCCESS -> {
                        resource.data?.let {
                            selectedResponse = it

                            viewModel.isItIntDatabase(it.id!!)
                            setLayoutVisibility(show = true, showToast = false, null)
                            setupFragmentViewPager()
                        }
                    }
                }
            }
        }

        viewModel.isItInDatabase.observe(viewLifecycleOwner){response->
            println("normal $response")
            response?.let {
                if(it){
                    isItInDatabase=true
                    binding.saveButton.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.baseline_restore_from_trash_24))
                }else{
                    resetSaveImage()
                }
            }?:resetSaveImage()
        }

        viewModel.isItInFavorite.observe(viewLifecycleOwner){response->
            println("favorite $response")
            response?.let {
                if(it){
                    isItInFavorite=true
                    binding.likeButton.imageTintList= ColorStateList.valueOf(resources.getColor(R.color.scaletRed))
                }else{
                    resetFavorite()
                }
            }?:resetFavorite()

        }

        viewModel.imageResponse.observe(viewLifecycleOwner){ resource->
            if (resource != null) {
                when (resource.status) {
                  Status.SUCCESS -> {
                        resource.data?.let { it ->
                            selectedImageResponse=it

                            backgroundList=selectedImageResponse!!.backdrops.map {backdrop->
                                backdrop.file_path
                            }
                            posterList=selectedImageResponse!!.posters.filter {
                                it.iso_639_1=="en"
                            }.map {
                                it.file_path
                            }
                            if (posterList.isEmpty() && backgroundList.isEmpty()){
                                posterList= listOf(selectedUrl)
                                isSingleUrl=true
                            }
                            setupPosterViewPager(posterList,backgroundList)
                        }
                    }
                    else->Unit
                }
            }
        }
    }

    private fun resetSaveImage(){
        isItInDatabase=false
        binding.saveButton.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.baseline_add_24))
    }

    private fun resetFavorite(){
        isItInFavorite=false
        binding.likeButton.imageTintList= ColorStateList.valueOf(resources.getColor(R.color.white))
    }

    private fun setupPosterViewPager(portraitList: List<String>,landscapeList:List<String>){

        if (portraitList.isEmpty() && landscapeList.isEmpty()) return
        var portraits=portraitList
        var landscapes=landscapeList
        var shouldFitXY=false
        if(portraitList.isEmpty()){
            portraits=landscapeList
            shouldFitXY=true
        }
        if (landscapeList.isEmpty()){
            landscapes=portraitList
        }
        portraits=if (portraits.size>=10) portraits.subList(0,10) else portraits.subList(0,portraits.size)
        landscapes=if (landscapes.size>=10) landscapes.subList(0,10) else landscapes.subList(0,landscapes.size)
        val adapterList = if(Resources.getSystem().configuration.orientation==Configuration.ORIENTATION_PORTRAIT){
            portraits
        }else landscapes

        val viewPagerAdapter=PosterImageViewPagerAdapter(shouldFitXY).apply {
            urlList=adapterList
            singleUrl=isSingleUrl
        }
        binding.posterImageViewPager.apply {
            adapter=viewPagerAdapter
            offscreenPageLimit=3
            binding.circleIndicator.setViewPager(this)
        }
        viewPagerHandler= Handler(Looper.getMainLooper())
        runnable?.let {
            viewPagerHandler?.removeCallbacks(it)
        }
        var started=false
        runnable= Runnable {
            try {
                binding.posterImageViewPager.apply {
                    if (currentItem==this.adapter!!.itemCount-1){
                        currentItem=0
                        setCurrentItem(currentItem,true)
                        handler.removeCallbacks(runnable!!)
                        handler.postDelayed(runnable!!,5000)
                        return@apply
                    }
                    if(started){
                        setCurrentItem(currentItem+1,true)
                    }
                    started=true
                    handler.postDelayed(runnable!!,5000)

                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        viewPagerHandler!!.post(runnable!!)
    }

    private fun setLayoutVisibility(show: Boolean, showToast: Boolean, message: String?) {
        if (showToast) {
            binding.mainLayout.visibility = View.GONE
            Snackbar.make(requireView(),message.toString(),Snackbar.LENGTH_INDEFINITE).show()
            return
        }
        if (show) {
            setTints()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTints() {
        if((searchLanguage == "tr") && ((selectedResponse?.overview == null) || (selectedResponse?.overview?.isEmpty() == true))){
            searchLanguage = "en"
            if (isItInMovieList) {
                viewModel.resetData()
                viewModel.getDetails(movieSearch, selectedId!!, searchLanguage)
            } else {
                viewModel.resetData()
                viewModel.getDetails(tvSearch, selectedId!!, searchLanguage)
            }
            observeLiveData()
        }
        val vibrantColor=ColorStateList.valueOf(vibrantColor!!)
        val darkMutedColor=ColorStateList.valueOf(darkMutedColor!!)
        binding.saveButton.backgroundTintList = vibrantColor
        binding.saveButton.imageTintList= darkMutedColor
        binding.ratingBar.progressTintList=vibrantColor
        binding.linearLayout.backgroundTintList=vibrantColor
        binding.tabLayout.setTabTextColors(ContextCompat.getColor(requireContext(),R.color.white),DetailsFragment.vibrantColor!!)
        binding.tabLayout.setSelectedTabIndicatorColor(DetailsFragment.vibrantColor!!)
        binding.trailerButton.backgroundTintList = vibrantColor
        binding.trailerButton.imageTintList = darkMutedColor
        binding.ratingBar.rating =( (selectedResponse?.vote_average?.toFloat()?.div(2f))?:0f).also {
            seasonRatingFloat=it
        }
        selectedResponse?.let { it ->

            it.genres?.let {
            var name=""
                for(i in it.indices){
                    name += if (i == it.size-1){
                        it[i].name
                    } else{
                        "${it[i].name} , "
                    }
                    if(i == 5) break
                }
            seasonGenres=name
            binding.genreText.text=name
            }
            var text=""
            if (it.episode_run_time?.isNotEmpty() == true) {
                text= it.episode_run_time.last().toString()
            }
            binding.runtimeText.text = "${it.runtime?:text} min"
            binding.ratingText.text = ( it.vote_average?.toBigDecimal()?.setScale(1, RoundingMode.UP)?.toString() + "/10").also {
                seasonRating=it
            }
            binding.nameText.text = it.original_title?:it.original_name
            binding.yearText.text = it.release_date?:it.last_air_date
        }
    }


    private fun watchList(isFavorite:Boolean){
        val isTvShow=!isItInMovieList
        selectedResponse?.let { detailResponse ->
            val roomEntity= RoomEntity(isFavorite,detailResponse.name?:detailResponse.original_name?:detailResponse.original_title?:"",
                detailResponse.last_air_date?:detailResponse.release_date!!,
                detailResponse.poster_path!!,
                detailResponse.vote_average!!,
                isTvShow,
                detailResponse.id!!.toLong())
            if(!isItInDatabase){
                viewModel.insertRoomEntity(roomEntity)
                showSnackBar(resources.getString(R.string.added_history),roomEntity,"Save")

            }else{
                viewModel.deleteRoomEntity(detailResponse.id)
                showSnackBar(resources.getString(R.string.delete_history),roomEntity,"Delete")

            }
        }

    }

    private fun showSnackBar(message:String,entity: RoomEntity,action:String){
        Snackbar.make(requireView(),message,Snackbar.LENGTH_SHORT).setAction(resources.getString(R.string.undo)){
            if (action=="Save"){
                viewModel.deleteRoomEntity(entity.field_id.toInt())
            }else{
                viewModel.insertRoomEntity(entity)
            }
        }.show()
    }

    private fun favorite(){
         if (isItInFavorite){
            viewModel.updateFavorite(selectedResponse!!.id!!,false)
         }else{
            if (!isItInDatabase){
                watchList(true)
            }else{
                viewModel.updateFavorite(selectedResponse!!.id!!,true)
            }
            binding.favoriteLottie.visibility=View.VISIBLE
            binding.favoriteLottie.playAnimation()
         }
    }

    override fun onDestroyView() {
        job?.cancel()
        job2?.cancel()
        binding.favoriteLottie.removeAnimatorListener(animatorListener)
        isSingleUrl=false
        fragmentViewPagerAdapter = null
        fragmentViewPager?.adapter=fragmentViewPagerAdapter
        viewPagerHandler?.removeCallbacks(runnable!!)
        viewPagerHandler=null
        runnable=null
        _binding=null
        super.onDestroyView()
    }


}