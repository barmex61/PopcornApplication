package com.fatih.popcorn.other

import com.fatih.popcorn.ui.CustomFragmentFactory
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@EntryPoint
@InstallIn(ActivityComponent::class)
interface CustomFragmentFactoryEntryPoint {
    fun getFragmentFactory(): CustomFragmentFactory
}