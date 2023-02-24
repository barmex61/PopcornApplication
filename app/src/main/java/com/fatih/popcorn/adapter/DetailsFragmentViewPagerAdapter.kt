package com.fatih.popcorn.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter

class DetailsFragmentViewPagerAdapter (var fragmentList:List<Fragment>,fm:FragmentManager,lifecycle: Lifecycle) :FragmentStateAdapter(fm,lifecycle){

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}