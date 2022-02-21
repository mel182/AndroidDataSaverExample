package com.example.datasaverexampleapp.work_manager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.work.*
import com.example.datasaverexampleapp.R
import com.example.datasaverexampleapp.databinding.ActivityWorkManagerBinding
import com.example.datasaverexampleapp.type_alias.Layout
import java.util.concurrent.TimeUnit

class WorkManagerActivity : AppCompatActivity() {

    private var binding: ActivityWorkManagerBinding? = null

    companion object {
        val TASK_KEY = "task"
        val DESCRIPTION_KEY = "description"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_manager)

        // Create input data for processing
        val inputData = Data.Builder()
            .putString(TASK_KEY,"Work Manager")
            .putString(DESCRIPTION_KEY,"Work is finished")
            .build()

        // Create constraints which contain the conditions on
        // which the task must be executed
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Create one time request
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setInputData(inputData)
            .setConstraints(constraints)
            .build()

        // Create work manager instance
        val workManager = WorkManager.getInstance(applicationContext)

        binding = DataBindingUtil.setContentView<ActivityWorkManagerBinding>(
            this, Layout.activity_work_manager
        ).apply {

            oneTimeRequestButton.setOnClickListener {
                WorkManager.getInstance(applicationContext).enqueue(oneTimeWorkRequest)
            }

            workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.id).observe(this@WorkManagerActivity) {
                handleResult(it)
            }

            val PERIODIC_TASK_TAG = "periodic"

            // Create one time request
            val periodicWorkRequest = PeriodicWorkRequest.Builder(MyWorker::class.java, 20, TimeUnit.SECONDS)
                .addTag(PERIODIC_TASK_TAG)
                .setInputData(inputData)
//            .setConstraints(constraints)
                // Setting a backoff on case the work needs to retry
//            .setBackoffCriteria(BackoffPolicy.LINEAR, PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                .build()

            // Periodic work request
            periodicWorkRequestButton.setOnClickListener {
                WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork("periodicUniqueName",
                    ExistingPeriodicWorkPolicy.KEEP, // Existing Periodic Work Policy
                    periodicWorkRequest) // work request
            }

            workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id).observe(this@WorkManagerActivity) {
                handlePeriodicResult(it)
            }
        }
    }

    private fun handleResult(workInfo:WorkInfo)
    {
        binding?.apply {
            oneTimeStatus.text = ""

            if (workInfo.state.isFinished)
            {
                val outputData = workInfo.outputData // Retrieve result output data
                val task = outputData.getString(TASK_KEY)?:""
                val description = outputData.getString(DESCRIPTION_KEY)?:""

                if (task != "" && description != "")
                {
                    oneTimeStatus.text = StringBuilder()
                        .append("Status: ${workInfo.state.name}\n\n")
                        .append("Output: task $task\n\n")
                        .append("description: $description")
                        .toString()
                    return
                }
            }

            oneTimeStatus.text = StringBuilder()
                .append("${workInfo.state.name}\n")
                .toString()
        }

    }

    var taskCount:Int = 0

    private fun handlePeriodicResult(workInfo:WorkInfo)
    {
        binding?.apply {
            taskCount++
            periodicTaskStatus.text = ""

            if (workInfo.state.isFinished)
            {
                val outputData = workInfo.outputData // Retrieve result output data
                val task = outputData.getString(TASK_KEY)?:""
                val description = outputData.getString(DESCRIPTION_KEY)?:""

                if (task != "" && description != "")
                {
                    periodicTaskStatus.text = StringBuilder()
                        .append("Task: ${taskCount}\n\n")
                        .append("Status: ${workInfo.state.name}\n\n")
                        .append("Output: task $task\n\n")
                        .append("description: $description")
                        .toString()
                    return
                }
            }

            periodicTaskStatus.text = StringBuilder()
                .append("Task: ${taskCount}\n\n")
                .append("${workInfo.state.name}\n")
                .toString()
        }
    }
}