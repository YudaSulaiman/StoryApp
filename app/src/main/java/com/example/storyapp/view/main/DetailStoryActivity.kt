package com.example.storyapp.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityDetailStoryBinding
import com.example.storyapp.utils.DateFormatter
import java.util.*

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView(){
        val name = intent.getStringExtra(NAME)
        val desc = intent.getStringExtra(DESCRIPTION)
        val photo = intent.getStringExtra(PHOTO)
        val date = intent.getStringExtra(DATE)

        Glide.with(binding.ivDetailStory)
            .load(photo)
            .into(binding.ivDetailStory)
        binding.tvNameDetail.text = getString(R.string.story_detail_name, name)
        binding.tvDateDetail.text = date?.let { DateFormatter.formatDate(it, TimeZone.getDefault().id) }
        binding.tvDetailDescription.text = desc
    }

    companion object{
        const val NAME = "name"
        const val DESCRIPTION = "description"
        const val PHOTO = "photo"
        const val DATE = "date"
    }
}