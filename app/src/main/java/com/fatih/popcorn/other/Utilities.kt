package com.fatih.popcorn.other

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.fatih.popcorn.R
import com.fatih.popcorn.entities.remote.detailresponse.ProductionCompany
import com.fatih.popcorn.entities.remote.detailresponse.ProductionCountry
import com.fatih.popcorn.entities.remote.discoverresponse.DiscoverResponse
import com.fatih.popcorn.entities.remote.youtuberesponse.YoutubeResponse
import com.fatih.popcorn.other.Constants.base_img_url
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("url","fitXY","isYoutube")
fun ImageView.setImageUrl(url:String?,fitXY: Boolean,isYoutube:Boolean){

    this.alpha=0.2f
    val fadeScaleAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation)
    this.scaleType=if (fitXY){ ScaleType.FIT_XY }else{ ScaleType.CENTER_CROP }
    val requestOptions= RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).placeholder(R.drawable.popcorn)
            .centerCrop()

    val baseUrl= if (isYoutube) "" else base_img_url
    try {
        Glide.with(this.context).applyDefaultRequestOptions(requestOptions).load(baseUrl+url).listener(object :RequestListener<Drawable>{
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                this.apply {
                    alpha=1f
                    startAnimation(fadeScaleAnimation)
                }
                return false
            }
        }).into(this)
       /* Picasso.get().load(baseUrl+url).apply(requestOptions).listener(object :RequestListener<Drawable>{
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                this@setImageUrl.apply {
                    alpha=1f
                    startAnimation(fadeScaleAnimation)
                }
                return false
            }
        }).into(this)   */
    }catch (e:Exception){
        e.printStackTrace()
    }
}


@BindingAdapter("color")
fun ImageView.setTint(isFavorite:Boolean){
    val color=if (isFavorite) ColorStateList.valueOf(resources.getColor(R.color.scaletRed)) else ColorStateList.valueOf(resources.getColor(R.color.white))
    this.imageTintList= color
}

@BindingAdapter("android:setVoteAverage")
fun TextView.setVoteAverageText(voteAverage: Double?) {
    voteAverage?.let {
        this.text=if (it.toString().length>2){
            it.toString().substring(0..2)
        }else{
            it.toString()
        }
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

fun DiscoverResponse?.recommend(data:DiscoverResponse):DiscoverResponse{
    return this?.let {
        if (it.recommendationId==data.recommendationId && it.page != data.page ){
            it.results += data.results
            it.total_pages = data.total_pages
            it.total_results = data.total_results
            it.page = data.page
        }else if (it.recommendationId != data.recommendationId){
            it.results = data.results
            it.total_pages = data.total_pages
            it.total_results = data.total_results
            it.page = data.page
            it.genres = data.genres
            it.sortBy=data.sortBy
        }
        it
    }?:data
}

fun YoutubeResponse.updateList(position:Int):YoutubeResponse{

    return this.apply {
            this.items=this.items.toMutableList().apply {
                removeAt(position)
            }
        }
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
fun TextView.setTagLine(tag:String?){
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

@SuppressLint("SetTextI18n")
@BindingAdapter("date")
fun TextView.setDate(date:String?){
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val currentDate: String = sdf.format(Date())
    date?.let {
        try {
            val year=currentDate.substring(0,4).toInt()-it.substring(0,4).toInt()
            val month=currentDate.substring(5,7).toInt()-it.substring(5,7).toInt()
            val day=currentDate.substring(8,10).toInt()-it.substring(8,10).toInt()
            if (year>=1){
                this.text="$year " + resources.getString(R.string.yearsago)
            }else if(month>=1){
                this.text="$month "+ resources.getString(R.string.monthago)
            }else if(day>=1){
                this.text="$day " + resources.getString(R.string.dayago)
            }else{
                this.text=resources.getString(R.string.today)
            }
        }catch (e:Exception){

        }
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("review")
fun TextView.setReview(review:Int?){
    this.text=review?.let {
        "$review "+ resources.getString(R.string.review)
    }?:resources.getString(R.string.rviewcount)
}

