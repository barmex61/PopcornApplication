package com.fatih.popcorn.adapter

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.fatih.popcorn.databinding.WatchListRowBinding
import com.fatih.popcorn.entities.local.RoomEntity
import com.fatih.popcorn.other.Constants.getVibrantColor



class WatchListAdapter (val layout:Int): BaseAdapter<RoomEntity,WatchListRowBinding>(layout) {

    override var diffUtil=object :DiffUtil.ItemCallback<RoomEntity>(){
        override fun areItemsTheSame(oldItem: RoomEntity, newItem: RoomEntity): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: RoomEntity, newItem: RoomEntity): Boolean {
            return oldItem==newItem
        }
    }
    override var asyncListDiffer=AsyncListDiffer(this,diffUtil)

    override fun onBindViewHolder(holder: MyViewHolder<WatchListRowBinding>, position: Int) {
        holder.binding.watchList=list[position]
        holder.binding.watchList=list[position]
        holder.itemView.setOnClickListener {
            val pair= getVibrantColor(holder.binding.recommendImageView)
            myItemClickLambda?.invoke(list[position].posterPath,list[position].field_id.toInt(),pair)
        }
    }

}