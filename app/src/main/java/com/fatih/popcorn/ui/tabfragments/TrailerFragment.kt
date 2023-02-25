package com.fatih.popcorn.ui.tabfragments

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fatih.popcorn.R
import com.fatih.popcorn.adapter.YoutubeVideoAdapter
import com.fatih.popcorn.databinding.FragmentTrailerBinding
import com.fatih.popcorn.entities.remote.youtuberesponse.İtem
import com.fatih.popcorn.other.Constants
import com.fatih.popcorn.other.Constants.movieSearch
import com.fatih.popcorn.other.Constants.tvSearch
import com.fatih.popcorn.other.Status
import com.fatih.popcorn.viewmodel.TrailerFragmentViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.DefaultPlayerUiController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class TrailerFragment @Inject constructor(): Fragment(R.layout.fragment_trailer) {

    private var _binding:FragmentTrailerBinding?=null
    private val binding:FragmentTrailerBinding
    get() = _binding!!
    private lateinit var viewModel: TrailerFragmentViewModel
    private var myVideoId:String=""
    private var selectedId=0
    private var part="snippet,contentDetails,statistics"
    private lateinit var youtubePlayerView: YouTubePlayerView
    private var listener: AbstractYouTubePlayerListener?=null
    private var youtubeVideoAdapter : YoutubeVideoAdapter?=null
    private var myYoutubePlayer:YouTubePlayer?=null
    private var mCurrentTime=0f
    private var mCurrentVideoId=""
    private val tracker=YouTubePlayerTracker()
    private var isThereAnyVideoUrl=false
    private var isRotated=false

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
            _binding= FragmentTrailerBinding.inflate(inflater,container,false)
            doInitialization()
            mCurrentVideoId=savedInstanceState?.getString("video_id")?:myVideoId
            mCurrentTime=savedInstanceState?.getFloat("current_time")?:mCurrentTime
            isRotated=savedInstanceState?.getBoolean("isRotated")?:isRotated
            return binding.root
        }

        @SuppressLint("DiscouragedApi", "InternalInsetResource")
        private fun setupStatusBar(isPortrait:Boolean){
            val statusBarHeightId = resources.getIdentifier("status_bar_height", "dimen", "android")
            val statusBarHeight = resources.getDimensionPixelSize(statusBarHeightId) +10
            if (isPortrait){
                binding.trailerMainLayout.updatePadding(top = statusBarHeight)
            }else{
                binding.trailerMainLayout.updatePadding(top = 0, bottom = 0, left = 0, right = 0)
            }
        }

        private fun doInitialization(){
            setupStatusBar(controlOrientation())
            viewModel= ViewModelProvider(requireActivity())[TrailerFragmentViewModel::class.java]
            youtubePlayerView=binding.youtubePlayer
            youtubePlayerView.enableAutomaticInitialization=false
            if (controlOrientation()){
                youtubePlayerView.exitFullScreen()
            }else{
                youtubePlayerView.enterFullScreen()
            }
            lifecycle.addObserver(youtubePlayerView)
            listener=object:AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    myYoutubePlayer=youTubePlayer
                    myYoutubePlayer!!.addListener(tracker)
                    youtubePlayerView.setCustomPlayerUi(DefaultPlayerUiController(youtubePlayerView,myYoutubePlayer!!).apply {
                        showUi(true)
                    }.rootView)

                    if (mCurrentVideoId.isNotEmpty() && mCurrentTime != 0f){
                        myYoutubePlayer!!.loadVideo(mCurrentVideoId,mCurrentTime)
                    }else {
                        if(viewModel.itemList.value != null && viewModel.itemList.value!!.isNotEmpty())
                        myYoutubePlayer!!.cueVideo(viewModel.itemList.value?.get(0)?.id?:"",0f)

                    }
                }

                override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                    super.onStateChange(youTubePlayer, state)
                    println(state.name)
                }

                override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {
                    super.onVideoId(youTubePlayer, videoId)
                }
            }
            val iFramePlayerOptions=IFramePlayerOptions.Builder().controls(0).build()
            youtubePlayerView.initialize(listener as AbstractYouTubePlayerListener,iFramePlayerOptions)
            youtubeVideoAdapter= YoutubeVideoAdapter(R.layout.fragment_trailer_row)

            youtubeVideoAdapter!!.setOnItemClickListener {position,item->
                viewModel.updateList(position,item){
                    youtubeVideoAdapter?.list=it
                    myYoutubePlayer?.loadVideo(youtubeVideoAdapter!!.list[position].id,0f)
                }
            }
            binding.youtubeRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
            binding.youtubeRecyclerView?.adapter = youtubeVideoAdapter

            arguments?.let {
                selectedId=TrailerFragmentArgs.fromBundle(it).id
            }

            if (Constants.checkIsItInMovieListOrNot()){
                viewModel.getVideos(movieSearch,selectedId)
            }else{
                viewModel.getVideos(tvSearch,selectedId)
            }

            observeLiveData()

        }
        private fun observeLiveData(){
            viewModel.videoResponse.observe(viewLifecycleOwner){resource->
                if(resource!=null){
                    when(resource.status){
                        Status.SUCCESS->{
                            resource.data?.let {
                                it.results.map {it->
                                    if(it.site=="YouTube"){
                                        binding.youtubePlayer.visibility=View.VISIBLE
                                        binding.youtubeRecyclerView?.visibility=View.VISIBLE
                                        isThereAnyVideoUrl=true
                                        myVideoId = if(myVideoId.isEmpty()){
                                            it.key
                                        }else{
                                            myVideoId+","+it.key
                                        }
                                    }
                                }
                                if (!isThereAnyVideoUrl){
                                    binding.trailerLottie.playAnimation()
                                }
                                myVideoId.let {
                                    viewModel.getYoutubeResponse(part,it)
                                }
                            }
                        }else->Unit
                    }
                }
            }

           viewModel.youtubeResponse.observe(viewLifecycleOwner){resources->
                if(resources!=null){
                    when(resources.status){

                        Status.SUCCESS->{
                            resources.data?.let { it->
                               viewModel.itemList.value=it.items.toMutableList()
                               youtubeVideoAdapter?.list=it.items
                            }
                        }
                        else->Unit
                    }
                }
            }
        }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("video_id", tracker.videoId)
        outState.putFloat("current_time", tracker.currentSecond)
        outState.putBoolean("isRotated",true)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        if (!isRotated){
            viewModel.resetData()
        }
        youtubeVideoAdapter=null
        isThereAnyVideoUrl=false
        myYoutubePlayer?.removeListener(listener as AbstractYouTubePlayerListener)
        _binding=null
        youtubePlayerView.release()
        super.onDestroy()
    }

    private fun controlOrientation() = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

}