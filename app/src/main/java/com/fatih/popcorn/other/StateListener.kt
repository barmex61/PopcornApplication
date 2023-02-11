package com.fatih.popcorn.other

interface StateListener {
    fun stateCallback(stateList:MutableList<State>):State
}