package com.jetpackcompose.realmdb

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetpackcompose.realmdb.models.Address
import com.jetpackcompose.realmdb.models.Course
import com.jetpackcompose.realmdb.models.Student
import com.jetpackcompose.realmdb.models.Teacher
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    // For more info about Realm query, see: https://www.mongodb.com/docs/realm/realm-query-language/
    // example query (get all course that John JR 1 is enrolled in:
    //     "enrolledStudents.name == $0",
    //      "John JR 1"
    //
    // example query (get all course were the student count is at least 2:
    // 'enrolledStudents.@count >= 2'
    //
    // example query (get all course were the teacher full name contains 'John')
    // 'teacher.address.fullName CONTAINS $0',
    // 'John'
    //
    // USAGE:
    // ?.query<Course>(
    //   "teacher.address.fullName CONTAINS $0",
    //   "John Doe 2"
    // )

    private val realm = MyApp.realm
    val courses = realm
        ?.query<Course>()
        ?.asFlow()
        ?.map { results ->
            results.list.toList()
        }?.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    var courseDetails: Course? by mutableStateOf(null)
    private set

    init {
        if (isDbEmpty())
            createSampleEntries()
    }

    fun showCourseDetails(course: Course) {
        courseDetails = course
    }

    fun hideCourseDetails() {
        courseDetails = null
    }

    fun deleteCourse() {
        Log.i("TAG51","delete course!")
        viewModelScope.launch {
            realm?.write {
                val course = courseDetails ?: return@write
                val latestCourse = findLatest(course) ?: return@write
                delete(latestCourse)
                courseDetails = null
            }
        }
    }

    private fun createSampleEntries() {
        viewModelScope.launch {
            realm?.write {
                val address1 = Address().apply {
                    fullName = "John Doe"
                    street = "John Street"
                    houseNumber = 24
                    zip = 12345
                    city = "John city"
                }
                val address2 = Address().apply {
                    fullName = "John Doe 2"
                    street = "John Street 2"
                    houseNumber = 21
                    zip = 12345
                    city = "John city 2"
                }

                val course1 = Course().apply {
                    name = "Kotlin Programming Madde Easy"
                }
                val course2 = Course().apply {
                    name = "Android basics"
                }
                val course3 = Course().apply {
                    name = "Asynchronous Programming With Coroutines"
                }

                val teacher1 = Teacher().apply {
                    address = address1
                    courses = realmListOf(course1, course2)
                }
                val teacher2 = Teacher().apply {
                    address = address2
                    courses = realmListOf(course3)
                }

                course1.teacher = teacher1
                course2.teacher = teacher1
                course3.teacher = teacher2

                address1.teacher = teacher1
                address2.teacher = teacher2

                val student1 = Student().apply {
                    name = "John JR 1"
                }
                val student2 = Student().apply {
                    name = "John JR 2"
                }


                course1.enrolledStudents.add(student1)
                course2.enrolledStudents.add(student2)
                course3.enrolledStudents.addAll(listOf(student1, student2))

                copyToRealm(teacher1, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(teacher2, updatePolicy = UpdatePolicy.ALL)

                copyToRealm(course1, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(course2, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(course3, updatePolicy = UpdatePolicy.ALL)

                copyToRealm(student1, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(student2, updatePolicy = UpdatePolicy.ALL)
            }
        }
    }

    private fun isDbEmpty(): Boolean {
        return realm?.let {
            val result = it.query<Course>()
            result.count().find() == 0L
        }?: false
    }
}