package com.fatih.popcorn.ui

import android.animation.Animator
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.fatih.popcorn.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay


@SuppressLint("CustomSplashScreen")
class CustomSplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {

    private var lottieView:LottieAnimationView?=null
    private var animatorListener:Animator.AnimatorListener?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lottieView=view.findViewById(R.id.splashLottieView)
        animatorListener=object :Animator.AnimatorListener{
            override fun onAnimationCancel(animation: Animator)=Unit
            override fun onAnimationEnd(animation: Animator) {
                findNavController().navigate(CustomSplashScreenFragmentDirections.actionCustomSplashScreenFragmentToMainFragment())
            }
            override fun onAnimationRepeat(animation: Animator) =Unit
            override fun onAnimationStart(animation: Animator)=Unit
        }
        lottieView?.addAnimatorListener(animatorListener)
        lottieView?.playAnimation()
    }

    override fun onDestroyView() {
        lottieView?.removeAnimatorListener(animatorListener)
        lottieView=null
        super.onDestroyView()
    }
}