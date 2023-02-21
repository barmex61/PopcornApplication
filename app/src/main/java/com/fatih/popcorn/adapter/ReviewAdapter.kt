package com.fatih.popcorn.adapter

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.fatih.popcorn.entities.remote.reviewresponse.ReviewResponse
import com.fatih.popcorn.entities.remote.reviewresponse.ReviewResult


class ReviewAdapter(
    val layout:Int,
) : BaseAdapter<ReviewResult>(layout) {


    override var diffUtil: DiffUtil.ItemCallback<ReviewResult> = object : DiffUtil.ItemCallback<ReviewResult>(){
        override fun areContentsTheSame(oldItem: ReviewResult, newItem: ReviewResult): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areItemsTheSame(oldItem: ReviewResult, newItem: ReviewResult): Boolean {
            return oldItem==newItem
        }
    }

    override var asyncListDiffer: AsyncListDiffer<ReviewResult> = AsyncListDiffer(this,diffUtil)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.dateText.text="asdasdasd"
        holder.binding.profileNameText.text="Profile Name"
        holder.binding.ratingText.text="1/10"
    }


}