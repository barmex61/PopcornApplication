package com.fatih.popcorn.ui.tabfragments

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fatih.popcorn.R
import com.fatih.popcorn.adapter.CastRecyclerViewAdapter
import com.fatih.popcorn.databinding.FragmentCastBinding
import com.fatih.popcorn.other.Status
import com.fatih.popcorn.ui.DetailsFragment
import com.fatih.popcorn.viewmodel.DetailsFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject


@AndroidEntryPoint
class CastFragment : Fragment(R.layout.fragment_cast) {

    private var _binding:FragmentCastBinding?=null
    private val binding:FragmentCastBinding
    get() = _binding!!
    private var view:View?=null
    private var recyclerView:RecyclerView?=null
    private var job: Job?=null
    private val handler = CoroutineExceptionHandler{ _,throwable->
        println("Caught exception $throwable")
    }
    @Inject
    lateinit var castAdapter:CastRecyclerViewAdapter
    private lateinit var viewModel: DetailsFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= FragmentCastBinding.inflate(inflater,container,false)
        view=binding.root
        viewModel=ViewModelProvider(requireActivity())[DetailsFragmentViewModel::class.java]
        doInitialization()
        return view
    }

    private fun doInitialization(){

        recyclerView=binding.veilRecyclerView
        recyclerView!!.layoutManager = GridLayoutManager(requireContext(),Resources.getSystem().displayMetrics.widthPixels/200)
        castAdapter.vibrantColor=DetailsFragment.vibrantColor!!
        recyclerView!!.adapter = castAdapter
        observeLiveData()
    }

    private fun observeLiveData(){
        viewModel.creditsResponse.observe(viewLifecycleOwner){
            when(it.status){
                Status.SUCCESS->{
                    it.data?.let {
                        job?.cancel()
                        job=lifecycleScope.launch(Dispatchers.Main + handler){
                            val castList=async(Dispatchers.Default){
                                it.cast.filter {
                                    !it.profile_path.isNullOrEmpty() && it.profile_path != "null"
                                }.distinctBy {
                                    it.profile_path
                                }.map { cast->
                                    Triple(cast.name,cast.character,cast.profile_path!!)
                                }
                            }
                            val crewList=async(Dispatchers.Default){
                                it.crew.filter {
                                    !it.profile_path.isNullOrEmpty() && it.profile_path != "null"
                                }.distinctBy {
                                    it.profile_path
                                }.map { crew->
                                    Triple(crew.name,crew.job,crew.profile_path!!)
                                }
                            }
                            castAdapter.castList=castList.await() + crewList.await()
                        }
                    }
                }
                else->Unit
            }
        }
    }

    override fun onDestroyView() {
        recyclerView=null
        _binding=null
        view=null
        super.onDestroyView()
    }

}