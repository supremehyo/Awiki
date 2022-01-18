package com.supremehyo.awiki.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.supremehyo.awiki.databinding.WikiItemLoadingBinding
import com.supremehyo.awiki.databinding.WikiItemRecyclerviewBinding
import com.supremehyo.awiki.repository.wiki.WikiContract

class HomeRecyclerViewAdapter(items: List<WikiContract?>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_LOADING = 1
    }

    private var context: Context? = null
    private var unFilteredList = items
    private var filteredList = items
    override fun getItemCount(): Int {
        return if (filteredList == null){
            0
        }else{
            filteredList?.size!!
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (filteredList?.get(position)) {
            null -> TYPE_LOADING
            else -> TYPE_ITEM
        }
    }

    fun updateItem(list:ArrayList<WikiContract?>?){
        this.filteredList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        if (viewType == TYPE_ITEM){
            val binding = WikiItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ItemViewHolder(binding)
        }else{
            val binding = WikiItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return LoadingViewHolder(binding)
        }


    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder){
            val item = filteredList?.get(position)
            val itemHolder = holder as ItemViewHolder
            itemHolder.bind(item!!)
        }else if (holder is LoadingViewHolder){

        }
    }
    // 아이템뷰에 프로그레스바가 들어가는 경우
    inner class LoadingViewHolder(var binding: WikiItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    inner class ItemViewHolder(var binding: WikiItemRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WikiContract){
            binding.wikiTitle.text = item.title

        }

    }
}