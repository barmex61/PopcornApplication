package com.fatih.popcorn.ui.tabfragments

import android.content.res.Resources
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


class AboutFragment:Fragment() {

    private var view: View?=null
    private var _binding: FragmentAboutBinding? = null
    private val binding:FragmentAboutBinding
    get() = _binding!!
    private lateinit var detailResponse:DetailResponse


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= FragmentAboutBinding.inflate(inflater,container,false)
        view=binding.root
        detailResponse=arguments?.getSerializable("detailResponse")?.let {
            it as DetailResponse
        }?:detailResponse
        doInitialization()
        return view
    }

    private fun doInitialization(){
        binding.apply {
            textDescription.text=detailResponse.overview?:"-"
            originalNameText.text=detailResponse.original_name?:detailResponse.original_title?:"-"
            statusText.text=detailResponse.status?:"-"
            timeText.text=detailResponse.runtime?.let {
                "$it min"
            }?:detailResponse.episode_run_time?.let {
                if (it.isNotEmpty()){
                    "${it[0]} min average"
                }else{
                    "-"
                } }?:"-"
            originalLanguageText.text=detailResponse.original_language?:"-"
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
                    "-"
                } }?:"-"
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
                    "-"
                } }?:"-"

            fundText.text=detailResponse.budget?.let {
                if (it>=1000000){
                    "$${it/1000000} million "
                }else if (it>=100000){
                    "$${it/1000}k"
                }else{
                    "$${it}"
                }
            }?:"-"
            incomeText.text=detailResponse.revenue?.let {
                if (it>=1000000){
                    "$${it/1000000} million "
                }else if (it>=100000){
                    "$${it/1000}k"
                }else{
                    "$${it}"
                }
            }?:"-"
            taglineText.text=(detailResponse.tagline?:"-").let {
                it.ifEmpty {
                    "-"
                }
            }
            releaseDateText.text=detailResponse.release_date?:detailResponse.last_air_date?.let {
                "Last episode : $it"
            }
            voteAverageText.text=detailResponse.vote_average?.toString()?:"-"
            voteCountText.text=detailResponse.vote_count?.toString()?:"-"
            detailResponse.genres?.let {list->
                for (text in list.map { it.name }){
                    val textView=TextView(requireContext())
                    val layoutParams=ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT)
                    textView.layoutParams=layoutParams
                    textView.text = text
                    textView.setPadding(10,10,10,10)
                    textView.setTextColor(resources.getColor(R.color.white, null))
                    textView.setBackgroundResource(R.drawable.review_background)
                    textView.id=View.generateViewId()
                    binding.detailsLayout.addView(textView)
                    binding.flow.addView(textView)
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