package com.fatih.popcorn.ui.tabfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.fatih.popcorn.R
import com.fatih.popcorn.databinding.FragmentAboutBinding
import com.fatih.popcorn.entities.remote.detailresponse.DetailResponse


class AboutFragment:Fragment() {

    private var view: View?=null
    private var _binding: FragmentAboutBinding? = null
    private val binding:FragmentAboutBinding
    get() = _binding!!
    private lateinit var detailResponse:DetailResponse


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= DataBindingUtil.inflate(inflater,R.layout.fragment_about,container,false)
        view=binding.root
        detailResponse=arguments?.getSerializable("detailResponse")?.let {
            it as DetailResponse
        }?:detailResponse
        doInitialization()
        return view
    }

    private fun doInitialization(){
        binding.detailsResponse=detailResponse
        binding.apply {

            detailResponse.genres?.let {list->
                if (list.isNotEmpty()){
                    binding.tR.visibility=View.VISIBLE
                    binding.flow.visibility=View.VISIBLE
                    for (text in list.map { it.name }){
                        val textView=TextView(requireContext())
                        val layoutParams=ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT)
                        textView.layoutParams=layoutParams
                        textView.text = text
                        textView.setPadding(10,10,10,10)
                        textView.setTextColor(resources.getColor(R.color.white, null))
                        textView.setBackgroundResource(R.drawable.flow_item_bg)
                        textView.id=View.generateViewId()
                        binding.detailsLayout.addView(textView)
                        binding.flow.addView(textView)
                    }
                }else{
                    binding.tR.visibility=View.INVISIBLE
                    binding.flow.visibility=View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding=null
        view=null
        super.onDestroyView()
    }
}