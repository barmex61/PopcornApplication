package com.fatih.popcorn.ui

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.PorterDuff
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2

import com.fatih.popcorn.R
import com.fatih.popcorn.adapter.CastRecyclerViewAdapter
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
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.Runnable
import java.math.RoundingMode
import java.util.*
import javax.inject.Inject
import kotlin.Exception

@AndroidEntryPoint
class DetailsFragment @Inject constructor() : Fragment(R.layout.fragment_details) {

    @Inject
    lateinit var castAdapter:CastRecyclerViewAdapter
    @Inject
    lateinit var crewAdapter:CastRecyclerViewAdapter

    private lateinit var viewModel: DetailsFragmentViewModel
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
    private var vibrantColor: Int? = null
    private var darkMutedColor: Int? = null
    private var isItInDatabase = false
    private var job:Job?=null
    private var fragmentViewPagerAdapter:DetailsFragmentViewPagerAdapter?=null
    private var job2:Job?=null
    private var posterList= listOf<String>()
    private var backgroundList= listOf<String>()
    private var searchLanguage= language
    private var fragmentList : List<Fragment>? =null
    private var fragmentViewPager:ViewPager2?=null
    private val handler = CoroutineExceptionHandler{ _,throwable->
        println("Caught exception $throwable")
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        doInitialization()
        return binding.root
    }

