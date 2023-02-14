package com.fatih.popcorn.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fatih.popcorn.R
import com.fatih.popcorn.databinding.FragmentMainRvRowBinding
import com.fatih.popcorn.entities.remote.DiscoverResult
import com.fatih.popcorn.other.Constants.getVibrantColor
import com.fatih.popcorn.ui.HomeFragmentDirections
import javax.inject.Inject

class HomeFragmentAdapter @Inject constructor():RecyclerView.Adapter<HomeFragmentAdapter.HomeFragmentViewHolder>() {

    private var myItemClickLambda:((Int,Pair<Int,Int>?)->Unit)?=null

    fun setMyOnClickLambda(lambda:(Int,Pair<Int,Int>?) ->Unit){
        this.myItemClickLambda=lambda
    }


    private val discoverDataUtil=object:DiffUtil.ItemCallback<DiscoverResult>(){
        override fun areContentsTheSame(oldItem: DiscoverResult, newItem: DiscoverResult): Boolean {
            return oldItem==newItem
        }

        override fun areItemsTheSame(oldItem: DiscoverResult, newItem: DiscoverResult): Boolean {
            return oldItem.id==newItem.id
        }
    }
    private val searchAsyncListDiffer=AsyncListDiffer(this,discoverDataUtil)

    var discoverList:List<DiscoverResult>
        get() = searchAsyncListDiffer.currentList
        set(value) =searchAsyncListDiffer.submitList(value)
    inner class HomeFragmentViewHolder(val binding:FragmentMainRvRowBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFragmentViewHolder {
        return HomeFragmentViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
        R.layout.fragment_main_rv_row,parent,false))
    }

    override fun getItemCount(): Int {
        println(discoverList.size)
        return discoverList.size
    }

    override fun onBindViewHolder(holder: HomeFragmentViewHolder, position: Int) {
        val discoverResult=discoverList[position]
        holder.binding.discoverResult=discoverResult
        println("hellooo")
        holder.itemView.setOnClickListener {
            println(discoverResult.id)
            val colorPair=getVibrantColor(holder.binding.movieImage)
            discoverResult.id?.let {
                println(it)
                myItemClickLambda?.invoke(it,colorPair)
            }
        }
    }
}