package com.example.api.data

import com.example.api.data.network.ApiInterface
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: ApiInterface) {

    suspend fun getPhotos() = apiService.getPhotos()
}