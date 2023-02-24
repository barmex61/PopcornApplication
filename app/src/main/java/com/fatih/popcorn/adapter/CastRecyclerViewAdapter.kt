package com.fatih.popcorn.adapter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fatih.popcorn.R
import com.fatih.popcorn.databinding.CastRviewRowBinding
import com.fatih.popcorn.other.Constants
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import javax.inject.Inject

class CastRecyclerViewAdapter @Inject constructor(
    val layout:Int
):BaseAdapter<Triple<String,String,String>,CastRviewRowBinding>(layout) {

    private var hideProgressBar:((Boolean)->Unit)? =null
    private var onlyOnce=false

    fun setHideProgressBar(lambda:(Boolean)->Unit){
        this.hideProgressBar=lambda
    }

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
        val imageView=holder.binding.circleImageView
        list[position].third.let {
            val castView=holder.binding.castRowLayout
            if (castView.visibility!=View.VISIBLE){
                setImages(it,imageView,castView)
            }
        }
    }

    private fun setImages(url:String,imageView:ImageView,layout: View){
        Constants.picasso.load("https://image.tmdb.org/t/p/original$url").fit().centerCrop().placeholder(
            R.drawable.baseline_account_circle_24).into(imageView,object: Callback {
            override fun onSuccess() {
                if (layout.visibility != View.VISIBLE){
                    layout.visibility = View.VISIBLE
                }
                if(!onlyOnce){
                    hideProgressBar?.invoke(true)
                    onlyOnce=true
                }
            }
            override fun onError(e: java.lang.Exception?) =Unit
        })
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        onlyOnce=false
        hideProgressBar=null
        super.onDetachedFromRecyclerView(recyclerView)
    }
}