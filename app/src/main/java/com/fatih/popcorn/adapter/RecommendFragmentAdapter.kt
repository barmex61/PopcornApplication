package com.fatih.popcorn.adapter

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.fatih.popcorn.databinding.FragmentRecommendRowBinding
import com.fatih.popcorn.entities.remote.discoverresponse.DiscoverResult
import com.fatih.popcorn.other.Constants

class RecommendFragmentAdapter(val layout:Int) :BaseAdapter<DiscoverResult,FragmentRecommendRowBinding>(layout){


    override var diffUtil: DiffUtil.ItemCallback<DiscoverResult> = object :DiffUtil.ItemCallback<DiscoverResult>(){
        override fun areContentsTheSame(oldItem: DiscoverResult, newItem: DiscoverResult): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areItemsTheSame(oldItem: DiscoverResult, newItem: DiscoverResult): Boolean {
            return oldItem.id==newItem.id
        }
    }

    override var asyncListDiffer: AsyncListDiffer<DiscoverResult> = AsyncListDiffer(this,diffUtil)

    override fun onBindViewHolder(holder: MyViewHolder<FragmentRecommendRowBinding>, position: Int) {
        holder.binding.imageUrl=list[position].poster_path
        holder.binding.voteAverage=list[position].vote_average
        holder.binding.name=list[position].name?:list[position].title
        holder.binding.releaseDate=list[position].release_date
        holder.binding.firstAirDate=list[position].first_air_date
        holder.binding.recommendNameText.isSelected=true
        holder.itemView.setOnClickListener {
            val pair= Constants.getVibrantColor(holder.binding.recommendImageView)
            if(position<list.size){
                list[position].id?.let { id->
                    myItemClickLambda?.invoke(list[position].poster_path,id,pair,null)
                }
            }
        }
    }

}