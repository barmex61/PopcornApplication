package com.fatih.popcorn.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fatih.popcorn.R
import com.fatih.popcorn.databinding.FragmentMainRvRowBinding
import com.fatih.popcorn.entities.remote.DiscoverResult
import javax.inject.Inject

class HomeFragmentAdapter @Inject constructor():RecyclerView.Adapter<HomeFragmentAdapter.HomeFragmentViewHolder>() {

    private val discoverDataUtil=object:DiffUtil.ItemCallback<DiscoverResult>(){
        override fun areContentsTheSame(oldItem: DiscoverResult, newItem: DiscoverResult): Boolean {
            return oldItem==newItem
        }

        override fun areItemsTheSame(oldItem: DiscoverResult, newItem: DiscoverResult): Boolean {
            return oldItem.id==newItem.id
        }
    }
    private val searchAsyncListDiffer=AsyncListDiffer(this,discoverDataUtil)

    var discoverList:List<DiscoverResult>
        get() = searchAsyncListDiffer.currentList
        set(value) =searchAsyncListDiffer.submitList(value)
    inner class HomeFragmentViewHolder(val binding:FragmentMainRvRowBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFragmentViewHolder {
        return HomeFragmentViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
        R.layout.fragment_main_rv_row,parent,false))
    }

    override fun getItemCount(): Int {
        return discoverList.size
    }

    override fun onBindViewHolder(holder: HomeFragmentViewHolder, position: Int) {
        holder.binding.discoverResult=discoverList[position]
        holder.binding.animatedLayout.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context,R.anim.from_down))
    }
}