package com.example.datasaverexampleapp.background_task

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.datasaverexampleapp.R

/**
 * WorkManager is an API that makes it easy to schedule deferrable, asynchronous
 * tasks that are expected to run even if the app exits or the device restarts.
 * The WorkManager API is a suitable and recommended replacement for all previous
 * Android background scheduling APIs, including FirebaseJobDispatcher, GcmNetworkManager,
 * and Job Scheduler.
 */
class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {

        val inputData = inputData // Retrieving input data from the request
        val task = inputData.getString(WorkManagerActivity.TASK_KEY)?:""
        val description = inputData.getString(WorkManagerActivity.DESCRIPTION_KEY)?:""

//        displayNotification(task,description) // Task to execute
        performTask()

        // Create output data
        val outputData = Data.Builder()
            .putString(WorkManagerActivity.TASK_KEY,task)
            .putString(WorkManagerActivity.DESCRIPTION_KEY,"Task Finished Successfully")
            .build()

        return Result.success(outputData)
    }

    private fun performTask()
    {
        Log.i("TAG","Task Perform")
    }

    private fun displayNotification(task:String, description:String)
    {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelID = "work_manager"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val notificationChannel = NotificationChannel("work_manager","Work Manager example", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notificationBuilder = NotificationCompat.Builder(applicationContext,channelID)
            .setContentTitle(task)
            .setContentText(description)
            .setSmallIcon(R.mipmap.ic_launcher)

        notificationManager.notify(1,notificationBuilder.build())
    }
}