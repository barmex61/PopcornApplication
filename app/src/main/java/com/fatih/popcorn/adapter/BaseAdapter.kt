package com.fatih.popcorn.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fatih.popcorn.R
import com.fatih.popcorn.databinding.ReviewRecyclerRowBinding

abstract class BaseAdapter <T> (val layoutId:Int):RecyclerView.Adapter<BaseAdapter.MyViewHolder>(){

    abstract var diffUtil:DiffUtil.ItemCallback<T>
    abstract var asyncListDiffer:AsyncListDiffer<T>
    var list:List<T>
    get() = asyncListDiffer.currentList
    set(value) = asyncListDiffer.submitList(value)

    class MyViewHolder(val binding:ReviewRecyclerRowBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val binding=DataBindingUtil.inflate<ReviewRecyclerRowBinding>(LayoutInflater.from(parent.context),R.layout.review_recycler_row,parent,false)
       return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}