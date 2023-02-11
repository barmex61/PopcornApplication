package com.fatih.popcorn.other

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.fatih.popcorn.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

@BindingAdapter("android:downloadUrl")
fun ImageView.setImageUrl(url:String?){
    this.alpha=0.2f
    try {
        url.let {

            Picasso.get().load("https://www.themoviedb.org/t/p/w600_and_h900_bestv2$url").noFade().placeholder(
                R.drawable.popcorn)
                .into(this,object : Callback {
                    override fun onSuccess() {
                        this@setImageUrl.animate().alpha(1f).setDuration(600).start()
                    }

                    override fun onError(e: java.lang.Exception?) =Unit
                })

        }
    }catch (e:Exception){
        e.printStackTrace()
    }
}

@BindingAdapter("android:setVoteAverage")
fun TextView.setVoteAverageText(voteAverage:Double?){
    voteAverage?.let {
        this.text = it.toString()
    }
}

@BindingAdapter("firstAirDate","releaseDate")
fun TextView.setReleaseDate(firstAirDate:String?,releaseDate:String?){
    val date=firstAirDate?:releaseDate
    date?.let {
        try {
            this.text = it.substring(0,4)
        }catch (e:Exception){
            this.text=it
        }
    }
}

