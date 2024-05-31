package com.example.internetcheckthread

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var network:TextView
    private val handler = Handler(Looper.getMainLooper())
    private var status:String? = null
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            Log.d(TAG,"Internet Available")
            status = "Internet Available"
            updateNetworkStatus(status)
        }

        // Network capabilities have changed for the network
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
            status = "Internet Available"
            updateNetworkStatus(status)
        }

        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
            Log.d(TAG,"Internet Lost")
            status = "Internet Lost"
            updateNetworkStatus(status)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        network = findViewById(R.id.network)

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val connectivityManager = getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)



    }

    private fun updateNetworkStatus(status:String?){
        handler.post(object : Runnable {
            override fun run() {
                network.text = status
            }

        })
    }
}