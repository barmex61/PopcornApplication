package com.fatih.popcorn.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fatih.popcorn.entities.remote.DiscoverResponse
import com.fatih.popcorn.other.Resource
import com.fatih.popcorn.other.Status
import com.fatih.popcorn.repository.PopcornRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(private val popcornRepo:PopcornRepositoryInterface):ViewModel() {

    private val _discoverData=MutableLiveData<Resource<DiscoverResponse>>()
    val discoverData:LiveData<Resource<DiscoverResponse>>
        get() = _discoverData

    fun getMovies(page:Int,sort_by:String,genres:String)=viewModelScope.launch{
        _discoverData.value=Resource.loading()
        _discoverData.value=popcornRepo.getMovies(sort_by,page,genres)
    }

    fun getTvShows(page:Int,sort_by:String,genres:String)=viewModelScope.launch{
        _discoverData.value=Resource.loading()
        _discoverData.value=popcornRepo.getTvShows(sort_by,page,genres)
    }

    fun search(name:String,query: String,page:Int)=viewModelScope.launch{
        _discoverData.value= Resource.loading()
        _discoverData.postValue(popcornRepo.search(name, query, page))
    }

}