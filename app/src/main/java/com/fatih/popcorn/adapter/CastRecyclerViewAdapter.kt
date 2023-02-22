package com.fatih.popcorn.adapter

import android.view.View
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.fatih.popcorn.databinding.CastRviewRowBinding
import com.fatih.popcorn.other.setCastImage
import javax.inject.Inject

class CastRecyclerViewAdapter @Inject constructor(
    val layout:Int,
    val hideProgressBar:(Boolean)->Unit
):BaseAdapter<Triple<String,String,String>,CastRviewRowBinding>(layout) {

    override var vibrantColor: Int = 0

    override var diffUtil=object :DiffUtil.ItemCallback<Triple<String,String,String>>(){
        override fun areContentsTheSame(oldItem: Triple<String, String, String>, newItem: Triple<String, String, String>): Boolean {
            return ((oldItem.first==newItem.first) && (oldItem.second==newItem.second) && (oldItem.third==newItem.third))
        }

        override fun areItemsTheSame(oldItem: Triple<String, String, String>, newItem: Triple<String, String, String>): Boolean {
            return oldItem.third==newItem.third
        }
    }

    override var asyncListDiffer=AsyncListDiffer(this,diffUtil)

    override fun onBindViewHolder(holder: MyViewHolder<CastRviewRowBinding>, position: Int) {
        holder.binding.nameText.setTextColor(vibrantColor)
        holder.binding.characterText.setTextColor(vibrantColor)
        holder.binding.name=list[position].first
        holder.binding.charecter="(${list[position].second})"
        holder.binding.circleImageView.setCastImage(list[position].third){

            if (it && holder.binding.castRowLayout.visibility==View.GONE){
                holder.binding.castRowLayout.visibility=View.VISIBLE
                hideProgressBar(true)
            }else if (!it){
                holder.binding.castRowLayout.visibility=View.GONE
            }
        }
    }
}