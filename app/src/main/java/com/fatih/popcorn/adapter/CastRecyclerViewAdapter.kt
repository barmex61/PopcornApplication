package com.fatih.popcorn.adapter

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.fatih.popcorn.R
import com.fatih.popcorn.databinding.CastRviewRowBinding
import com.fatih.popcorn.other.CastAdapterListener
import com.fatih.popcorn.other.Constants
import com.fatih.popcorn.other.Constants.base_img_url
import com.fatih.popcorn.other.setImageUrl
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import javax.inject.Inject

class CastRecyclerViewAdapter @Inject constructor(
    val layout:Int,
    private val castAdapterListener: CastAdapterListener
):BaseAdapter<Triple<String,String,String>,CastRviewRowBinding>(layout) {

    private var onlyOnce=false


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
        Glide.with(imageView.context).load(base_img_url+url).apply(RequestOptions().placeholder(R.drawable.baseline_account_circle_24).centerCrop().diskCacheStrategy(
            DiskCacheStrategy.AUTOMATIC)).listener(object :
            RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                if (layout.visibility != View.VISIBLE){
                    layout.visibility = View.VISIBLE
                }
                if(!onlyOnce){
                    castAdapterListener.setImages(true)
                    onlyOnce=true
                }
                return false
            }
        }).into(imageView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        onlyOnce=false
        super.onDetachedFromRecyclerView(recyclerView)
    }
}