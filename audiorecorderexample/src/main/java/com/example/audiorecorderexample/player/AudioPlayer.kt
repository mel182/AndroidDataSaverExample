package com.example.audiorecorderexample.player

import java.io.File

interface AudioPlayer {
    fun playFile(outputFile: File)
    fun stop()
}