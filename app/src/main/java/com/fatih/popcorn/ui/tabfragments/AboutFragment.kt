package com.fatih.popcorn.ui.tabfragments

import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fatih.popcorn.R
import com.fatih.popcorn.databinding.FragmentAboutBinding
import com.fatih.popcorn.entities.remote.detailresponse.DetailResponse
import com.google.android.material.internal.FlowLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AboutFragment @Inject constructor(private val detailResponse: DetailResponse):Fragment() {

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
        binding.apply {
            textDescription.text=detailResponse.overview?:"-"
            originalNameText.text=detailResponse.original_name?:detailResponse.original_title?:"-"
            statusText.text=detailResponse.status?.let {
              "$it min"
            }?:"-"
            timeText.text=detailResponse.runtime?.toString()?:detailResponse.episode_run_time?.let {
                if (it.isNotEmpty()){
                    it[0].toString()
                }else{
                    ""
                } }?:""
            originalLanguageText.text=detailResponse.original_language
            countryText.text=detailResponse.production_countries?.let {
                if(it.isNotEmpty()){
                    var countryText=""
                    for (index in it.indices){
                        countryText += if (index == 0){
                            it[index].name
                        }else{
                            ",\n${it[index].name}"
                        }
                    }
                    countryText
                }else{
                    ""
                } }?:""
            companyText.text=detailResponse.production_companies?.let {
                if(it.isNotEmpty()){
                    var companyText=""
                    for (index in it.indices){
                        companyText += if (index == 0){
                            it[index].name
                        }else{
                            ",\n${it[index].name}"
                        }
                    }
                    companyText
                }else{
                    ""
                } }?:""

            fundText.text=detailResponse.budget?.let {
                println(it.toString())
               "$${it/1000000} million "
            }?:"-"
            incomeText.text=detailResponse.revenue?.let {
                println(it.toString())
                "$${it/1000000} million"
            }?:"-"
            taglineText.text=detailResponse.tagline?:"-"
            voteAverageText.text=detailResponse.vote_average?.toString()?:"-"
            voteCountText.text=detailResponse.vote_count?.toString()?:"-"
        }
    }

    override fun onDestroyView() {
        _binding=null
        view=null
        super.onDestroyView()
    }
}