package com.example.popshelf.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import com.example.popshelf.domain.NetworkStatusProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Implementation of [NetworkStatusProvider] using Android's [ConnectivityManager].
 *
 * This class check if network connectivity changes and exposes the change via a [StateFlow].
 * and it provides a synchronous method [isOnline] to check current status.
 *
 * @param context Application context.
 */
class NetworkMonitor(context: Context): NetworkStatusProvider {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    override fun isOnline(): Boolean = _isConnected.value

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _isConnected.value = true
        }

        override fun onLost(network: Network) {
            _isConnected.value = false
        }
    }

    init {
        try {
            connectivityManager.registerDefaultNetworkCallback(callback)
        } catch (e: Exception) {
            _isConnected.value = false
        }
    }

}
