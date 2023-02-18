package com.fatih.popcorn.entities.remote.creditsresponse

data class CreditsResponse(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)