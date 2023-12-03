package dependencies

interface AndroidCameraXDependencies {

    val cameraXCore
        get() = "androidx.camera:camera-core:${Versions.cameraXVersion}"

    val cameraXCamera2
        get() = "androidx.camera:camera-camera2:${Versions.cameraXVersion}"

    val cameraXLifeCycle
        get() = "androidx.camera:camera-lifecycle:${Versions.cameraXVersion}"

    val cameraXVideo
        get() = "androidx.camera:camera-video:${Versions.cameraXVersion}"

    val cameraXView
        get() = "androidx.camera:camera-view:${Versions.cameraXVersion}"

    val cameraXExtensions
        get() = "androidx.camera:camera-extensions:${Versions.cameraXVersion}"
}