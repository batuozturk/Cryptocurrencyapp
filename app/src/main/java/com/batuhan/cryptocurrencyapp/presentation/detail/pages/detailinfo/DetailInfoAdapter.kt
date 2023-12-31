package com.batuhan.cryptocurrencyapp.presentation.detail.pages.detailinfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.batuhan.cryptocurrencyapp.data.model.DetailInfoData
import com.batuhan.cryptocurrencyapp.databinding.DetailInfoItemBinding

class DetailInfoAdapter :
    ListAdapter<DetailInfoData, DetailInfoAdapter.DetailInfoViewHolder>(object :
            DiffUtil.ItemCallback<DetailInfoData>() {
            override fun areItemsTheSame(oldItem: DetailInfoData, newItem: DetailInfoData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DetailInfoData, newItem: DetailInfoData): Boolean {
                return oldItem.value == newItem.value
            }
        }) {

    inner class DetailInfoViewHolder(private val binding: DetailInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DetailInfoData) {
            binding.run {
                title.text = root.context.getString(item.title)
                value.text = item.value
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailInfoViewHolder {
        val binding =
            DetailInfoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailInfoViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}
