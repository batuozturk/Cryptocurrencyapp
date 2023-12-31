package com.batuhan.cryptocurrencyapp.presentation.settings.bottomsheet.selectlanguage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.batuhan.cryptocurrencyapp.data.model.LocaleData
import com.batuhan.cryptocurrencyapp.databinding.NewsSourceItemBinding

class SelectLanguageAdapter() :
    ListAdapter<LocaleData, SelectLanguageAdapter.SelectLanguageViewHolder>(object :
            DiffUtil.ItemCallback<LocaleData>() {
            override fun areItemsTheSame(oldItem: LocaleData, newItem: LocaleData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: LocaleData, newItem: LocaleData): Boolean {
                return oldItem.isFavorite == newItem.isFavorite
            }
        }) {

    var eventHandler: SelectLanguageEventHandler? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectLanguageViewHolder {
        val binding =
            NewsSourceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectLanguageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectLanguageViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class SelectLanguageViewHolder(private val binding: NewsSourceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LocaleData) {
            binding.run {
                root.setOnClickListener {
                    eventHandler?.onItemSelected(item)
                }
                text.text = root.context.getString(item.title)
                checkIcon.visibility =
                    if (item.isFavorite == true) View.VISIBLE
                    else View.GONE
            }
        }
    }
}

interface SelectLanguageEventHandler {

    fun onItemSelected(item: LocaleData)
}
