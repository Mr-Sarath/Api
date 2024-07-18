package com.example.api.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.api.databinding.ItemMoviesBinding
import com.example.api.data.modelclass.album.AlbumResponseItem
import com.example.api.utils.logThis

class PostsAdapter() :
    ListAdapter<AlbumResponseItem, PostsAdapter.MovieViewHolder>(DiffCallBack()) {
    inner class MovieViewHolder(val binding: ItemMoviesBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            ItemMoviesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PostsAdapter.MovieViewHolder, position: Int) {
        with(getItem(position)) {
            holder.binding.apply {
                tvMovieTitle.text = title
                logThis("title------------------------->$title")
                val imageUrl = if (url.isNullOrEmpty()) thumbnailUrl else url
                ivMoviePoster.load(imageUrl)
            }
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<AlbumResponseItem>() {
        override fun areItemsTheSame(
            oldItem: AlbumResponseItem,
            newItem: AlbumResponseItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: AlbumResponseItem,
            newItem: AlbumResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }

}