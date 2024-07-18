package com.example.api.ui.viewmodel

import androidx.lifecycle.*
import com.example.api.data.Repository
import com.example.api.data.modelclass.album.AlbumResponse
import com.example.api.data.modelclass.album.AlbumResponseItem
import com.example.api.utils.Constant
import com.example.api.utils.Event
import com.example.api.utils.Resource
import com.example.api.utils.ResponseCodeManager
import com.example.api.utils.logThis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject
constructor(private val repository: Repository) :
    ViewModel() {

    private val mAlbumResponse: MutableLiveData<Event<Resource<List<AlbumResponseItem>>>> =
        MutableLiveData()
    val albumResponse: LiveData<Event<Resource<List<AlbumResponseItem>>>> get() = mAlbumResponse



    fun getAlbum() = viewModelScope.launch(Dispatchers.IO) {
        mAlbumResponse.postValue(Event(Resource.Loading()))
        try {
            val response = repository.getPhotos()
            if (response.isSuccessful) {
                logThis("in viewmodel success--------------------------->$response")
                response.body()?.let { resultResponse ->
                    mAlbumResponse.postValue(Event(Resource.Success(resultResponse)))
                }
            } else {
                logThis("in viewmodel error--------------------------->$response")
                mAlbumResponse.postValue(
                    Event(
                        Resource.Error(
                            ResponseCodeManager.checkRetrofitApiResponse(
                                response
                            )
                        )
                    )
                )
            }
        } catch (t: Throwable) {
            logThis("in viewmodel catch block--------------------------->$t")
            logThis("in catch block------------------")
            when (t) {
                is IOException -> mAlbumResponse.postValue(
                    Event(Resource.Error(Constant.NETWORK_FAILURE))
                )

                else -> mAlbumResponse.postValue(
                    Event(Resource.Error(Constant.CONVERSION_FAILURE))
                )
            }
        }
    }
}