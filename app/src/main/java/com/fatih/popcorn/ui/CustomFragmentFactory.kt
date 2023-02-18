package com.fatih.popcorn.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.fatih.popcorn.adapter.CastRecyclerViewAdapter
import com.fatih.popcorn.adapter.HomeFragmentAdapter
import javax.inject.Inject

class CustomFragmentFactory @Inject constructor(private val adapter: HomeFragmentAdapter,private val castAdapter: CastRecyclerViewAdapter):FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){
            HomeFragment::class.java.name->HomeFragment(adapter)
            DetailsFragment::class.java.name->DetailsFragment(castAdapter)
            else->super.instantiate(classLoader, className)
        }
    }
}