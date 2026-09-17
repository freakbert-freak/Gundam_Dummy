package com.example.projek_gundam

data class Gundam(
    val id: Int,
    val wikiName: String,
    val wikiUrl: String,
    val name: String,
    val header: String,
    val details: String?,
    val imgUrl: String,
    val pilots: String
)

data class ApiInfo(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

data class GundamResponse(
    val info: ApiInfo,
    val results: List<Gundam>
)