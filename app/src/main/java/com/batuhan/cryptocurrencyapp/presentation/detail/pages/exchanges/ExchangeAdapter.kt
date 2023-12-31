package com.batuhan.cryptocurrencyapp.presentation.detail.pages.exchanges

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.batuhan.cryptocurrencyapp.data.model.MarketItem
import com.batuhan.cryptocurrencyapp.databinding.DetailInfoItemBinding

class ExchangeAdapter : ListAdapter<MarketItem, ExchangeAdapter.ExchangeViewHolder>(object :
        DiffUtil.ItemCallback<MarketItem>() {
        override fun areItemsTheSame(oldItem: MarketItem, newItem: MarketItem): Boolean {
            return newItem == oldItem
        }

        override fun areContentsTheSame(oldItem: MarketItem, newItem: MarketItem): Boolean {
            return newItem.priceUsd == oldItem.priceUsd
        }
    }) {

    inner class ExchangeViewHolder(private val binding: DetailInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MarketItem) {
            binding.run {
                title.text = item.exchangeId ?: ""
                value.text = item.priceUsd ?: ""
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeViewHolder {
        val binding = DetailInfoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExchangeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExchangeViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}
