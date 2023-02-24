package com.fatih.popcorn.ui.tabfragments

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fatih.popcorn.R
import com.fatih.popcorn.adapter.RecommendFragmentAdapter
import com.fatih.popcorn.databinding.FragmentFamiliarBinding
import com.fatih.popcorn.other.Constants
import com.fatih.popcorn.other.Constants.checkIsItInMovieListOrNot
import com.fatih.popcorn.other.Constants.movieSearch
import com.fatih.popcorn.other.Constants.tvSearch
import com.fatih.popcorn.other.Status
import com.fatih.popcorn.viewmodel.FamiliarFragmentViewModel

class FamiliarFragment:Fragment(R.layout.fragment_familiar) {

    private var _binding: FragmentFamiliarBinding?=null
    private val binding: FragmentFamiliarBinding
        get() = _binding!!
    private var recyclerView: RecyclerView?=null
    private var totalAvailablePages=2
    private lateinit var onScrollListener: RecyclerView.OnScrollListener
    private var selectedId=1
    private lateinit var recommendAdapter: RecommendFragmentAdapter
    private lateinit var viewModel: FamiliarFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= FragmentFamiliarBinding.inflate(inflater,container,false)
        viewModel= ViewModelProvider(requireActivity())[FamiliarFragmentViewModel::class.java]
        recommendAdapter= RecommendFragmentAdapter(R.layout.fragment_recommend_row)
        selectedId=arguments?.getInt("id")?:selectedId
        if (savedInstanceState?.getBoolean("isRotated") != true){
            viewModel.resetData()
        }
        recommendAdapter.setMyOnClickLambda {url, id, pair ->
            pair?.let {
                findNavController().navigate(R.id.action_detailsFragment_self, bundleOf("id" to id,"vibrantColor" to it.first,"darkMutedColor" to it.second,"url" to url),
                    NavOptions.Builder().setPopUpTo(R.id.detailsFragment,true).build())
            }?: findNavController().navigate(R.id.action_detailsFragment_self, bundleOf("id" to id,"vibrantColor" to R.color.white,"darkMutedColor" to R.color.black2, "url" to url),
                NavOptions.Builder().setPopUpTo(R.id.detailsFragment,true).build())
        }
        doInitialization()
        return binding.root
    }

    private fun doInitialization(){
        recyclerView=binding.familiarRecyclerView
        recyclerView!!.layoutManager = GridLayoutManager(requireContext(), Resources.getSystem().displayMetrics.widthPixels/200)
        recyclerView!!.adapter = recommendAdapter
        onScrollListener=object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1) && viewModel.familiarCurrentPage.value!! < totalAvailablePages) {
                    viewModel.familiarCurrentPage.value= viewModel.familiarCurrentPage.value!! +1
                    if (checkIsItInMovieListOrNot()){
                        viewModel.getFamiliars(movieSearch,selectedId)
                    }else{
                        viewModel.getFamiliars(tvSearch,selectedId)
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        }
        recyclerView!!.addOnScrollListener(onScrollListener)
        if (checkIsItInMovieListOrNot()){
            viewModel.getFamiliars(movieSearch,selectedId)
        }else{
            viewModel.getFamiliars(tvSearch,selectedId)
        }
        observeLiveData()
    }

    private fun observeLiveData(){
        viewModel.discoverResponse.observe(viewLifecycleOwner){
            when(it.status){
                Status.SUCCESS->{
                    it.data?.let {
                        totalAvailablePages=it.total_pages
                        recommendAdapter.list=it.results
                    }?:{
                        binding.familiarLottie.visibility=View.VISIBLE
                        binding.familiarLottie.playAnimation()
                    }
                    if (it.data?.total_results == 0){
                        binding.familiarLottie.visibility=View.VISIBLE
                        binding.familiarLottie.playAnimation()
                    }
                }
                else->Unit
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("isRotated",true)
        println("onsave")
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        recyclerView?.adapter = null
        recyclerView=null
        _binding=null
        super.onDestroyView()
    }
}