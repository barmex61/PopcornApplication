package com.fatih.popcorn.ui.tabfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.fatih.popcorn.R
import com.fatih.popcorn.databinding.FragmentCastBinding
import com.fatih.popcorn.entities.remote.detailresponse.DetailResponse
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CastFragment @Inject constructor(): Fragment(R.layout.fragment_cast) {

    private var _binding:FragmentCastBinding?=null
    private val binding:FragmentCastBinding
    get() = _binding!!
    private var view:View?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding=DataBindingUtil.inflate(inflater,R.layout.fragment_cast,container,false)

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}