package com.example.datasaverexampleapp.work_manager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.example.datasaverexampleapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * WorkManager is an API that makes it easy to schedule deferrable, asynchronous
 * tasks that are expected to run even if the app exits or the device restarts.
 * The WorkManager API is a suitable and recommended replacement for all previous
 * Android background scheduling APIs, including FirebaseJobDispatcher, GcmNetworkManager,
 * and Job Scheduler.
 */
class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    private var counter = 0

    override fun doWork(): Result {

        Log.i("TAG","Do work My worker class called!")

        val inputData = inputData // Retrieving input data from the request
        val task = inputData.getString(WorkManagerActivity.TASK_KEY)?:""
        val description = inputData.getString(WorkManagerActivity.DESCRIPTION_KEY)?:""

        displayNotification(task,description) // Task to execute
        
        // Create output data
        val outputData = Data.Builder()
            .putString(WorkManagerActivity.TASK_KEY,task)
            .putString(WorkManagerActivity.DESCRIPTION_KEY,"Task Finished Successfully")
            .build()

        // -------------

        // Create input data for processing
        val message = Data.Builder()
            .putString(WorkManagerActivity.TASK_KEY,"Work Manager")
            .putString(WorkManagerActivity.DESCRIPTION_KEY,"Work is finished")
            .build()

        // Create constraints which contain the conditions on
        // which the task must be executed
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Create one time request
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setInputData(message)
            .setConstraints(constraints)
            .build()

        if (counter != 5)
        {
            counter ++
            CoroutineScope(Dispatchers.IO).launch {

                delay(10000)

                CoroutineScope(Dispatchers.Main).launch {

                    WorkManager.getInstance(applicationContext).enqueue(oneTimeWorkRequest)
                }
            }
        } else {
            counter = 0
        }
        // -----------------

        return Result.success(outputData)
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