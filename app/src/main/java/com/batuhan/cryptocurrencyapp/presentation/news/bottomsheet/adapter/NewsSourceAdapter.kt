package com.batuhan.cryptocurrencyapp.presentation.news.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.batuhan.cryptocurrencyapp.data.model.NewsSourceData
import com.batuhan.cryptocurrencyapp.databinding.NewsSourceItemBinding

class NewsSourceAdapter : ListAdapter<NewsSourceData, NewsSourceAdapter.NewsSourceViewHolder>(object :
        DiffUtil.ItemCallback<NewsSourceData>() {
        override fun areItemsTheSame(oldItem: NewsSourceData, newItem: NewsSourceData): Boolean {
            return oldItem.newsSource.name == newItem.newsSource.name
        }

        override fun areContentsTheSame(oldItem: NewsSourceData, newItem: NewsSourceData): Boolean {
            return oldItem.newsSource.name == newItem.newsSource.name
        }
    }) {

    var eventHandler: NewsSourceEventHandler? = null

    inner class NewsSourceViewHolder(private val binding: NewsSourceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NewsSourceData) {
            binding.run {
                root.setOnClickListener {
                    eventHandler?.onSourceSelected(item.newsSource.name)
                }
                text.text = root.context.getString(item.newsSource.title)
                val visibility = if(item.isSelected) View.VISIBLE else View.GONE
                checkIcon.visibility = visibility
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsSourceViewHolder {
        val binding =
            NewsSourceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsSourceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsSourceViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}

interface NewsSourceEventHandler {
    fun onSourceSelected(source: String)
}
