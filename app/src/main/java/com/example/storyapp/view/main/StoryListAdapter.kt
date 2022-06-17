package com.example.storyapp.view.main

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.databinding.StoryListBinding
import com.example.storyapp.utils.DateFormatter
import java.util.*

class StoryListAdapter : PagingDataAdapter<ListStoryItem, StoryListAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): StoryViewHolder {
        Log.i("RESULT ADAPTER", getItem(1).toString())
        val binding = StoryListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val data = getItem(position)
        Log.i("RESULT ADAPTER", data.toString())
        if (data != null) {
            holder.bind(data)
        }
    }

    class StoryViewHolder(var binding: StoryListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListStoryItem) {
            Glide.with(binding.ivPhoto.context)
                .load(data.photoUrl)
                .into(binding.ivPhoto)
            binding.tvName.text = data.name
            binding.tvDate.text = DateFormatter.formatDate(data.createdAt, TimeZone.getDefault().id)
            itemView.setOnClickListener{
                val toDetail = Intent(itemView.context, DetailStoryActivity::class.java)
                toDetail.putExtra(DetailStoryActivity.NAME, data.name)
                toDetail.putExtra(DetailStoryActivity.DESCRIPTION, data.description)
                toDetail.putExtra(DetailStoryActivity.PHOTO, data.photoUrl)
                toDetail.putExtra(DetailStoryActivity.DATE, data.createdAt)
                itemView.context.startActivity(toDetail)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}