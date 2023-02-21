package com.fatih.popcorn.ui.tabfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.fatih.popcorn.R
import com.fatih.popcorn.adapter.ReviewAdapter
import com.fatih.popcorn.databinding.FragmentReviewBinding
import com.fatih.popcorn.other.Constants
import com.fatih.popcorn.other.Constants.checkIsItInMovieListOrNot
import com.fatih.popcorn.other.Constants.movieSearch
import com.fatih.popcorn.other.Constants.tvSearch
import com.fatih.popcorn.other.Status
import com.fatih.popcorn.viewmodel.DetailsFragmentViewModel

class ReviewFragment:Fragment(R.layout.fragment_review) {

    private var _binding:FragmentReviewBinding?=null
    private val binding:FragmentReviewBinding
    get() = _binding!!
    private var view: View?=null
    private var recyclerView:RecyclerView?=null
    private var adapter: ReviewAdapter?=null
    private var viewModel:DetailsFragmentViewModel?=null
    private var selectedId:Int=1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding=DataBindingUtil.inflate(inflater,R.layout.fragment_review,container,false)
        view=binding.root
        viewModel=ViewModelProvider(requireActivity())[DetailsFragmentViewModel::class.java]
        if (checkIsItInMovieListOrNot()){
            viewModel?.getReviews(movieSearch,selectedId,viewModel!!.reviewCurrentPage.value!!)
        }else{
            viewModel?.getReviews(tvSearch,selectedId,viewModel!!.reviewCurrentPage.value!!)
        }
        recyclerView=binding.reciewRecyclerView
        adapter= ReviewAdapter(R.layout.review_recycler_row)
        observeLiveData()
        return view
    }

    private fun observeLiveData(){
        viewModel?.reviewResponse?.observe(viewLifecycleOwner){
            when(it.status){
                Status.SUCCESS->{
                    it.data?.let {
                        adapter?.list=it.results
                        println(adapter?.list?.size)
                    }
                }
                else->Unit
            }
        }
    }

    override fun onDestroyView() {
        adapter=null
        recyclerView=null
        view=null
        viewModel=null
        super.onDestroyView()
    }

}