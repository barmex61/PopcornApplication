package com.fatih.popcorn.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fatih.popcorn.entities.remote.DiscoverResponse
import com.fatih.popcorn.other.*
import com.fatih.popcorn.other.Constants.stateList
import com.fatih.popcorn.repository.PopcornRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(private val popcornRepo:PopcornRepositoryInterface):ViewModel() {

    private val _discoverData=MutableLiveData<Resource<DiscoverResponse?>>()
    val discoverData:LiveData<Resource<DiscoverResponse?>>
        get() = _discoverData

    var currentPage=MutableLiveData<Int>(1)

    fun getMovies(sort_by:String,genres:String)=viewModelScope.launch{
        if(stateList.last()!=State.MOVIE){
            _discoverData.value?.let {
                it.data=null
            }
        }
        stateList.addFilter(State.MOVIE)
        _discoverData.value=Resource.loading(_discoverData.value?.data)
        val response=popcornRepo.getMovies(sort_by, currentPage.value!!, genres)
        when(response.status){
            Status.SUCCESS->{
                _discoverData.value= Resource.success(_discoverData.value!!.data.add(response.data!!))
            }
            Status.ERROR->{
                _discoverData.value= Resource.error("Error occurred")
            }
            else->Unit
        }
    }

    fun getTvShows(sort_by:String,genres:String)=viewModelScope.launch{
        if(stateList.last()!=State.TV_SHOW){
            _discoverData.value?.let {
                it.data=null
            }
        }
        stateList.addFilter(State.TV_SHOW)
        _discoverData.value=Resource.loading(_discoverData.value?.data)
        val response=popcornRepo.getTvShows(sort_by, currentPage.value!!, genres)
        when(response.status){
            Status.SUCCESS->{
                _discoverData.value= Resource.success(_discoverData.value!!.data.add(response.data!!))
            }
            Status.ERROR->{
                _discoverData.value= Resource.error("Error occurred")
            }
            else->Unit
        }
    }

    fun search(name:String,query: String)=viewModelScope.launch{
        stateList.addFilter(State.SEARCH)
        val response=popcornRepo.search(name, query, currentPage.value!!)
        _discoverData.value=Resource.loading(response.data)
        when(response.status){
            Status.SUCCESS->{
                println("${name} ${query}")
                println(_discoverData.value!!.data.add(response.data!!))
                _discoverData.value= Resource.success(_discoverData.value!!.data.add(response.data!!))
            }
            Status.ERROR->{
                _discoverData.value= Resource.error("Error occurred")
            }
            else->Unit
        }
    }

}