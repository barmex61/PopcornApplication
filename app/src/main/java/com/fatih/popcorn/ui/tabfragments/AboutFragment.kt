package com.fatih.popcorn.ui.tabfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fatih.popcorn.R
import com.fatih.popcorn.databinding.FragmentAboutBinding

class AboutFragment:Fragment() {

    private var view: View?=null
    private var _binding: FragmentAboutBinding? = null
    private val binding:FragmentAboutBinding
    get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= FragmentAboutBinding.inflate(inflater,container,false)
        view=binding.root
        doInitialization()
        return view
    }

    private fun doInitialization(){
        val textView=binding.bilgiText
        binding.flow.addView(textView)
    }

    override fun onDestroyView() {
        _binding=null
        view=null
        super.onDestroyView()
    }
}