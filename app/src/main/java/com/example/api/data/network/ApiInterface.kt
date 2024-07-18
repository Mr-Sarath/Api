package com.example.api.data.network

import com.example.api.data.modelclass.album.AlbumResponse
import com.example.api.data.modelclass.album.AlbumResponseItem
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
@GET("photos")
suspend fun getPhotos():Response<List<AlbumResponseItem>>
 }