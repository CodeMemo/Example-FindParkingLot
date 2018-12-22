package com.example.codememo.findparkinglot.asynctask;

import android.os.AsyncTask;

import com.example.codememo.findparkinglot.other.Common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class GetRoutePlanTask extends AsyncTask<String,Integer,String> {

    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        String jsonInStr;
        try {
            jsonInStr = getRemoteDate(url);
        }catch (Exception ex){
            return null;
        }
        return jsonInStr;
    }

    @Override
    public void onPreExecute()
    {
        super.onPreExecute();
        Common.routePlanStatus = "loading";
    }

    @Override
    public void onPostExecute(String result)
    {
        super.onPreExecute();
        Common.routePlanJsonStr = result;
        Common.routePlanStatus = "complete";
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
