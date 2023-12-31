package com.batuhan.cryptocurrencyapp.presentation.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.batuhan.cryptocurrencyapp.data.model.SettingsItem
import com.batuhan.cryptocurrencyapp.databinding.SettingsItemBinding

class SettingsAdapter : ListAdapter<SettingsItem, SettingsAdapter.SettingsItemViewHolder>(object :
        DiffUtil.ItemCallback<SettingsItem>() {
        override fun areItemsTheSame(oldItem: SettingsItem, newItem: SettingsItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SettingsItem, newItem: SettingsItem): Boolean {
            return oldItem.routing == newItem.routing
        }
    }) {

    var eventHandler: SettingsEventHandler? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsItemViewHolder {
        val binding =
            SettingsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SettingsItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SettingsItemViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class SettingsItemViewHolder(private val binding: SettingsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SettingsItem) {
            binding.run {
                root.setOnClickListener {
                    eventHandler?.onItemClick(item)
                }
                icon.setImageResource(item.icon)
                title.text = root.context.getString(item.title)
            }
        }
    }
}

interface SettingsEventHandler {
    fun onItemClick(item: SettingsItem)
}
