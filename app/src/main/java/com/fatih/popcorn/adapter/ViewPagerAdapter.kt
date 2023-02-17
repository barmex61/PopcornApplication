package com.fatih.popcorn.adapter

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.fatih.popcorn.other.setViewPagerImage

class ViewPagerAdapter (private val context: Context,
                        private val portraitList:List<String>,
                        private val landscapeList:List<String>):PagerAdapter() {
    override fun getCount(): Int {
        return if(isItPortrait()){
            portraitList.size
        }else landscapeList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view==`object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView=ImageView(context).apply {
            this.scaleType=ImageView.ScaleType.CENTER_CROP
            if (isItPortrait()){
                setViewPagerImage(portraitList[position])
            }else{
                setViewPagerImage(landscapeList[position])
            }
            container.addView(this)
        }
        return  imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    private fun isItPortrait():Boolean{
        return Resources.getSystem().displayMetrics.widthPixels<Resources.getSystem().displayMetrics.heightPixels
    }
}