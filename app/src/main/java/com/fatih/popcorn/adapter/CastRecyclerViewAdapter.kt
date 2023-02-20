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

    private val diffUtil=object :DiffUtil.ItemCallback<Triple<String,String,String>>(){
        override fun areContentsTheSame(oldItem: Triple<String, String, String>, newItem: Triple<String, String, String>): Boolean {
            return ((oldItem.first==newItem.first) && (oldItem.second==newItem.second) && (oldItem.third==newItem.third))
        }

        override fun areItemsTheSame(oldItem: Triple<String, String, String>, newItem: Triple<String, String, String>): Boolean {
            return oldItem.third==newItem.third
        }
    }

    private var asyncListDiffer=AsyncListDiffer(this,diffUtil)

    var castList:List<Triple<String,String,String>>
    get() = asyncListDiffer.currentList
    set(value) = asyncListDiffer.submitList(value)

    class CastRecyclerViewHolder(val castRecyclerRowBinding: CastRviewRowBinding):RecyclerView.ViewHolder(castRecyclerRowBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastRecyclerViewHolder {
        return CastRecyclerViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.cast_rview_row,parent,false))
    }

    override fun getItemCount(): Int {
        return castList.size
    }

    override fun onBindViewHolder(holder: CastRecyclerViewHolder, position: Int) {
        holder.castRecyclerRowBinding.imageUrl=castList[position].third
        holder.castRecyclerRowBinding.name=castList[position].first
        holder.castRecyclerRowBinding.charecter="(${castList[position].second})"
    }
}