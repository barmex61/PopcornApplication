package com.fatih.popcorn.ui

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.fatih.popcorn.R
import com.fatih.popcorn.databinding.FragmentDetailsBinding
import com.fatih.popcorn.entities.remote.detailresponse.DetailResponse
import com.fatih.popcorn.other.Constants.checkIsItInMovieListOrNot
import com.fatih.popcorn.other.Constants.colorMatrixColorFilter
import com.fatih.popcorn.other.Constants.movieSearch
import com.fatih.popcorn.other.Constants.tvSearch
import com.fatih.popcorn.other.Status
import com.fatih.popcorn.other.setImageUrl
import com.fatih.popcorn.viewmodel.DetailsFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private lateinit var viewModel: DetailsFragmentViewModel
    private lateinit var binding: FragmentDetailsBinding
    private var selectedId: Int? = null
    private var selectedResponse: DetailResponse? = null
    private var vibrantColor: Int? = null
    private var darkMutedColor: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        doInitialization()
        //binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, _, _, _ ->
        //binding.layoutHeader.y= binding.nestedScrollView.scrollY.toFloat()-binding.nestedScrollView.scrollY.toFloat()/2.6f })
        return binding.root
    }

    private fun doInitialization() {
        setStatusBarPadding()
        viewModel = ViewModelProvider(this)[DetailsFragmentViewModel::class.java]
        //binding.trailerImage.setOnClickListener { youtube() }
        //binding.watchList.setOnClickListener { watchList() }
        binding.watchListButton.setOnClickListener { findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToWatchListFragment()) }
        //binding.reviewImage.setOnClickListener { goWeb() }
        binding.backButton.setOnClickListener { findNavController().navigateUp() }
        //binding.episodesImage.setOnClickListener {view-> goEpisodes(view) }
        binding.backgroundImage.colorFilter = colorMatrixColorFilter
        arguments?.let {
            selectedId = DetailsFragmentArgs.fromBundle(it).id
            // isItInDatabase(selectedTvShowId!!)
            darkMutedColor = DetailsFragmentArgs.fromBundle(it).darkMutedColor
            vibrantColor = DetailsFragmentArgs.fromBundle(it).vibrantColor
        }
        if (checkIsItInMovieListOrNot()) {
            viewModel.getDetails(movieSearch, selectedId!!, "")
        } else {
            viewModel.getDetails(tvSearch, selectedId!!, "")
        }
        observeLiveData()
    }

    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    private fun setStatusBarPadding() {
        val statusBarHeightId = resources.getIdentifier("status_bar_height", "dimen", "android")
        val statusBarHeight = resources.getDimensionPixelSize(statusBarHeightId) + 10
        binding.linearLayout.updatePadding(top = statusBarHeight)
    }

    private fun observeLiveData() {

        viewModel.detailResponse.observe(viewLifecycleOwner) { resource ->
            if (resource != null) {
                when (resource.status) {
                    Status.LOADING -> {
                        setLayoutVisibility(show = false, showToast = false, null)
                    }
                    Status.ERROR -> {
                        setLayoutVisibility(show = false, showToast = true, resource.message)
                    }
                    Status.SUCCESS -> {
                        resource.data?.let {
                            selectedResponse = it
                            setLayoutVisibility(show = true, showToast = false, null)
                        }
                    }
                }
            }
        }

    }

    private fun setLayoutVisibility(show: Boolean, showToast: Boolean, message: String?) {
        binding.mainLayout.visibility = View.INVISIBLE
        if (showToast) {
            binding.mainLayout.visibility = View.GONE
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            return
        }
        val trailerImage = binding.trailerImage
        val reviewImage = binding.reviewImage
        if (show) {
            trailerImage.visibility = View.VISIBLE
            reviewImage.visibility = View.VISIBLE
            setTints()
        } else {
            trailerImage.visibility = View.INVISIBLE
            reviewImage.visibility = View.INVISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTints() {
        val vibrantColor=ColorStateList.valueOf(vibrantColor!!)
        val darkMutedColor=ColorStateList.valueOf(darkMutedColor!!)
        binding.ratingText.setTextColor(vibrantColor)
        binding.imgPlay.backgroundTintList = vibrantColor
        binding.imgPlay.imageTintList= darkMutedColor
        binding.saveImage.backgroundTintList = vibrantColor
        binding.saveImage.imageTintList = darkMutedColor
        binding.trailerImage.imageTintList = vibrantColor
        binding.trailerText.setTextColor(darkMutedColor)
        binding.videoImage.imageTintList=darkMutedColor
        binding.reviewImage.imageTintList = darkMutedColor
        binding.reviewText.setTextColor(vibrantColor)
        binding.ratingBar.rating = selectedResponse?.vote_average?.toFloat()?:0f
        binding.ratingBar.progressDrawable.setColorFilter(this.vibrantColor!!,PorterDuff.Mode.SRC_ATOP)

        selectedResponse?.let { it ->
            binding.posterImage.setImageUrl(it.poster_path)
            binding.genreText.text = it.genres?.let {
                if(it.isNotEmpty()){
                    it[0].name
                }else{
                    ""
                }
            }
            binding.textDescription.text = it.overview
            binding.runtimeText.text = "${it.runtime} min"
            binding.ratingText.text = it.vote_average?.toBigDecimal()?.setScale(1, RoundingMode.UP)?.toString() + "/10"
            binding.nameText.text = it.original_title
            binding.yearText.text = it.release_date

            if (checkIsItInMovieListOrNot()) {
                binding.episodesImage.visibility = View.INVISIBLE
                binding.episodesText.visibility = View.INVISIBLE
            } else {
                binding.episodesImage.visibility = View.VISIBLE
                binding.episodesText.visibility = View.VISIBLE
            }

        }
        binding.mainLayout.apply {
            visibility=View.VISIBLE
            startAnimation(AnimationUtils.loadAnimation(requireContext(),R.anim.fall_down))
        }
    }

}