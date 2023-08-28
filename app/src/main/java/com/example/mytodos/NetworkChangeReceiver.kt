package com.example.mytodos

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.widget.Toast

class NetworkChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: Network? = cm.activeNetwork
            val networkCapabilities: NetworkCapabilities? = cm.getNetworkCapabilities(activeNetwork)

            val isConnected = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

            val status = if (isConnected) "Connected" else "Disconnected"
            Toast.makeText(context, "Network Status: $status", Toast.LENGTH_SHORT).show()
        }
    }

}