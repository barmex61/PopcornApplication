package com.fatih.popcorn.ui

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fatih.popcorn.R
import com.fatih.popcorn.adapter.HomeFragmentAdapter
import com.fatih.popcorn.adapter.SeasonAdapter
import com.fatih.popcorn.adapter.WatchListAdapter
import com.fatih.popcorn.databinding.FragmentSeasonsBinding
import com.fatih.popcorn.databinding.FragmentWatchListBinding
import com.fatih.popcorn.other.Constants.movieSearch
import com.fatih.popcorn.other.Constants.tvSearch
import com.fatih.popcorn.other.Status
import com.fatih.popcorn.viewmodel.DetailsFragmentViewModel

class WatchListFragment:Fragment() {

    private val viewModel: DetailsFragmentViewModel by lazy{
        ViewModelProvider(requireActivity())[DetailsFragmentViewModel::class.java]
    }
    private var _binding: FragmentWatchListBinding?=null
    private val binding: FragmentWatchListBinding
        get() = _binding!!

    private var watchListAdapter: WatchListAdapter?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding= DataBindingUtil.inflate(inflater,R.layout.fragment_watch_list,container,false)
        setStatusBarPadding()
        watchListAdapter= WatchListAdapter(R.layout.watch_list_row).apply {
            setMyOnClickLambda { url, id, pair ,isTvShow->
                val search= if (isTvShow == true) tvSearch else movieSearch
                pair?.let {
                    findNavController().navigate(WatchListFragmentDirections.actionWatchListFragmentToDetailsFragment(id,pair.first,pair.second,url,search))
                }?: findNavController().navigate(WatchListFragmentDirections.actionWatchListFragmentToDetailsFragment(id,R.color.white,R.color.black2,url,search))
            }
        }
        binding.watchListRecyclerView.apply {
            layoutManager= GridLayoutManager(requireContext(), Resources.getSystem().displayMetrics.widthPixels/200)
            adapter=watchListAdapter
            hasFixedSize()
        }
        observeLiveData()
        return binding.root
    }

    private fun observeLiveData(){
        viewModel.entityList.observe(viewLifecycleOwner) { result->
            watchListAdapter?.list=result
        }
    }

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    private fun setStatusBarPadding(){
        val statusBarHeightId = resources.getIdentifier("status_bar_height", "dimen", "android")
        val statusBarHeight = resources.getDimensionPixelSize(statusBarHeightId) +10
        binding.layoutWatchList.updatePadding(top = statusBarHeight)
    }

    override fun onDestroyView() {
        watchListAdapter=null
        _binding=null
        super.onDestroyView()
    }
}