package dependencies

interface TensorFlowLiteDependencies {

    val tensorFlowLiteTaskVision
        get() = "org.tensorflow:tensorflow-lite-task-vision:0.4.0"

    val tensorFlowLiteGpuDelegatePlugin
        get() = "org.tensorflow:tensorflow-lite-gpu-delegate-plugin:0.4.0"

    val tensorFlowLiteGpu
        get() = "org.tensorflow:tensorflow-lite-gpu:2.9.0"

}