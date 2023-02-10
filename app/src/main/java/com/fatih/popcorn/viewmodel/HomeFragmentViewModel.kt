package com.fatih.popcorn.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fatih.popcorn.entities.remote.movie.MovieResponse
import com.fatih.popcorn.entities.remote.movie.MovieResult
import com.fatih.popcorn.entities.remote.tvshow.TvShowResponse
import com.fatih.popcorn.entities.remote.tvshow.TvShowResult
import com.fatih.popcorn.other.Resource
import com.fatih.popcorn.repository.PopcornRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(private val popcornRepo:PopcornRepositoryInterface):ViewModel() {

    private val _movieList=MutableLiveData<Resource<MovieResponse>>()
    val movieList:LiveData<Resource<MovieResponse>>
        get() = _movieList

    private val _tvShowList=MutableLiveData<Resource<TvShowResponse>>()
    val tvShowList:LiveData<Resource<TvShowResponse>>
        get() = _tvShowList
    fun getMovies(page:Int,sort_by:String,genres:String)=viewModelScope.launch{
        _movieList.value=Resource.loading()
        _movieList.value=popcornRepo.getMovies(sort_by,page,genres)
    }

    fun getTvShows(page:Int,sort_by:String,genres:String)=viewModelScope.launch{
        _tvShowList.value=Resource.loading()
        _tvShowList.value=popcornRepo.getTvShows(sort_by,page,genres)
    }

}