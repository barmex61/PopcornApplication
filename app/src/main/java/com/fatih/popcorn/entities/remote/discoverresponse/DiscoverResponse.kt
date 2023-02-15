package com.fatih.popcorn.entities.remote.discoverresponse

data class DiscoverResponse(
    var page: Int,
    var results: List<DiscoverResult>,
    var total_pages: Int,
    var total_results: Int,
    var genres:String?,
    var sortBy:String?
)