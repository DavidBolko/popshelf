package com.example.popshelf.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.popshelf.domain.NetworkStatusProvider


/**
 * Implementacia rozhrania, ktorý sleduje stav siete, oddeluje závislosti, a umožnuje vyhnuť sa injectovaniu
 * kontextu priamo do repozitárov.
 * @param Context - Kontext aplikácie
 */
class NetworkStatusProviderImpl(private val context: Context) : NetworkStatusProvider {
    override fun isOnline(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}