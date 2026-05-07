package com.futurecode.ghostfinderradardetector.model

data class ScarySoundModel(
    val id: Int,
    val title: String,
    val icon: Int,
    var isPlaying: Boolean = false,
    val sounds:Int
)