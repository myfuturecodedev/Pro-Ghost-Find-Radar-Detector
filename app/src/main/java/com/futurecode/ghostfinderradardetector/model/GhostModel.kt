package com.futurecode.ghostfinderradardetector.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep // 👈 Add this annotation
@Parcelize
data class GhostModel(
    val id: Int,
    val image: Int,
    var isSelected: Boolean = false,
    val name: String = "",
    val age: String = "Unknown",
    val dangerLevel: String = "",
    val description: String = "",
    val sounds: Int = 0,          // 👈 default value
    var isCaptured: Boolean = false

) : Parcelable