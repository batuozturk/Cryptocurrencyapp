package com.batuhan.cryptocurrencyapp.presentation.settings.bottomsheet.selectcurrency.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.batuhan.cryptocurrencyapp.data.model.RateItem
import com.batuhan.cryptocurrencyapp.databinding.NewsSourceItemBinding

class SelectCurrencyAdapter() :
    ListAdapter<RateItem, SelectCurrencyAdapter.SelectCurrencyViewHolder>(object :
            DiffUtil.ItemCallback<RateItem>() {
            override fun areItemsTheSame(oldItem: RateItem, newItem: RateItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: RateItem, newItem: RateItem): Boolean {
                return oldItem.isFavorite == newItem.isFavorite
            }
        }) {

    var eventHandler: SelectCurrencyEventHandler? = null
    var converterIndex: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectCurrencyViewHolder {
        val binding =
            NewsSourceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectCurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectCurrencyViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class SelectCurrencyViewHolder(private val binding: NewsSourceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RateItem) {
            binding.run {
                root.setOnClickListener {
                    eventHandler?.onItemSelected(item)
                }
                text.text = item.symbol
                checkIcon.visibility =
                    if (item.isFavorite == true && converterIndex == -1) View.VISIBLE
                    else if (item.converterIndex == converterIndex && item.converterIndex != -1) View.VISIBLE
                    else View.GONE
            }
        }
    }
}

interface SelectCurrencyEventHandler {

    fun onItemSelected(item: RateItem)
}
