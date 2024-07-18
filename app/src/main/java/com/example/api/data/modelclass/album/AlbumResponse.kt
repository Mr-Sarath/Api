package com.example.api.data.modelclass.album

import com.google.gson.annotations.SerializedName

data class AlbumResponse(

	@field:SerializedName("AlbumResponse")
	val albumResponse: List<AlbumResponseItem>? = null
)

data class AlbumResponseItem(

	@field:SerializedName("albumId")
	val albumId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("thumbnailUrl")
	val thumbnailUrl: String? = null
)
