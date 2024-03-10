package com.jetpackcompose.realmdb

import android.app.Application
import com.jetpackcompose.realmdb.models.Address
import com.jetpackcompose.realmdb.models.Course
import com.jetpackcompose.realmdb.models.Student
import com.jetpackcompose.realmdb.models.Teacher
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class MyApp: Application() {

    companion object {
        var realm: Realm? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        realm = Realm.open(
            configuration = RealmConfiguration.create(
                schema = setOf(
                    Address::class,
                    Teacher::class,
                    Course::class,
                    Student::class
                )
            )
        )
    }

}