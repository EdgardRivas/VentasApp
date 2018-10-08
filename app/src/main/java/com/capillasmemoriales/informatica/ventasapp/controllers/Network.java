package com.capillasmemoriales.informatica.ventasapp.controllers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Network {

    private static ConnectivityManager manager;
    private static final String IP = "https://sisadmov.laauxiliadora.com";
    public static final String VALIDATE = IP + "/validate.php";
    public static final String LOGIN = IP + "/loginVentas.php";
    public static final String GET_CONTACTS = IP + "/getContacts.php";
    public static final String SEARCH_CONTACT = IP + "/searchContact.php";
    public static final String ADD_CONTACT = IP + "/addContact.php";
    public static final String EDIT_CONTACT = IP + "/editContact.php";
    public static final String DELETE_CONTACT = IP + "/deleteContact.php";
    public static final int DURATION = 1500;

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
/*
    public static boolean isConnectedWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static boolean isConnectedMobile(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }
*/
}
