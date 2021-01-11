package com.example.datasaverexampleapp.background_task

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.example.datasaverexampleapp.R
import kotlinx.android.synthetic.main.activity_work_manager.*

class WorkManagerActivity : AppCompatActivity() {

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

        one_time_request_button.setOnClickListener {
            WorkManager.getInstance(applicationContext).enqueue(oneTimeWorkRequest)
        }

        workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.id).observe(this, {
            handleResult(it)
        })
    }

    private fun handleResult(workInfo:WorkInfo)
    {
        one_time_status.text = ""

        if (workInfo.state.isFinished)
        {
            val outputData = workInfo.outputData // Retrieve result output data
            val task = outputData.getString(TASK_KEY)?:""
            val description = outputData.getString(DESCRIPTION_KEY)?:""

            if (task != "" && description != "")
            {
                one_time_status.text = StringBuilder()
                    .append("Status: ${workInfo.state.name}\n\n")
                    .append("Output: task $task\n\n")
                    .append("description: $description")
                    .toString()
                return
            }
        }

        one_time_status.text = StringBuilder().append("${workInfo.state.name}\n").toString()
    }
}