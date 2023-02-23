package com.example.jetpackcomposedownloadmanagerexample

interface Downloader {
    fun downloadFile(url:String) : Long
}