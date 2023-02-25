package com.fatih.popcorn.adapter


import android.content.res.ColorStateList
import android.view.View
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.fatih.popcorn.databinding.SeasonLayoutRowBinding
import com.fatih.popcorn.entities.remote.detailresponse.Season
import com.fatih.popcorn.ui.DetailsFragment

class SeasonAdapter (val layout:Int): BaseAdapter<Season,SeasonLayoutRowBinding>(layout) {



    override var diffUtil=object:DiffUtil.ItemCallback<Season>(){
        override fun areContentsTheSame(oldItem: Season, newItem: Season): Boolean {
            return oldItem==newItem
        }

        override fun areItemsTheSame(oldItem: Season, newItem: Season): Boolean {
            return oldItem==newItem
        }
    }
    override var asyncListDiffer=AsyncListDiffer(this,diffUtil)

    override fun onBindViewHolder(holder: MyViewHolder<SeasonLayoutRowBinding>, position: Int) {
        holder.binding.tvShowSeason=list[position]
        holder.binding.rating=DetailsFragment.seasonRating
        holder.binding.genre=DetailsFragment.seasonGenres
        holder.binding.ratingBar.rating =DetailsFragment.seasonRatingFloat?:0f
        holder.binding.circleBg.backgroundTintList= ColorStateList.valueOf(DetailsFragment.vibrantColor!!)
        holder.binding.circleBg2.backgroundTintList= ColorStateList.valueOf(DetailsFragment.vibrantColor!!)
        holder.binding.ratingBar.progressTintList= ColorStateList.valueOf(DetailsFragment.vibrantColor!!)

    }
    

}