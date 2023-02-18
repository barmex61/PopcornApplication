package com.fatih.popcorn.ui

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.fatih.popcorn.BuildConfig
import com.fatih.popcorn.R
import com.fatih.popcorn.other.Constants.isFirstRun
import com.fatih.popcorn.other.Constants.orientation
import com.fatih.popcorn.other.CustomFragmentFactoryEntryPoint
import com.fatih.popcorn.other.Status
import com.fatih.popcorn.viewmodel.HomeFragmentViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var navHostFragment:NavHostFragment

    @Inject
    lateinit var fragmentFactory: CustomFragmentFactory

    companion object {
        lateinit var viewModel: HomeFragmentViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val entryPoint = EntryPointAccessors.fromActivity(
            this,
            CustomFragmentFactoryEntryPoint::class.java
        )
        supportFragmentManager.fragmentFactory = entryPoint.getFragmentFactory()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel=ViewModelProvider(this)[HomeFragmentViewModel::class.java]
        val currentOrientation= resources.configuration.orientation
        if(isFirstRun && currentOrientation == orientation){
            viewModel.getMovies( "popularity.desc","")
            isFirstRun=false
        }
        orientation=currentOrientation
        installSplashScreen().apply {
            setKeepOnScreenCondition{
                viewModel.discoverData.value?.status== Status.LOADING
            }
        }
        setContentView(R.layout.activity_main)
        setupNavController()
    }
    override fun onDestroy() {
        isFirstRun=true
        super.onDestroy()
    }
    private fun setupNavController(){
        navHostFragment=supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController=navHostFragment.navController
    }

}