package com.example.alarmmanagerexample

interface AlarmScheduler {
    fun schedule(item:AlarmItem)
    fun cancel(item:AlarmItem)
}