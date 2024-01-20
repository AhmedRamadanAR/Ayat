package com.example.ayat

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class NetworkConnectivityObserver(context:Context):InternetObserver {
    private val connectivityManager= context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe(): Flow<InternetObserver.Status> {
        return callbackFlow {
            val callBack= object :ConnectivityManager.NetworkCallback(){
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
             launch {    send(InternetObserver.Status.Available)}
                }

                override fun onLost(network: Network) {
                    super.onLost(network)

                    launch { send(InternetObserver.Status.Not_Available) }
                }
            }
           connectivityManager.registerDefaultNetworkCallback(callBack)

            awaitClose {
                connectivityManager.unregisterNetworkCallback(callBack)
            }
        }.distinctUntilChanged()
    }
}