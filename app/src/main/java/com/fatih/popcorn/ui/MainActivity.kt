package com.fatih.popcorn.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.fatih.popcorn.R
import com.fatih.popcorn.other.Constants.isFirstRun
import com.fatih.popcorn.other.Constants.orientation
import com.fatih.popcorn.viewmodel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var navHostFragment:NavHostFragment
    private lateinit var viewModel:HomeFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel=ViewModelProvider(this)[HomeFragmentViewModel::class.java]
        val currentOrientation= resources.configuration.orientation
        if(isFirstRun && currentOrientation == orientation){
            viewModel.getMovies( "popularity.desc","")
            isFirstRun=false
        }
        orientation=currentOrientation
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