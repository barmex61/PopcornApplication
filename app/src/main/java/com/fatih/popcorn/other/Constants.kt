package com.fatih.popcorn.other

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.palette.graphics.Palette
import com.fatih.popcorn.R
import com.google.android.material.color.utilities.CorePalette
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.util.Locale


object Constants {

    var language=Locale.getDefault().language ?: ""
    const val BASE_URL="https://api.themoviedb.org/3/"
    val sortList= listOf("popularity.desc","release_date.desc","vote_average.desc","first_air_date.desc")
    const val tvSearch="tv"
    const val movieSearch="movie"
    var stateList= mutableListOf(State.MOVIE)
    val sortArray= arrayOf("Popularity","First Air Date","Vote Average")
    val movie_genre_list= arrayOf("Action","Adventure","Animation","Comedy","Crime","Documentary","Drama","Family","Fantasy","History","Horror","Music","Mystery","Romance","Science Fiction","TV Movie","Thriller","War","Western")
    val movie_booleanArray=BooleanArray(movie_genre_list.size)
    val movieGenreMap: HashMap<String,Int> = hashMapOf("Action" to 28,"Adventure" to 12,"Animation" to 16,"Comedy" to 35,"Crime" to 80,"Documentary" to 99,"Drama" to 18 ,"Family" to 10751,"Fantasy" to 14,"History" to 36,"Horror" to 27,"Music" to 10402 ,"Mystery" to 9648 ,"Romance" to 10749,"Science Fiction" to 878,"TV Movie" to 10770,"Thriller" to 53 ,"War" to 10752,"Western" to 37 )
    val tv_show_genre_list= arrayOf("Action","Animation","Comedy","Crime","Documentary","Drama","Family","Kids","Mystery","News","Reality","Science Fiction","Soap","Talk","War","Western")
    val tv_show_booleanArray=BooleanArray(tv_show_genre_list.size)
    val tvShowGenreMap:HashMap<String,Int> = hashMapOf("Action" to 10759,"Animation" to 16,"Comedy" to 35,"Crime" to 80,"Documentary" to 99,"Drame" to 18,"Family" to 10751,"Kids" to 10762,"Mystery" to 9648,"News" to 10763,"Reality" to 10764,"Science Fiction" to 10765,"Soap" to 10766,"Talk" to 10767,"War" to 10768,"Western" to 37)
    val qualityArray= arrayOf("360p","480p","720p","1080p")
    val qualityBooleanArray=BooleanArray(qualityArray.size)
    var orientation=Resources.getSystem().configuration.orientation
    var isFirstRun=true
    fun checkIsItInMovieListOrNot():Boolean{
        if(stateList.last()==State.MOVIE || ( stateList.last()==State.SEARCH && stateList[stateList.size-2]==State.MOVIE)){
            return true
        }
        return false
    }

    fun getVibrantColor(imageView:ImageView?):Pair<Int,Int>?{

        var pair:Pair<Int,Int>?=null
        imageView?.let {
            if(imageView.drawable!=null){
                val drawable = imageView.drawable as BitmapDrawable
                val bitmap = drawable.bitmap
                Palette.Builder(bitmap).generate().apply {
                    val vibrantColor=this.getVibrantColor(ContextCompat.getColor(imageView.context,R.color.white))
                    val darkMutedColor=this.getDarkMutedColor(ContextCompat.getColor(imageView.context,R.color.white))
                    pair= Pair(vibrantColor,darkMutedColor)
                }
            }
        }
        return pair
    }


}