package com.fatih.popcorn.viewmodel

import androidx.lifecycle.*
import com.fatih.popcorn.entities.local.RoomEntity
import com.fatih.popcorn.entities.remote.detailresponse.DetailResponse
import com.fatih.popcorn.entities.remote.imageresponse.ImageResponse
import com.fatih.popcorn.other.Resource
import com.fatih.popcorn.repository.PopcornRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsFragmentViewModel @Inject constructor(private val popcornRepo:PopcornRepositoryInterface):ViewModel() {

    private var _detailResponse=MutableLiveData<Resource<DetailResponse>>()
    val detailResponse:LiveData<Resource<DetailResponse>>
    get() = _detailResponse

    private var _imageResponse=MutableLiveData<Resource<ImageResponse>>()
    val imageResponse:LiveData<Resource<ImageResponse>>
    get() = _imageResponse

    val roomEntityList=popcornRepo.getAllRoomEntity()

    private var _isItInDatabase= MutableLiveData<Boolean>()
    val isItInDatabase:LiveData<Boolean>
    get() = _isItInDatabase
    fun getDetails(searchName:String,id:Int,language:String) {

       _detailResponse= popcornRepo.getDetails(searchName,id, language).catch {
           it.printStackTrace()
       }.asLiveData(viewModelScope.coroutineContext) as MutableLiveData<Resource<DetailResponse>>
       getImages(searchName,id)
    }

    private fun getImages(searchName : String , id : Int)=viewModelScope.launch {
        _imageResponse.value=Resource.loading(null)
        _imageResponse = popcornRepo.getImages(searchName,id).catch {
            it.printStackTrace()
        }.asLiveData(viewModelScope.coroutineContext) as MutableLiveData<Resource<ImageResponse>>

    }

    fun insertRoomEntity(roomEntity: RoomEntity)=viewModelScope.launch(Dispatchers.Default) {
        popcornRepo.insertRoomEntity(roomEntity)
    }
    fun deleteRoomEntity(roomEntity: RoomEntity)=viewModelScope.launch(Dispatchers.Default) {
        popcornRepo.deleteRoomEntity(roomEntity)
    }

    fun isItIntDatabase(idInput:Int)  = viewModelScope.launch(Dispatchers.Default){
        _isItInDatabase.postValue(popcornRepo.getSelectedRoomEntity(idInput)?.let {
            true
        }?:false
        )
    }

}