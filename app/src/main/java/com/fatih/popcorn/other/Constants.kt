package com.fatih.popcorn.other

import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.fatih.popcorn.R
import com.fatih.popcorn.ui.PopcornApplication
import java.util.Locale


object Constants {

    const val base_img_url="https://image.tmdb.org/t/p/original"
    const val YOUTUBE_BASE_URL="https://youtube.googleapis.com/youtube/v3/"
    var language=Locale.getDefault().language ?: ""
    const val BASE_URL="https://api.themoviedb.org/3/"
    val sortList= listOf("popularity.desc","release_date.desc","vote_average.desc","first_air_date.desc")
    const val tvSearch="tv"
    const val movieSearch="movie"
    var stateList= mutableListOf(State.MOVIE)
    val sortArray: Array<String> = PopcornApplication.appContext.resources.getStringArray(R.array.sort_array)
    val movie_genre_list: Array<String> =  PopcornApplication.appContext.resources.getStringArray(R.array.movie_genre_list)
    val movie_booleanArray=BooleanArray(movie_genre_list.size)
    val movieGenreMap: HashMap<String,Int> = hashMapOf(movie_genre_list[0] to 28,movie_genre_list[1] to 12,movie_genre_list[2] to 16,movie_genre_list[3] to 35,movie_genre_list[4] to 80,movie_genre_list[5] to 99,movie_genre_list[6] to 18 ,movie_genre_list[7] to 10751,movie_genre_list[8] to 14,movie_genre_list[9] to 36,movie_genre_list[10] to 27,movie_genre_list[11] to 10402 ,movie_genre_list[12] to 9648 ,movie_genre_list[13] to 10749,movie_genre_list[14] to 878,movie_genre_list[15] to 10770,movie_genre_list[16] to 53 ,movie_genre_list[17] to 10752,movie_genre_list[18] to 37 )
    val tv_show_genre_list:Array<String> =  PopcornApplication.appContext.resources.getStringArray(R.array.tv_show_genre_list)
    val tv_show_booleanArray=BooleanArray(tv_show_genre_list.size)
    val tvShowGenreMap:HashMap<String,Int> = hashMapOf(tv_show_genre_list[0] to 10759,tv_show_genre_list[1] to 16,tv_show_genre_list[2] to 35,tv_show_genre_list[3] to 80,tv_show_genre_list[4] to 99,tv_show_genre_list[5] to 18,tv_show_genre_list[6] to 10751,tv_show_genre_list[7] to 10762,tv_show_genre_list[8] to 9648,tv_show_genre_list[9] to 10763,tv_show_genre_list[10] to 10764,tv_show_genre_list[11] to 10765,tv_show_genre_list[12] to 10766,tv_show_genre_list[13] to 10767,tv_show_genre_list[14] to 10768,tv_show_genre_list[15] to 37)
    var orientation= PopcornApplication.appContext.resources.configuration.orientation
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