package com.example.codememo.findparkinglot;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.codememo.findparkinglot.Fragment.LocationListFragment;
import com.example.codememo.findparkinglot.Fragment.MapFragment;
import com.example.codememo.findparkinglot.model.MainActivityModel;
import com.example.codememo.findparkinglot.other.Common;
import com.example.codememo.findparkinglot.po.NewTaipeiPublicParkingLotPositionPO;
import com.example.codememo.findparkinglot.presenter.MainActivityPresenter;
import com.example.codememo.findparkinglot.view.MainActivityInterface;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements MainActivityInterface {
    private static final int REQ_PERMISSIONS = 0;
    private FragmentManager fragmentManager;
    private LocationListFragment locationListFragment;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Common.mainPresenter = new MainActivityPresenter(getApplicationContext(),this,new MainActivityModel());
        Common.mainPresenter.onCreate();
        if(savedInstanceState != null)
        {
            locationListFragment = (LocationListFragment)(fragmentManager.getFragment(savedInstanceState, "LocationList"));
        }else
        {
            Common.mainPresenter.createLocationList();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        askPermissions();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        fragmentManager.putFragment(outState, "LocationList", locationListFragment);
        super.onSaveInstanceState(outState);
    }

    private void askPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        Set<String> permissionsRequest = new HashSet<>();
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsRequest.add(permission);
            }
        }

        if (!permissionsRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsRequest.toArray(new String[permissionsRequest.size()]),
                    REQ_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_PERMISSIONS:
                String text = "";
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        text += permissions[i] + "\n";
                    }
                }
                if (!text.isEmpty()) {
                    text += "Not Granted";
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void setDefaultFragment() {
        fragmentManager = getSupportFragmentManager();

        Bundle bundleForLocationListFragment = new Bundle();
        bundleForLocationListFragment.putSerializable("LocationList",(ArrayList<NewTaipeiPublicParkingLotPositionPO>)Common.listNewTaipeiPublicParkingLotPositionPO);
        locationListFragment = LocationListFragment.newInstance(bundleForLocationListFragment);

        Bundle bundleForMapFragment = new Bundle();
        mapFragment = MapFragment.newInstance(bundleForMapFragment);

    }

    @Override
    public void setLocationList() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_main,locationListFragment,"LocationList");
        fragmentTransaction.commit();
    }

    @Override
    public void refreshRecycleView() {
        locationListFragment.locationListAdapter.DataChage(Common.listNewTaipeiPublicParkingLotPositionPO);
    }

    @Override
    public void setSwipeRefreshLayoutStatus(boolean in) {
        if(locationListFragment != null && locationListFragment.locationListswipeRefreshLayout != null)
        {
            locationListFragment.locationListswipeRefreshLayout.setRefreshing(in);
        }
    }

    @Override
    public void showDetailOnMap() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundleForMapFragment = new Bundle();
        bundleForMapFragment.putString("Name",Common.usingNewTaipeiPublicParkingLotPositionPO.getNAME());
        bundleForMapFragment.putString("Area",Common.usingNewTaipeiPublicParkingLotPositionPO.getAREA());
        bundleForMapFragment.putString("Adress",Common.usingNewTaipeiPublicParkingLotPositionPO.getADDRESS());
        bundleForMapFragment.putString("ServiceTime",Common.usingNewTaipeiPublicParkingLotPositionPO.getSERVICETIME());
        bundleForMapFragment.putString("X",Common.usingNewTaipeiPublicParkingLotPositionPO.getTW97X());
        bundleForMapFragment.putString("Y",Common.usingNewTaipeiPublicParkingLotPositionPO.getTW97Y());
        mapFragment.setArguments(bundleForMapFragment);
        fragmentTransaction.replace(R.id.fragment_main,mapFragment,"Map");
        fragmentTransaction.addToBackStack("LocationList");
        fragmentTransaction.commit();
    }
}
