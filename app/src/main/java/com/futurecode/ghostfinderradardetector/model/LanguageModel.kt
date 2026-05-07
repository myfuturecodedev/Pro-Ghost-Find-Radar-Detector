package com.futurecode.ghostfinderradardetector.model

data class LanguageModel(
    val name: String,
    val code: String,
    val flag: Int,
    var isSelected: Boolean = false
)