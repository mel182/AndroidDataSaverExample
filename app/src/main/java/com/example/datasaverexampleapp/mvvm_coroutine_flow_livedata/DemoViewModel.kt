@file:Suppress("UNUSED_PARAMETER")

package com.example.datasaverexampleapp.mvvm_coroutine_flow_livedata

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.datasaverexampleapp.compose.bottom_navigation.BottomNavItem
import com.example.datasaverexampleapp.compose.bottom_navigation.repository.BottomNavRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DemoViewModel:ViewModel() {

    private var demoResponse:MutableLiveData<DemoModel> = MutableLiveData()
    private var demoAsyncResponse:MutableLiveData<DemoModelSummary> = MutableLiveData()

    private var composeDemoResponse:MutableLiveData<List<BottomNavItem>> = MutableLiveData()
    val composeDemoAsyncResponse:LiveData<List<BottomNavItem>>
    get() = composeDemoResponse

    init {
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
            val items2 = arrayListOf(
                BottomNavItem(name = "Home", route = "home", icon = Icons.Default.Home),
                BottomNavItem(name = "Chat", route = "chat", icon = Icons.Default.Notifications, badgeCount = 2),
                BottomNavItem(name = "Settings", route = "settings", icon = Icons.Default.Settings)
            )
            BottomNavRepo.tabItems.apply {
                clear()
                addAll(items2)
            }
            composeDemoResponse.value = items2
            delay(5000)
            val items3 = arrayListOf(
                BottomNavItem(name = "Home", route = "home", icon = Icons.Default.Home),
                BottomNavItem(name = "Chat", route = "chat", icon = Icons.Default.Notifications, badgeCount = 3),
                BottomNavItem(name = "Settings", route = "settings", icon = Icons.Default.Settings)
            )
            BottomNavRepo.tabItems.apply {
                clear()
                addAll(items3)
            }
            composeDemoResponse.value = items3
        }
    }

    fun getDemo(amount:Int): LiveData<DemoModel>
    {
        DemoRepository.getDemo(amount = amount,object : RepositoryCallback<DemoModel>{

            override fun onResponse(response: DemoModel) {
                demoResponse.value = response
            }

            override fun onFailed(error: DemoModel) {
                demoResponse.value = error
            }
        })

        return demoResponse
    }

    fun getAsyncDemo(amount:Int): LiveData<DemoModelSummary>
    {
        CoroutineScope(Dispatchers.Main).launch{
            DemoRepository.getDemoAsync().let {
                demoAsyncResponse.value = it.first()
            }
        }

        return demoAsyncResponse
    }

    suspend fun getAsyncDemo(): LiveData<DemoModelSummary> = DemoRepository.getDemoAsync().asLiveData()
}