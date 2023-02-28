package com.fatih.popcorn.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
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

    private var navController: NavController?=null
    private var navHostFragment:NavHostFragment?=null
    private var viewModel:HomeFragmentViewModel?=null
    private var navControllerState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel=ViewModelProvider(this)[HomeFragmentViewModel::class.java]
        val currentOrientation= resources.configuration.orientation
        if(isFirstRun && currentOrientation == orientation){
            viewModel?.getMovies( "popularity.desc","")
            isFirstRun=false
        }
        orientation=currentOrientation
        setContentView(R.layout.activity_main)
        setupNavController()
        if (savedInstanceState != null) {
            navControllerState = savedInstanceState.getBundle("navControllerState");
            navController!!.restoreState(navControllerState);
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {

        super.onSaveInstanceState(outState, outPersistentState)
        navControllerState = navController!!.saveState()
        outState.putBundle("navControllerState", navControllerState)
    }

    private fun setupNavController(){
        navHostFragment=supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController=navHostFragment?.navController
    }

    override fun onDestroy() {
        isFirstRun=true
        viewModel=null
        navHostFragment=null
        navController=null
        super.onDestroy()
    }

}