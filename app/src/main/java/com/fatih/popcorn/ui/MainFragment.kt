package com.fatih.popcorn.ui

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fatih.popcorn.R
import com.fatih.popcorn.adapter.Adapter
import com.fatih.popcorn.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment: Fragment(R.layout.fragment_main) {

    private lateinit var binding:FragmentMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=DataBindingUtil.bind(view)!!
        val list= listOf<String>("Yes","No","Yes","Yes","No","Yes","Yes","No","Yes","Yes","No","Yes","Yes","No","Yes","Yes","No","Yes")
        binding.moviesRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        val adapter=Adapter().apply {
            this.list=list.toMutableList()
        }
        binding.moviesRecyclerView.adapter=adapter

    }
}