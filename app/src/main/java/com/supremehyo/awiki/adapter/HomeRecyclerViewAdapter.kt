package com.supremehyo.awiki.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.os.bundleOf
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.supremehyo.awiki.DTO.HometoEditDTO
import com.supremehyo.awiki.R
import com.supremehyo.awiki.databinding.WikiItemLoadingBinding
import com.supremehyo.awiki.databinding.WikiItemRecyclerviewBinding
import com.supremehyo.awiki.repository.wiki.WikiContract
import com.supremehyo.awiki.utils.EventBus
import com.supremehyo.awiki.viewmodel.EditFragmentViewModel
import kotlinx.android.synthetic.main.wiki_item_recyclerview.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeRecyclerViewAdapter(activity : Activity ,items: List<WikiContract?>? , viewmodel : EditFragmentViewModel) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_LOADING = 1
    }

    private var  model : EditFragmentViewModel = viewmodel
    private var context: Context? = null
    private var unFilteredList = items
    private var filteredList = items
    private var aactivity = activity
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
            holder.itemView.wiki_item_cl.setOnClickListener {
                var temp = HometoEditDTO(item, "read")
                model.clickHomeWikiListItem(temp)
                findNavController(holder.itemView).navigate(R.id.action_homeFragment_to_writeFragment)
            }
        }else if (holder is LoadingViewHolder){

        }
    }
    // 아이템뷰에 프로그레스바가 들어가는 경우
    inner class LoadingViewHolder(var binding: WikiItemLoadingBinding) : RecyclerView.ViewHolder(binding.root) {}

    inner class ItemViewHolder(var binding: WikiItemRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WikiContract){
            binding.wikiTitle.text = item.title
            binding.rawContent.text = item.rawContent
        }
    }
}