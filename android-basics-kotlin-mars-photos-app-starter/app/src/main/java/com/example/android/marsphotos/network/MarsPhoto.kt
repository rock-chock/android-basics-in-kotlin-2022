package com.example.android.marsphotos.network

import com.squareup.moshi.Json

data class MarsPhoto (
    val id: String,
    // Annotate to use a variable name that differs from the key name in JSON response
    @Json(name = "img_src") val imgSrcUrl: String
    )