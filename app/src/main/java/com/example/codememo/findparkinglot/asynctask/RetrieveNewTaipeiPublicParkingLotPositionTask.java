package com.example.codememo.findparkinglot.asynctask;

import android.os.AsyncTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.example.codememo.findparkinglot.po.NewTaipeiPublicParkingLotPositionPO;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RetrieveNewTaipeiPublicParkingLotPositionTask extends AsyncTask<String, Integer, List<NewTaipeiPublicParkingLotPositionPO>> {

    @Override
    protected List<NewTaipeiPublicParkingLotPositionPO> doInBackground(String... params) {
        String url = params[0];
        String jsonInStr;
        Gson gson = new GsonBuilder().create();
        try {
            jsonInStr = getRemoteDate(url);
        }catch (Exception ex){
            return null;
        }
        JsonObject inputObject = new Gson().fromJson(jsonInStr, JsonObject.class);
        JsonObject resultObject = inputObject.getAsJsonObject("result");
        JsonArray recordArray = resultObject.getAsJsonArray("records");
        NewTaipeiPublicParkingLotPositionPO[] aryNewTaipeiPublicParkingLotPositionPO = gson.fromJson(recordArray,NewTaipeiPublicParkingLotPositionPO[].class);
        List<NewTaipeiPublicParkingLotPositionPO> listNewTaipeiPublicParkingLotPositionPO = new ArrayList<NewTaipeiPublicParkingLotPositionPO>();
        for(int i = 0;i<aryNewTaipeiPublicParkingLotPositionPO.length;i++)
        {
            listNewTaipeiPublicParkingLotPositionPO.add(aryNewTaipeiPublicParkingLotPositionPO[i]);
        }
        return listNewTaipeiPublicParkingLotPositionPO;
    }

    private String getRemoteDate(String pUrl) {
        StringBuilder jsonInStr = new StringBuilder();
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(pUrl).openConnection();
        } catch (IOException e) {
            //e.printStackTrace();
        }
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setUseCaches(false); // do not use a cached copy
        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            //e.printStackTrace();
        }
        connection.setRequestProperty("charset", "UTF-8");
        int responseCode = 0;
        try {
            responseCode = connection.getResponseCode();
        } catch (IOException e) {
            //e.printStackTrace();
        }
        if (responseCode == 200) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } catch (IOException e) {
                //e.printStackTrace();
            }
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    jsonInStr.append(line);
                }
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }else{

        }
        connection.disconnect();
        return jsonInStr.toString();
    }
}
