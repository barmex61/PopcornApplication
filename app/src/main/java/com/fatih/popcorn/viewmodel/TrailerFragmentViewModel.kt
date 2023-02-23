package com.fatih.popcorn.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fatih.popcorn.entities.remote.videoresponse.VideoResponse
import com.fatih.popcorn.entities.remote.youtuberesponse.YoutubeResponse
import com.fatih.popcorn.other.Resource
import com.fatih.popcorn.repository.PopcornRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrailerFragmentViewModel @Inject constructor(private val popcornRepository: PopcornRepositoryInterface) : ViewModel() {

    private val _videoResponse=MutableLiveData<Resource<VideoResponse>>()
    val videoResponse:LiveData<Resource<VideoResponse>>
        get() = _videoResponse

    private val _youtubeResponse=MutableLiveData<Resource<YoutubeResponse>>()
    val youtubeResponse:LiveData<Resource<YoutubeResponse>>
        get() = _youtubeResponse


    fun getVideos(name: String, id: Int)=viewModelScope.launch {
        _videoResponse.value= Resource.loading(null)
        _videoResponse.value=popcornRepository.getVideos(name, id)
    }

    fun getYoutubeResponse(part:String,id:String)=viewModelScope.launch {
        _youtubeResponse.value= Resource.loading(_youtubeResponse.value?.data)
        _youtubeResponse.value=popcornRepository.getYoutubeVideoDetails(part, id)
    }
}