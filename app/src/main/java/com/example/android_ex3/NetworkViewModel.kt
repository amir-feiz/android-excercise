// NetworkViewModel.kt
package com.example.networkmonitor

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NetworkViewModel(application: Application) : AndroidViewModel(application) {

    private val _networkStatus = MutableLiveData<String>()
    val networkStatus: LiveData<String> get() = _networkStatus

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val connectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetworkInfo
            val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
            _networkStatus.postValue(if (isConnected) "Connected to Internet" else "Disconnected from Internet")
        }
    }

    init {
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        application.registerReceiver(networkReceiver, filter)
    }

    override fun onCleared() {
        super.onCleared()
        getApplication<Application>().unregisterReceiver(networkReceiver)
    }
}
