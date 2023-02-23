package com.fatih.popcorn.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fatih.popcorn.entities.remote.discoverresponse.DiscoverResponse
import com.fatih.popcorn.other.Resource
import com.fatih.popcorn.other.Status
import com.fatih.popcorn.other.recommend
import com.fatih.popcorn.repository.PopcornRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FamiliarFragmentViewModel @Inject constructor(private val popcornRepository: PopcornRepositoryInterface) :ViewModel(){


    private var _discoverResponse=MutableLiveData<Resource<DiscoverResponse>>()
    val discoverResponse:LiveData<Resource<DiscoverResponse>>
    get() = _discoverResponse

    var familiarCurrentPage=MutableLiveData(1)

    fun getFamiliars(name: String, id: Int)=viewModelScope.launch {
        _discoverResponse.value= Resource.loading(_discoverResponse.value?.data)
        val response=popcornRepository.getFamiliars(name, id, familiarCurrentPage.value!!)
        when(response.status){
            Status.SUCCESS->{
                _discoverResponse.value= Resource.success(_discoverResponse.value?.data.recommend(response.data!!.apply {
                    recommendationId=id
                }))
            }
            Status.ERROR->{
                _discoverResponse.value= Resource.error(response.message)
            }
            else->Unit
        }
    }

    fun resetData(){
        _discoverResponse=MutableLiveData<Resource<DiscoverResponse>>()
        familiarCurrentPage=MutableLiveData(1)
    }

}