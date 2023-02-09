package com.fatih.popcorn.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fatih.popcorn.R
import com.fatih.popcorn.databinding.FragmentMainRvRowBinding
import com.fatih.popcorn.entities.remote.movie.MovieResult
import com.fatih.popcorn.entities.remote.tvshow.TvShowResult
import javax.inject.Inject

class HomeFragmentAdapter @Inject constructor():RecyclerView.Adapter<HomeFragmentAdapter.HomeFragmentViewHolder>() {

    private val movieDiffUtil=object:DiffUtil.ItemCallback<MovieResult>(){
        override fun areContentsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
            return oldItem==newItem
        }

        override fun areItemsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
            return oldItem.id==newItem.id
        }
    }
    private val movieAsyncListDiffer=AsyncListDiffer(this,movieDiffUtil)

    var movieList:List<MovieResult>
    get() = movieAsyncListDiffer.currentList
    set(value) =movieAsyncListDiffer.submitList(value)

    private val tvShowDiffUtil=object :DiffUtil.ItemCallback<TvShowResult>(){
        override fun areContentsTheSame(oldItem: TvShowResult, newItem: TvShowResult): Boolean {
            return oldItem==newItem
        }

        override fun areItemsTheSame(oldItem: TvShowResult, newItem: TvShowResult): Boolean {
            return oldItem.id==newItem.id
        }
    }
    private val tvShowAsyncListDiffer=AsyncListDiffer(this,tvShowDiffUtil)

    var tvShowList:List<TvShowResult>
        get() = tvShowAsyncListDiffer.currentList
        set(value) = tvShowAsyncListDiffer.submitList(value)


    inner class HomeFragmentViewHolder(val binding:FragmentMainRvRowBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFragmentViewHolder {
        println("yessir")
        return HomeFragmentViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
        R.layout.fragment_main_rv_row,parent,false))
    }

    override fun getItemCount(): Int {
        println("cagrıldı")
        return if(tvShowList.size>=movieList.size) tvShowList.size else movieList.size
    }

    override fun onBindViewHolder(holder: HomeFragmentViewHolder, position: Int) {
        if(movieList.isEmpty()){
            println("empty")
            holder.binding.tvShowResult=tvShowList[position]
        }else{
            println("not empty")
            holder.binding.movieResult=movieList[position]
        }
    }
}