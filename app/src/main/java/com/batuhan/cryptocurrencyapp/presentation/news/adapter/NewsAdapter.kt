package com.batuhan.cryptocurrencyapp.presentation.news.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.batuhan.cryptocurrencyapp.R
import com.batuhan.cryptocurrencyapp.data.model.RssItemData
import com.batuhan.cryptocurrencyapp.databinding.NewsItemViewBinding
import com.bumptech.glide.Glide
import org.jsoup.Jsoup

class NewsAdapter : ListAdapter<RssItemData, NewsAdapter.NewsViewHolder>(object :
        DiffUtil.ItemCallback<RssItemData>() {
        override fun areItemsTheSame(oldItem: RssItemData, newItem: RssItemData): Boolean {
            return oldItem.rssItem.guid == newItem.rssItem.guid
        }

        override fun areContentsTheSame(oldItem: RssItemData, newItem: RssItemData): Boolean {
            return oldItem.rssItem.content == newItem.rssItem.content &&
                oldItem.rssItem.guid == newItem.rssItem.guid &&
                oldItem.isExpanded == newItem.isExpanded
        }
    }) {

    var eventHandler: NewsEventHandler? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding =
            NewsItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class NewsViewHolder(private val binding: NewsItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RssItemData) {
            binding.run {
                root.setOnClickListener {
                    eventHandler?.onNewsClicked(item.rssItem.link ?: return@setOnClickListener)
                }
                title.text = item.rssItem.title
                date.text = item.rssItem.pubDate
                val expandDrawable =
                    if (item.isExpanded) R.drawable.ic_expand_less_24 else R.drawable.ic_expand_more_24
                expandItem.setImageResource(expandDrawable)
                expandItem.setOnClickListener {
                    eventHandler?.onNewsExpanded(item.rssItem.guid ?: return@setOnClickListener)
                }
                val visibility = if (item.isExpanded) View.VISIBLE else View.GONE
                Glide.with(root.context).load(item.rssItem.image).into(newsImage)
                content.text = Jsoup.parse(item.rssItem.description).text()
                newsDetail.visibility = visibility
            }
        }
    }
}

interface NewsEventHandler {

    fun onNewsExpanded(guid: String)

    fun onNewsClicked(url: String)
}