    private fun doInitialization() {
        setStatusBarPadding()
        fragmentViewPager=binding.detailsViewPager
        setupPosterViewPager(posterList,backgroundList)
        //setupCastRecyclerView()
        //binding.backgroundImage.colorFilter = colorMatrixColorFilter
        viewModel = ViewModelProvider(this)[DetailsFragmentViewModel::class.java]
        //binding.watchList.setOnClickListener { watchList() }
        binding.watchListButton.setOnClickListener { findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToWatchListFragment()) }
        binding.backButton.setOnClickListener { findNavController().navigateUp() }
        //binding.episodesImage.setOnClickListener {view-> goEpisodes(view) }
        arguments?.let {
            selectedId = DetailsFragmentArgs.fromBundle(it).id
            darkMutedColor = DetailsFragmentArgs.fromBundle(it).darkMutedColor
            vibrantColor = DetailsFragmentArgs.fromBundle(it).vibrantColor
        }
        if (checkIsItInMovieListOrNot()) {
            viewModel.getDetails(movieSearch, selectedId!!, searchLanguage)
        } else {
            viewModel.getDetails(tvSearch, selectedId!!, searchLanguage)
        }
        observeLiveData()
    }

    private fun setupFragmentViewPager(){
        val bundle=Bundle()
        bundle.putSerializable("detailResponse",selectedResponse!!)
        fragmentList= listOf(AboutFragment().apply {
            arguments=bundle
        },CastFragment(),ReviewFragment(),RecommendFragment(),FamiliarFragment(),TrailerFragment())
        _myFragmentManager=childFragmentManager
        fragmentViewPagerAdapter=DetailsFragmentViewPagerAdapter(fragmentList!!,myFragmentManager,lifecycle)
        fragmentViewPager!!.adapter=fragmentViewPagerAdapter
        TabLayoutMediator(binding.tabLayout,binding.detailsViewPager,true,true){tab,position->
           when(position){
               0->{tab.text=resources.getString(R.string.hakkinda)}
               1->{tab.text=resources.getString(R.string.actor)}
               2->{tab.text=resources.getString(R.string.comment)}
               3->{tab.text=resources.getString(R.string.recommendation)}
               4->{tab.text=resources.getString(R.string.familiar)}
               5->{tab.text=resources.getString(R.string.trailer)}
           }

        }.attach()

    }

    /* private fun setupCastRecyclerView(){
        crewRecyclerView=binding.crewRecyclerView
        crewRecyclerView!!.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        crewRecyclerView!!.adapter=crewAdapter
        castRecyclerView=binding.castRecyclerView
        castRecyclerView!!.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        castRecyclerView!!.adapter=castAdapter
    } */
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

        viewModel.isItInDatabase.observe(viewLifecycleOwner){
            if(it){
                isItInDatabase=it
                //binding.watchList.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.white))
            }else{
                isItInDatabase=it
                //binding.watchList.imageTintList =ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.gray))
            }
        }

        viewModel.creditsResponse.observe(viewLifecycleOwner){
            when(it.status){
                Status.SUCCESS->{

                    it.data?.let {
                        job2?.cancel()
                        job2=lifecycleScope.launch(Dispatchers.Main + handler){
                            val castList=async(Dispatchers.Default){
                                it.cast.filter {
                                    !it.profile_path.isNullOrEmpty() && it.profile_path != "null"
                                }.distinctBy {
                                    it.profile_path
                                }.map { cast->
                                    Triple(cast.name,cast.character,cast.profile_path!!)
                                }
                            }
                            val crewList=async(Dispatchers.Default){
                                it.crew.filter {
                                    !it.profile_path.isNullOrEmpty() && it.profile_path != "null"
                                }.distinctBy {
                                    it.profile_path
                                }.map { crew->
                                    Triple(crew.name,crew.job,crew.profile_path!!)
                                }
                            }
                            crewAdapter.castList=crewList.await()
                            castAdapter.castList=castList.await()
                        }
                    }
                }
                else->Unit
            }
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
                            setImageAnimations(backgroundList)
                            setupPosterViewPager(posterList,backgroundList)
                        }
                    }
                    else->Unit
                }
            }
        }
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
        }
        binding.posterImageViewPager.apply {
            adapter=viewPagerAdapter
            offscreenPageLimit=3
            binding.circleIndicator.setViewPager(this)
        }
        viewPagerHandler= Handler(Looper.getMainLooper())
        var started=false
        runnable= Runnable {
            try {
                binding.posterImageViewPager.apply {
                    if (currentItem==this.adapter!!.itemCount-1){
                        currentItem=0
                        setCurrentItem(currentItem,true)
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


    /* private fun startAnimation(bitmapList: List<Bitmap>){
        val imageView=binding.backgroundImage
        imageView.scaleType=ImageView.ScaleType.FIT_XY
        imageView.clearAnimation()
        val size=bitmapList.size-1
        var index=0
        val animation=AnimationUtils.loadAnimation(requireContext(),android.R.anim.fade_in).apply {
            repeatCount=Animation.INFINITE
            repeatMode=Animation.REVERSE
            duration=2000L
            setAnimationListener( object :Animation.AnimationListener{
                var repeatCount=0
                    override fun onAnimationEnd(animation: Animation?) {
                    }
                    override fun onAnimationRepeat(animation: Animation?) {
                        if (index<size && repeatCount%2==0){
                            index++
                            imageView.setImageBitmap(bitmapList[index])
                        }else if (index==size){
                            repeatCount=0
                            index=0
                        }
                        repeatCount++
                    }

                    override fun onAnimationStart(animation: Animation?) {
                        repeatCount++
                        imageView.setImageBitmap(bitmapList[index])
                    }
                })
        }
        imageView.startAnimation(animation)
    }  */

    private fun setImageAnimations(imageUrls:List<String>){
        job?.cancel()
        val bitmapList= mutableListOf<Bitmap>()
        if (imageUrls.isNotEmpty()){
            job=lifecycleScope.launch (Dispatchers.Main + handler){
                val subList=if (imageUrls.size>=9) imageUrls.subList(0,9) else imageUrls.subList(0,imageUrls.size-1)
                try {
                    for (url in subList){
                        val job=async(Dispatchers.IO){
                            Picasso.get().load("https://image.tmdb.org/t/p/original$url").get()
                        }
                        bitmapList.add(job.await())
                    }
                    withContext(Dispatchers.Main){
                        if (bitmapList.isNotEmpty()){
                           // startAnimation(bitmapList)
                        }
                    }
                }catch (e:Exception){
                    println(e.message)
                }
            }
        }

        /*    job=lifecycleScope.launch(Dispatchers.IO + handler) {
                try {
                    while (true){
                        for(url in imageUrls){
                            imageView.alpha=0f
                            val job= async(Dispatchers.IO) {
                                Picasso.get().load("https://image.tmdb.org/t/p/original$url").get()
                            }
                            val bitmap=job.await()
                            val job2=launch(Dispatchers.Main){
                                imageView.apply {
                                    setImageBitmap(bitmap)
                                    animate().alpha(1f).setDuration(2000L).withEndAction {
                                        this.animate().alpha(0f).setDuration(2000L).start()
                                    }.start()
                                }
                            }
                            delay(4000L)
                            job.cancel()
                            job2.cancel()
                        }
                    }

                }
                catch (e:Exception){
                    println(e.message)
                }
            } */
    }
    private fun setLayoutVisibility(show: Boolean, showToast: Boolean, message: String?) {
        if (showToast) {
            binding.mainLayout.visibility = View.GONE
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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
            if (checkIsItInMovieListOrNot()) {
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
        binding.imgPlay.backgroundTintList = vibrantColor
        binding.imgPlay.imageTintList= darkMutedColor
        //binding.ratingBar.progressTintMode=PorterDuff.Mode.SRC_ATOP
        binding.ratingBar.progressTintList=vibrantColor
        binding.linearLayout.backgroundTintList=vibrantColor
        binding.tabLayout.setTabTextColors(ContextCompat.getColor(requireContext(),R.color.white),this.vibrantColor!!)
        binding.tabLayout.setSelectedTabIndicatorColor(this.vibrantColor!!)
        binding.saveImage.backgroundTintList = vibrantColor
        binding.saveImage.imageTintList = darkMutedColor
       // binding.trailerImage.imageTintList = vibrantColor
        //binding.trailerText.setTextColor(darkMutedColor)
        //binding.videoImage.imageTintList=darkMutedColor
        //binding.reviewImage.imageTintList = darkMutedColor
        //binding.reviewText.setTextColor(vibrantColor)
        binding.ratingBar.rating = (selectedResponse?.vote_average?.toFloat()?.div(2f))?:0f
        selectedResponse?.let { it ->

            it.genres?.let {
            var name=""
                for(i in it.indices){
                    if (i == it.size-1){
                        name += it[i]
                    }
                    else{
                        name += "${it[i]} , "
                    }
                    if(i == 5) break
                }
            }
            var text=""
            if (it.episode_run_time?.isNotEmpty() == true) {
                text= it.episode_run_time.last().toString()
            }
            //binding.textDescription.text = it.overview
            binding.runtimeText.text = "${it.runtime?:text} min"
            binding.ratingText.text = it.vote_average?.toBigDecimal()?.setScale(1, RoundingMode.UP)?.toString() + "/10"
            binding.nameText.text = it.original_title?:it.original_name
            binding.yearText.text = it.release_date?:it.last_air_date

            if (checkIsItInMovieListOrNot()) {
              //  binding.episodesImage.visibility = View.INVISIBLE
                //binding.episodesText.visibility = View.INVISIBLE
            } else {
                //binding.episodesImage.visibility = View.VISIBLE
                //binding.episodesText.visibility = View.VISIBLE
            }
        }
    }


    private fun watchList(){
        val isTvShow=!checkIsItInMovieListOrNot()
        if(!isItInDatabase){
            selectedResponse?.let {detailResponse->
                val roomEntity= RoomEntity(
                    detailResponse.last_air_date?:detailResponse.release_date!!,
                    detailResponse.poster_path!!,
                    detailResponse.vote_average!!,isTvShow,
                    detailResponse.id!!.toLong())
                viewModel.insertRoomEntity(roomEntity)
                viewModel.isItIntDatabase(detailResponse.id)
            }
            if(isTvShow){
                Toast.makeText(requireContext(),"Tv show added into Watchlist",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(),"Movie added into Watchlist",Toast.LENGTH_SHORT).show()
            }

        }else{
            selectedResponse?.let {
                val roomEntity= RoomEntity(it.last_air_date?:it.release_date!!, it.poster_path!!, it.vote_average!!,isTvShow, it.id!!.toLong())
                viewModel.deleteRoomEntity(roomEntity)
                viewModel.isItIntDatabase(it.id)
            }
            if(isTvShow){
                Toast.makeText(requireContext(),"Tv show deleted from Watchlist",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(),"Movie deleted from Watchlist",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        job?.cancel()
        job2?.cancel()
        fragmentList=null
        fragmentViewPager?.adapter=null
        fragmentViewPagerAdapter=null
        viewPagerHandler?.removeCallbacks(runnable!!)
        binding.detailsViewPager.adapter=null
        _myFragmentManager=null
        fragmentViewPager=null
        viewPagerHandler=null
        runnable=null
        _binding=null
        super.onDestroyView()
    }



}