package com.example.audiorecorderexample.recorder

import java.io.File

interface AudioRecorder {
    fun start(outputFile: File)
    fun stop()
}