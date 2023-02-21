package com.fatih.popcorn.other

import android.content.res.Resources
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.fatih.popcorn.R
import com.fatih.popcorn.entities.remote.detailresponse.ProductionCompany
import com.fatih.popcorn.entities.remote.detailresponse.ProductionCountry
import com.fatih.popcorn.entities.remote.discoverresponse.DiscoverResponse
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso


@BindingAdapter("android:downloadUrl")
fun ImageView.setImageUrl(url: String?) {
    this.alpha = 0.1f

    val fadeScaleAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation)

    try {
        url.let {
            Picasso.get().load("https://www.themoviedb.org/t/p/w600_and_h900_bestv2$url").placeholder(
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
@BindingAdapter("android:imageUrl")
fun ImageView.setViewPagerImage(url: String?) {

    try {
        url.let {
            Picasso.get().load("https://image.tmdb.org/t/p/original$url").into(this)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun ImageView.setCastImage(url: String?,callBack:(Boolean) -> Unit){

    try {
        url.let {
            Picasso.get().load("https://image.tmdb.org/t/p/original$url").into(this,object:Callback{
                override fun onSuccess() {
                    callBack(true)
                }
                override fun onError(e: java.lang.Exception?) {
                    callBack(false)
                }
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
        } else if (this.genres != data.genres || this.sortBy != data.sortBy) {
            this.results = data.results
            this.total_pages = data.total_pages
            this.total_results = data.total_results
            this.page = data.page
            this.genres = data.genres
            this.sortBy=data.sortBy
        }
        this

    } ?: data
}

@BindingAdapter("episode_runtime","runtime")
fun TextView.setRuntime(episode_runtime: List<Int>?,runtime:Int?){
    this.text=runtime?.let {
        "$it min"
    }?:episode_runtime?.let {
        if (it.isNotEmpty()){
            "${it[0]} min average"
        }else{
            "-"
        } }?:"-"
}

@BindingAdapter("overview")
fun TextView.setOverview(overview:String?){
    overview?.let {
        if (it.isEmpty()){
            "-"
        }else{
            this.text=it
        }
    }?:"-"
}
@BindingAdapter("country")
fun TextView.setCountry(country:List<ProductionCountry>?){
    this.text=country?.let {
        if(it.isNotEmpty()){
            var countryText=""
            for (index in it.indices){
                countryText += if (index == 0){
                    it[index].name
                }else{
                    ",\n${it[index].name}"
                }
            }
            countryText
        }else{
            "-"
        } }?:"-"
}

@BindingAdapter("companies")
fun TextView.setCompanies(company:List<ProductionCompany>?){
    this.text=company?.let {
        if(it.isNotEmpty()){
            var companyText=""
            for (index in it.indices){
                companyText += if (index == 0){
                    it[index].name
                }else{
                    ",\n${it[index].name}"
                }
            }
            companyText
        }else{
            "-"
        } }?:"-"
}

@BindingAdapter("budget")
fun TextView.setBudget(budget:Int?){
    this.text=budget?.let {
        if (it>=1000000){
            "$${it/1000000} million "
        }else if (it>=100000){
            "$${it/1000}k"
        }else{
            "$${it}"
        }
    }?:"-"
}

@BindingAdapter("revenue")
fun TextView.setRevenue(revenue:Long?){
    this.text=revenue?.let {
        if (it>=1000000){
            "$${it/1000000} million "
        }else if (it>=100000){
            "$${it/1000}k"
        }else{
            "$${it}"
        }
    }?:"-"
}

@BindingAdapter("tag")
fun TextView.setTag(tag:String?){
    this.text=(tag?:"-").let {
        if(it.isEmpty()){
            "-"
        }else{
            it
        }
    }
}

@BindingAdapter("releaseDate","lastAirDate")
fun TextView.setRelease(releaseDate: String?,lastAirDate:String?){
    this.text=releaseDate?:lastAirDate?.let {
        "Last episode : $it"
    }
}
