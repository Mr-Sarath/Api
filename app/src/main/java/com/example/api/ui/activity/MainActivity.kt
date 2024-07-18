package com.example.api.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.api.R
import com.example.api.databinding.ActivityMainBinding
import com.example.api.ui.adapter.PostsAdapter
import com.example.api.ui.viewmodel.AlbumViewModel
import com.example.api.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private val viewModel by viewModels<AlbumViewModel>()
    private val postsAdapter by lazy { PostsAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initViews()
        handleEvents()

    }

    private fun initViews() {
        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val userEmail = sharedPref.getString("userEmail", "")

        binding?.tvEmail?.text = userEmail
        viewModel.getAlbum()
    }

    private fun handleEvents() {
        binding?.recyclerMovie?.apply {
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            layoutManager = GridLayoutManager(this@MainActivity, 2) // Change 2 to your desired number of columns
            adapter = postsAdapter
        }

        binding?.apply {
            ivLogout.setOnClickListener {
                showLogoutDialog()
            }
        }
        albumResponse()
    }

    private fun albumResponse() {
        viewModel.albumResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data?.let { res ->
                            postsAdapter.submitList(res)
                        }

                    }
                    is Resource.Error -> {
                       println("---------------Error-------------------")
                    }
                    is Resource.Loading -> {
                      println("-------------Loading...-----------------")
                    }
                }
            }
        }
    }


    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            logout()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }

    private fun logout() {
        val sharedPref = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
    }


}