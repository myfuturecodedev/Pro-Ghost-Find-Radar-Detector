package com.futurecode.ghostfinderradardetector.utils

import com.futurecode.ghostfinderradardetector.model.ScarySoundModel

sealed class UIItem {
    data class Content(val data: ScarySoundModel) : UIItem() // Change per screen
    object AdUnit : UIItem()
}