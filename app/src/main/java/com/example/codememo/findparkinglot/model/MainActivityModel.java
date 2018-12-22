package com.example.codememo.findparkinglot.model;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.example.codememo.findparkinglot.asynctask.GetRoutePlanTask;
import com.example.codememo.findparkinglot.asynctask.RetrieveNewTaipeiPublicParkingLotPositionTask;
import com.example.codememo.findparkinglot.database.AppDatabase;
import com.example.codememo.findparkinglot.other.Common;
import com.example.codememo.findparkinglot.po.NewTaipeiPublicParkingLotPositionPO;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivityModel{

    public MainActivityModel() {
        Common.aryNewTaipeiPublicParkingLotPositionPO = new NewTaipeiPublicParkingLotPositionPO[0];
        Common.listNewTaipeiPublicParkingLotPositionPO = new ArrayList<>();
    }

    public void getData(Context pContext) {
        readDataFromInternet();
        setRoom(pContext);
    }

    public void setRecycleViewData() {
        if(!Common.isInRefresh) {
            Common.isInRefresh = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<NewTaipeiPublicParkingLotPositionPO> tempList = Common.appdb.getTaipeiPublicParkingLotPositionDAO().getAll();
                    Common.listNewTaipeiPublicParkingLotPositionPO.clear();
                    for (int i = 0; i < tempList.size(); i++) {
                        Common.listNewTaipeiPublicParkingLotPositionPO.add(tempList.get(i));
                    }
                    Common.isInRefresh = false;
                }
            }).start();
        }
    }

    private void readDataFromInternet(){
        RetrieveNewTaipeiPublicParkingLotPositionTask retrieveNewTaipeiPublicParkingLotPositionTask = new RetrieveNewTaipeiPublicParkingLotPositionTask();
        try {
            Common.listNewTaipeiPublicParkingLotPositionPO = retrieveNewTaipeiPublicParkingLotPositionTask.execute("http://data.ntpc.gov.tw/api/v1/rest/datastore/382000000A-000225-002").get();
        } catch (InterruptedException e) {
            //e.printStackTrace();
        } catch (ExecutionException e) {
            //e.printStackTrace();
        }
    }

    private void setRoom(final Context pContext) {
        Common.appdb = Room.databaseBuilder(pContext, AppDatabase.class,"default.db").build();
        if( Common.listNewTaipeiPublicParkingLotPositionPO.size() > 0) {
            ArrayListToArray();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Common.appdb.getTaipeiPublicParkingLotPositionDAO().insertAll(Common.aryNewTaipeiPublicParkingLotPositionPO);
                }
            }).start();
        }
    }

    public void ArrayListToArray(){
        Common.aryNewTaipeiPublicParkingLotPositionPO = new NewTaipeiPublicParkingLotPositionPO[Common.listNewTaipeiPublicParkingLotPositionPO.size()];
        Common.listNewTaipeiPublicParkingLotPositionPO.toArray(Common.aryNewTaipeiPublicParkingLotPositionPO);
    }

    public void ArrayToArrayList(){
        Common.listNewTaipeiPublicParkingLotPositionPO.clear();
        for(int i =0;i<Common.aryNewTaipeiPublicParkingLotPositionPO.length;i++)
        {
            Common.listNewTaipeiPublicParkingLotPositionPO.add(Common.aryNewTaipeiPublicParkingLotPositionPO[i]);
        }
    }

    public void setRoutePlanData(){
        GetRoutePlanTask getRoutePlanTask = new GetRoutePlanTask();
        String urlForGoogleMapsDirectionsAPI = getUrlForGoogleMapsDirectionsAPI(Common.myLocationLatLng,Common.parkLocationLatLng);
        if((!urlForGoogleMapsDirectionsAPI.equals("")) && Common.routePlanStatus.equals("idle")) {
            getRoutePlanTask.execute(urlForGoogleMapsDirectionsAPI);
        }
    }

    private String getUrlForGoogleMapsDirectionsAPI(LatLng myLocationLatLng, LatLng parkLocationLatLng) {
        String url = "";
        if(myLocationLatLng != null && parkLocationLatLng != null){
            String originStr = myLocationLatLng.latitude + "," + myLocationLatLng.longitude;
            String destinationStr = parkLocationLatLng.latitude + "," + parkLocationLatLng.longitude;
            String outputType = "json";
            url = "https://maps.googleapis.com/maps/api/directions/"+ outputType +"?origin="+ originStr + "&destination="+ destinationStr + "&key=" + Common.googleDirectionAPIKey;
        }
        return url;
    }
}
