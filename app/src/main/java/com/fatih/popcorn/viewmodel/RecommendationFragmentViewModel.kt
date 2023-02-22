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
class RecommendationFragmentViewModel @Inject constructor(private val popcornRepository: PopcornRepositoryInterface) :ViewModel(){


    private val _discoverResponse=MutableLiveData<Resource<DiscoverResponse>>()
    val discoverResponse:LiveData<Resource<DiscoverResponse>>
    get() = _discoverResponse

    var recommendationCurrentPage=MutableLiveData(1)

    fun getRecommendations(name: String, id: Int)=viewModelScope.launch {
        _discoverResponse.value= Resource.loading(null)
        val response=popcornRepository.getRecommendations(name, id, recommendationCurrentPage.value!!)
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


}