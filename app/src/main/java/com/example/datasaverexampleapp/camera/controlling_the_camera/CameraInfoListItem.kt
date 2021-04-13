package com.example.datasaverexampleapp.camera.controlling_the_camera

import android.hardware.camera2.params.StreamConfigurationMap

data class CameraInfoListItem(
    val id: Int,
    val orientation: Int,
    val facing: Facing,
    val canDisableShutterSound: Boolean? = false,
    val controlAutoFocusModes: IntArray,
    val scalerStreamMap: StreamConfigurationMap?
)