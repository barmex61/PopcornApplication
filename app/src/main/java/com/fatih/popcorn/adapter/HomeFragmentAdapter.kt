package com.fatih.popcorn.adapter


import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.fatih.popcorn.databinding.FragmentMainRvRowBinding
import com.fatih.popcorn.entities.remote.discoverresponse.DiscoverResult
import com.fatih.popcorn.other.Constants.getVibrantColor

class HomeFragmentAdapter(private val layout:Int) :BaseAdapter<DiscoverResult,FragmentMainRvRowBinding>(layout) {

    override var diffUtil=object:DiffUtil.ItemCallback<DiscoverResult>(){
        override fun areContentsTheSame(oldItem: DiscoverResult, newItem: DiscoverResult): Boolean {
            return oldItem==newItem
        }

        override fun areItemsTheSame(oldItem: DiscoverResult, newItem: DiscoverResult): Boolean {
            return oldItem.id==newItem.id
        }
    }

    override var asyncListDiffer: AsyncListDiffer<DiscoverResult> = AsyncListDiffer(this,diffUtil)

    override fun onBindViewHolder(holder:MyViewHolder<FragmentMainRvRowBinding>, position: Int) {
        val selectedPosition=list[position]
        holder.binding.apply {
            imageUrl=selectedPosition.poster_path
            voteAverage=selectedPosition.vote_average
            releaseDate=selectedPosition.release_date
            firstAirDate=selectedPosition.first_air_date
        }
        holder.itemView.setOnClickListener {
            val pair= getVibrantColor(holder.binding.movieImage)
            if(position<list.size){
                list[position].id?.let { id->
                    myItemClickLambda?.invoke(list[position].poster_path,id,pair,null)
                }
            }
        }
    }

}