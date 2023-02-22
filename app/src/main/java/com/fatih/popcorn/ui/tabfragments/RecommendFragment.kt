package com.fatih.popcorn.ui.tabfragments

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.fatih.popcorn.R
import com.fatih.popcorn.adapter.CastRecyclerViewAdapter
import com.fatih.popcorn.adapter.RecommendFragmentAdapter
import com.fatih.popcorn.databinding.FragmentCastBinding
import com.fatih.popcorn.databinding.FragmentRecommendBinding
import com.fatih.popcorn.other.Constants
import com.fatih.popcorn.other.Constants.movieSearch
import com.fatih.popcorn.other.Constants.tvSearch
import com.fatih.popcorn.other.State
import com.fatih.popcorn.other.Status
import com.fatih.popcorn.ui.DetailsFragment
import com.fatih.popcorn.viewmodel.DetailsFragmentViewModel
import com.fatih.popcorn.viewmodel.RecommendationFragmentViewModel
import kotlinx.coroutines.*

class RecommendFragment:Fragment(R.layout.fragment_recommend) {

    private var _binding: FragmentRecommendBinding?=null
    private val binding: FragmentRecommendBinding
        get() = _binding!!
    private var view: View?=null
    private var recyclerView: RecyclerView?=null
    private var job: Job?=null
    private val handler = CoroutineExceptionHandler{ _,throwable->
        println("Caught exception $throwable")
    }
    private var totalAvailablePages=2
    private lateinit var onScrollListener:RecyclerView.OnScrollListener
    private var selectedId=1
    private lateinit var recommendAdapter: RecommendFragmentAdapter
    private lateinit var viewModel: RecommendationFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= FragmentRecommendBinding.inflate(inflater,container,false)
        view=binding.root
        selectedId=arguments?.getInt("id")?:selectedId
        viewModel= ViewModelProvider(requireActivity())[RecommendationFragmentViewModel::class.java]
        doInitialization()
        return view
    }

    private fun doInitialization(){
        recommendAdapter= RecommendFragmentAdapter(R.layout.fragment_recommend_row)
        recyclerView=binding.recommendRecyclerView
        recyclerView!!.layoutManager = StaggeredGridLayoutManager(Resources.getSystem().displayMetrics.widthPixels/200, VERTICAL)
        recyclerView!!.adapter = recommendAdapter
        onScrollListener=object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1) && viewModel.recommendationCurrentPage.value!! < totalAvailablePages) {
                    viewModel.recommendationCurrentPage.value= viewModel.recommendationCurrentPage.value!! +1
                    if (Constants.checkIsItInMovieListOrNot()){
                        viewModel.getRecommendations(movieSearch,selectedId)
                    }else{
                        viewModel.getRecommendations(tvSearch,selectedId)
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        }
        recyclerView!!.addOnScrollListener(onScrollListener)
        if (Constants.checkIsItInMovieListOrNot()){
            viewModel.getRecommendations(movieSearch,selectedId)
        }else{
            viewModel.getRecommendations(tvSearch,selectedId)
        }
        observeLiveData()
    }

    private fun observeLiveData(){
        viewModel.discoverResponse.observe(viewLifecycleOwner){
            when(it.status){
                Status.SUCCESS->{
                    it.data?.let {
                        recommendAdapter.list=it.results
                    }
                }
                else->Unit
            }
        }
    }

    override fun onDestroyView() {
        job?.cancel()
        recyclerView?.adapter = null
        recyclerView=null
        _binding=null
        view=null
        super.onDestroyView()
    }
}