package com.fatih.popcorn.ui.tabfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
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
        private var videoUrlArrayList=ArrayList<String>()
        private var part="snippet,contentDetails,statistics"
        private lateinit var youtubePlayerView: YouTubePlayerView
        private var position=0
        private var listener: AbstractYouTubePlayerListener?=null
        private var youtubeVideoAdapter : YoutubeVideoAdapter?=null

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
            _binding= FragmentTrailerBinding.inflate(inflater,container,false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            doInitialization()

        }
        private fun doInitialization(){
            youtubeVideoAdapter= YoutubeVideoAdapter(R.layout.fragment_trailer_row)
            youtubeVideoAdapter!!.setOnItemClickListener {
                position=it
                binding.youtubePlayer.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
                    override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.cueVideo(youtubeVideoAdapter!!.list[position].id,0f)
                    }
                })

            }
            arguments?.let {
                selectedId=TrailerFragmentArgs.fromBundle(it).id
            }
            viewModel= ViewModelProvider(requireActivity())[TrailerFragmentViewModel::class.java]
            if (Constants.checkIsItInMovieListOrNot()){
                viewModel.getVideos(movieSearch,selectedId)
            }else{
                viewModel.getVideos(tvSearch,selectedId)
            }
            binding.youtubeRecyclerView.layoutManager= LinearLayoutManager(requireContext())
            binding.youtubeRecyclerView.adapter=youtubeVideoAdapter

            observeLiveData()
            youtubePlayerView=binding.youtubePlayer
            youtubePlayerView.apply {
                enableAutomaticInitialization=true
            }
            lifecycle.addObserver(youtubePlayerView)
            listener=object:AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    youTubePlayer.cueVideo(videoUrlArrayList[0],0f)
                }

                override fun onStateChange(
                    youTubePlayer: YouTubePlayer,
                    state: PlayerConstants.PlayerState
                ) {
                    super.onStateChange(youTubePlayer, state)
                    println("${state}")
                }

                override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {
                    super.onVideoId(youTubePlayer, videoId)
                    println("videoıd")
                }

            }

            youtubePlayerView.addYouTubePlayerListener(listener as AbstractYouTubePlayerListener)

        }
        private fun observeLiveData(){
            viewModel.videoResponse.observe(viewLifecycleOwner){resource->
                if(resource!=null){
                    when(resource.status){
                        Status.SUCCESS->{
                            resource.data?.let {
                                it.results.map {it->
                                    if(it.site=="YouTube"){
                                        videoUrlArrayList.add(it.key)
                                        myVideoId = if(myVideoId.isEmpty()){
                                            it.key
                                        }else{
                                            myVideoId+","+it.key
                                        }
                                    }
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
                        Status.ERROR->{
                            binding.isLoading=false
                            Toast.makeText(requireContext(),resources.message,Toast.LENGTH_LONG).show()
                        }
                        Status.LOADING->{
                            binding.isLoading=true
                        }
                        Status.SUCCESS->{
                            binding.isLoading=false
                            resources.data?.let { it->
                                youtubeVideoAdapter?.list=it.items
                            }
                        }

                    }
                }
            }
        }

        override fun onDestroy() {
            youtubeVideoAdapter=null
            _binding=null
            videoUrlArrayList.clear()
            super.onDestroy()
        }
}