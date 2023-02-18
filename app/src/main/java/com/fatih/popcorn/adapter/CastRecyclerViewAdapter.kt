package com.fatih.popcorn.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fatih.popcorn.R
import com.fatih.popcorn.databinding.CastRviewRowBinding
import javax.inject.Inject

class CastRecyclerViewAdapter @Inject constructor():RecyclerView.Adapter<CastRecyclerViewAdapter.CastRecyclerViewHolder>() {

    private val diffUtil=object:DiffUtil.ItemCallback<String>(){
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }
    }

    private var asyncListDiffer=AsyncListDiffer(this,diffUtil)

    var urlList:List<String>
    get() = asyncListDiffer.currentList
    set(value) = asyncListDiffer.submitList(value)

    class CastRecyclerViewHolder(val castRecyclerRowBinding: CastRviewRowBinding):RecyclerView.ViewHolder(castRecyclerRowBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastRecyclerViewHolder {
        return CastRecyclerViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.cast_rview_row,parent,false))
    }

    override fun getItemCount(): Int {
        return urlList.size
    }

    override fun onBindViewHolder(holder: CastRecyclerViewHolder, position: Int) {
        holder.castRecyclerRowBinding.imageUrl=urlList[position]
        println("position ${position}")
    }
}