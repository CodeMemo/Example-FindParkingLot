package com.example.codememo.findparkinglot.other;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.codememo.findparkinglot.database.AppDatabase;
import com.example.codememo.findparkinglot.po.NewTaipeiPublicParkingLotPositionPO;
import com.example.codememo.findparkinglot.presenter.MainActivityPresenter;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Common {


    public static AppDatabase appdb;
    public static List<NewTaipeiPublicParkingLotPositionPO> listNewTaipeiPublicParkingLotPositionPO;
    public static NewTaipeiPublicParkingLotPositionPO[] aryNewTaipeiPublicParkingLotPositionPO;
    public static NewTaipeiPublicParkingLotPositionPO usingNewTaipeiPublicParkingLotPositionPO;
    public static MainActivityPresenter mainPresenter;
    public static boolean isInRefresh = false;
    public static LatLng parkLocationLatLng;
    public static LatLng myLocationLatLng;
    public static String routePlanJsonStr;
    public static String routePlanStatus = "idle";
    public static String googleDirectionAPIKey = "";

    public static boolean networkConnected(Activity activity) {
        if(activity != null) {
            ConnectivityManager conManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }else{
            return false;
        }
    }

    public static void showToast(Context context, int messageResId) {
        if(context != null) {
            Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showToast(Context context, String message) {
        if(context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
