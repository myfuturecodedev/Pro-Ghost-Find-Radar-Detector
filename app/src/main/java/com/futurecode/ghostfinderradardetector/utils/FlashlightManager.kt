package com.futurecode.ghostfinderradardetector.utils

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager

class FlashlightManager(context: Context) {

    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private var cameraId: String? = null

    init {
        try {
            // Usually, the first camera (index 0) is the rear camera with the flash
            cameraId = cameraManager.cameraIdList[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    fun toggleFlashlight(isEnabled: Boolean) {
        try {
            cameraId?.let {
                cameraManager.setTorchMode(it, isEnabled)
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }
}