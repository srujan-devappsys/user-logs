package com.example.myapplication.internal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
public class NetworkChangeReceiver extends BroadcastReceiver{
        private final NetworkCallback callback;
        public NetworkChangeReceiver(NetworkCallback callback)
        {
            this.callback =callback;
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Network network = cm.getActiveNetwork();
            NetworkCapabilities capabilities =cm.getNetworkCapabilities(network);

            Boolean isAvailable = capabilities !=null &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            callback.onNetworkChange(isAvailable);

        }
        public interface NetworkCallback {
            void onNetworkChange(boolean isAvailable);
        }


}
