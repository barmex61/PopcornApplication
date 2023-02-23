package com.fatih.popcorn.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fatih.popcorn.entities.remote.videoresponse.VideoResponse
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


    fun getVideos(name: String, id: Int)=viewModelScope.launch {
        _videoResponse.value= Resource.loading(null)
        _videoResponse.value=popcornRepository.getVideos(name, id)
        println(popcornRepository.getVideos(name, id).status)
        println(popcornRepository.getVideos(name, id).message)
    }
}