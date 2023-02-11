package com.fatih.popcorn.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.fatih.popcorn.BuildConfig
import com.fatih.popcorn.R
import com.fatih.popcorn.other.Status
import com.fatih.popcorn.viewmodel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
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
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory=fragmentFactory
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel=ViewModelProvider(this)[HomeFragmentViewModel::class.java]
        viewModel.getMovies(1, "popularity.desc","28")
        installSplashScreen().apply {
            setKeepOnScreenCondition{
                viewModel.discoverData.value?.status== Status.LOADING
            }
        }
        setContentView(R.layout.activity_main)
        setupNavController()
    }

    private fun setupNavController(){
        navHostFragment=supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController=navHostFragment.navController
    }

}