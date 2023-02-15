package com.fatih.popcorn.other

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.fatih.popcorn.R
import com.fatih.popcorn.entities.remote.DiscoverResponse
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@BindingAdapter("android:downloadUrl")
fun ImageView.setImageUrl(url: String?) {
    this.alpha = 0.1f

    val fadeScaleAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation)

    try {
        url.let {
            Picasso.get().load("https://www.themoviedb.org/t/p/w600_and_h900_bestv2$url").noFade()
                .placeholder(
                    R.drawable.popcorn
                )
                .into(this@setImageUrl, object : Callback {
                    override fun onSuccess() {
                        this@setImageUrl.apply {
                            alpha = 1f
                            startAnimation(fadeScaleAnimation)
                        }
                    }

                    override fun onError(e: java.lang.Exception?) = Unit
                })

        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

@BindingAdapter("android:setVoteAverage")
fun TextView.setVoteAverageText(voteAverage: Double?) {
    voteAverage?.let {
        this.text = it.toString()
    }
}

@BindingAdapter("firstAirDate", "releaseDate")
fun TextView.setReleaseDate(firstAirDate: String?, releaseDate: String?) {
    val date = firstAirDate ?: releaseDate
    date?.let {
        try {
            this.text = it.substring(0, 4)
        } catch (e: Exception) {
            this.text = it
        }
    }
}

fun <State> MutableList<State>.addFilter(data: State) {

    if (data == com.fatih.popcorn.other.State.SEARCH && this.last() == com.fatih.popcorn.other.State.SEARCH) {
        return
    }
    this.add(data)

}

fun DiscoverResponse?.add(data: DiscoverResponse): DiscoverResponse {
    return this?.let {
        if (this.page != data.page) {
            this.results += data.results
            this.total_pages = data.total_pages
            this.total_results = data.total_results
            this.page = data.page
            println("if")
        } else if (this.page == data.page && this.genres != data.genres) {
            println("else if")
            this.results = data.results
            this.total_pages = data.total_pages
            this.total_results = data.total_results
            this.page = data.page
            this.genres = data.genres
        }
        this

    } ?: data
}