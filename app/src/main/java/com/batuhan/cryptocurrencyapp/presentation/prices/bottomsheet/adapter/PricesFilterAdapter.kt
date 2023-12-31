package com.batuhan.cryptocurrencyapp.presentation.prices.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.batuhan.cryptocurrencyapp.data.model.FilterTypeData
import com.batuhan.cryptocurrencyapp.databinding.NewsSourceItemBinding

class PricesFilterAdapter :
    ListAdapter<FilterTypeData, PricesFilterAdapter.PricesFilterViewHolder>(object :
            DiffUtil.ItemCallback<FilterTypeData>() {
            override fun areItemsTheSame(oldItem: FilterTypeData, newItem: FilterTypeData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FilterTypeData, newItem: FilterTypeData): Boolean {
                return oldItem.isSelected == newItem.isSelected
            }
        }) {

    var eventHandler: PricesFilterEventHandler? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PricesFilterViewHolder {
        val binding =
            NewsSourceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PricesFilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PricesFilterViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class PricesFilterViewHolder(private val binding: NewsSourceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FilterTypeData) {
            binding.run {
                root.setOnClickListener {
                    eventHandler?.onFilterSelected(item.filterType.name)
                }
                text.text = root.context.getString(item.filterType.title)
                checkIcon.visibility = if (item.isSelected) View.VISIBLE else View.GONE
            }
        }
    }
}

interface PricesFilterEventHandler {
    fun onFilterSelected(name: String)
}
