package com.example.codememo.findparkinglot.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.codememo.findparkinglot.R;
import com.example.codememo.findparkinglot.other.Common;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private TextView tvName;
    private TextView tvArea;
    private TextView tvAdress;
    private TextView tvServiceTime;
    private FloatingActionButton fAbtRoutePlanning;
    private Marker parkLocationMarker;
    private Marker myLocationMarker;

    public static MapFragment newInstance(Bundle args) {
        MapFragment f = new MapFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,@Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        findViewsById(view);

        setData();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentByTag("mapFragment");
        if (mapFragment == null) {
            mapFragment = new SupportMapFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.map, mapFragment, "mapFragment");
            ft.commit();
            fm.executePendingTransactions();
        }
        mapFragment.getMapAsync(this);
    }

    private void setData() {
        tvName.setText(Common.usingNewTaipeiPublicParkingLotPositionPO.getNAME());
        tvArea.setText(Common.usingNewTaipeiPublicParkingLotPositionPO.getAREA());
        tvAdress.setText(Common.usingNewTaipeiPublicParkingLotPositionPO.getADDRESS());
        tvServiceTime.setText(Common.usingNewTaipeiPublicParkingLotPositionPO.getSERVICETIME());
        fAbtRoutePlanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Common.myLocationLatLng != null) {
                    Common.mainPresenter.routePlan();
                }else{
                    Common.showToast(view.getContext(),"Unknown location");
                }
            }
        });
        double x = Double.parseDouble(Common.usingNewTaipeiPublicParkingLotPositionPO.getTW97X());
        double y = Double.parseDouble(Common.usingNewTaipeiPublicParkingLotPositionPO.getTW97Y());
        double[] latlon =  Twd97ToWgs84(x,y);
        Common.parkLocationLatLng = new LatLng(latlon[0],latlon[1]);
        Common.myLocationLatLng = null;
    }

    private double[] Twd97ToWgs84(double x, double y) {

        double a = 6378137.0;
        double b = 6356752.314245;
        double lon0 = 121.0 * Math.PI / 180.0;
        double k0 = 0.9999;
        int dx = 250000;

        double dy = 0;
        double e = Math.pow((1- Math.pow(b,2)/Math.pow(a,2)), 0.5);

        x -= dx;
        y -= dy;

        // Calculate the Meridional Arc
        double M = y/k0;

        // Calculate Footprint Latitude
        double mu = M/(a*(1.0 - Math.pow(e, 2)/4.0 - 3*Math.pow(e, 4)/64.0 - 5*Math.pow(e, 6)/256.0));
        double e1 = (1.0 - Math.pow((1.0 - Math.pow(e, 2)), 0.5)) / (1.0 + Math.pow((1.0 - Math.pow(e, 2)), 0.5));

        double J1 = (3*e1/2.0 - 27*Math.pow(e1, 3)/32.0);
        double J2 = (21*Math.pow(e1, 2)/16.0 - 55*Math.pow(e1, 4)/32.0);
        double J3 = (151*Math.pow(e1, 3)/96.0);
        double J4 = (1097*Math.pow(e1, 4)/512.0);

        double fp = mu + J1*Math.sin(2*mu) + J2*Math.sin(4*mu) + J3*Math.sin(6*mu) + J4*Math.sin(8*mu);

        // Calculate Latitude and Longitude

        double e2 = Math.pow((e*a/b), 2);
        double C1 = Math.pow(e2*Math.cos(fp), 2);
        double T1 = Math.pow(Math.tan(fp), 2);
        double R1 = a*(1-Math.pow(e, 2))/Math.pow((1-Math.pow(e, 2)*Math.pow(Math.sin(fp), 2)), (3.0/2.0));
        double N1 = a/Math.pow((1-Math.pow(e, 2)*Math.pow(Math.sin(fp), 2)), 0.5);

        double D = x/(N1*k0);

        double Q1 = N1*Math.tan(fp)/R1;
        double Q2 = (Math.pow(D, 2)/2.0);
        double Q3 = (5 + 3*T1 + 10*C1 - 4*Math.pow(C1, 2) - 9*e2)*Math.pow(D, 4)/24.0;
        double Q4 = (61 + 90*T1 + 298*C1 + 45*Math.pow(T1, 2) - 3*Math.pow(C1, 2) - 252*e2)*Math.pow(D, 6)/720.0;
        double lat = fp - Q1*(Q2 - Q3 + Q4);

        double Q5 = D;
        double Q6 = (1 + 2*T1 + C1)*Math.pow(D, 3)/6.0;
        double Q7 = (5 - 2*C1 + 28*T1 - 3*Math.pow(C1, 2) + 8*e2 + 24*Math.pow(T1, 2))*Math.pow(D, 5)/120.0;
        double lon = lon0 + (Q5 - Q6 + Q7)/Math.cos(fp);

        return new double[]{Math.toDegrees(lat),Math.toDegrees(lon)};
    }

    private void findViewsById(View view) {
        tvName = view.findViewById(R.id.textView_mapName);
        tvArea = view.findViewById(R.id.textView_mapArea);
        tvAdress = view.findViewById(R.id.textView_mapAdress);
        tvServiceTime = view.findViewById(R.id.textView_mapServiceTime);
        fAbtRoutePlanning = view.findViewById(R.id.floatingActionButtonRoutePlanning);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setFusedLocation();
        setUpMap();
    }

    private void setFusedLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            Common.showToast(getActivity(),"ACCESS_FINE_LOCATION X");
        }

        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Common.myLocationLatLng = new LatLng(location.getLatitude(),location.getLongitude());
            }
        });
    }

    private void setUpMap() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            //googleMap.setMyLocationEnabled(true);
        } else {
            Common.showToast(getActivity(),"ACCESS_FINE_LOCATION X");
        }
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.setMyLocationEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        //googleMap.setOnMyLocationButtonClickListener(this);
        parkLocationMarker = googleMap.addMarker(new MarkerOptions()
                                                    .position(Common.parkLocationLatLng)
                                                );
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(Common.parkLocationLatLng)
                .zoom(18)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory
                .newCameraPosition(cameraPosition);
        googleMap.moveCamera(cameraUpdate);
    }

    public void drawRoutePlanOnMap() {
        if(Common.routePlanStatus.equals("loading") || Common.routePlanStatus.equals("complete"))
        {
            DrawRoutePlanOnMapTask drawRoutePlanOnMapTask = new DrawRoutePlanOnMapTask();
            drawRoutePlanOnMapTask.execute("");
        }
    }

    private class DrawRoutePlanOnMapTask extends AsyncTask<String,Integer,List<LatLng>>{

        @Override
        protected List<LatLng> doInBackground(String... params) {
            while(Common.routePlanStatus.equals("loading"))
            {
                if(Common.routePlanStatus.equals("complete"))
                {
                    break;
                }
            }
            List<LatLng> tempPointLatLng = new ArrayList<LatLng>();

            String routePlanJsonStr = Common.routePlanJsonStr;
            JsonObject inputObject = new Gson().fromJson(routePlanJsonStr, JsonObject.class);
            String status = inputObject.get("status").getAsString();
            if(status.equals("OK")){
                JsonArray routesArray = inputObject.getAsJsonArray("routes");
                JsonObject routesObject= routesArray.get(0).getAsJsonObject();
                JsonObject overviewPolylineObject = routesObject.getAsJsonObject("overview_polyline");
                String pointsStr = overviewPolylineObject.get("points").getAsString();
                decodeOverviewPolyLinePonts(pointsStr,tempPointLatLng);
            }
            return tempPointLatLng;
        }

        private void decodeOverviewPolyLinePonts(String encoded, List<LatLng> poly) {
            poly.clear();
            if (encoded != null && !encoded.isEmpty() && encoded.trim().length() > 0) {
                int index = 0, len = encoded.length();
                int lat = 0, lng = 0;

                while (index < len) {
                    int b, shift = 0, result = 0;
                    do {
                        b = encoded.charAt(index++) - 63;
                        result |= (b & 0x1f) << shift;
                        shift += 5;
                    } while (b >= 0x20);
                    int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                    lat += dlat;

                    shift = 0;
                    result = 0;
                    do {
                        b = encoded.charAt(index++) - 63;
                        result |= (b & 0x1f) << shift;
                        shift += 5;
                    } while (b >= 0x20);
                    int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                    lng += dlng;

                    LatLng p = new LatLng((((double) lat / 1E5)),
                            (((double) lng / 1E5)));
                    poly.add(p);
                }
            }
        }

        @Override
        public void onPostExecute(List<LatLng> pointLatLng)
        {
            super.onPreExecute();
            if (pointLatLng != null && pointLatLng.size() > 1) {
                googleMap.clear();
                PolylineOptions lineOptions = new PolylineOptions();
                lineOptions.addAll(pointLatLng);
                lineOptions.width(5);
                lineOptions.color(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                googleMap.addPolyline(lineOptions);
                myLocationMarker = googleMap.addMarker(new MarkerOptions()
                        .position(Common.myLocationLatLng)
                );

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(parkLocationMarker.getPosition());
                builder.include(myLocationMarker.getPosition());
                LatLngBounds bounds = builder.build();

                int padding = 50; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                googleMap.animateCamera(cu);
            }
            parkLocationMarker = googleMap.addMarker(new MarkerOptions()
                    .position(Common.parkLocationLatLng)
            );
            Common.routePlanJsonStr = "";
            Common.routePlanStatus = "idle";
        }
    }

//    @Override
//    public boolean onMyLocationButtonClick() {
//        if(mLastLocation != null)
//        {
//            myLocationLatLng = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
//            myLocationMarker = googleMap.addMarker(new MarkerOptions()
//                    .position(myLocationLatLng)
//            );
//
//            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//            builder.include(parkLocationMarker.getPosition());
//            builder.include(myLocationMarker.getPosition());
//            LatLngBounds bounds = builder.build();
//
//            int padding = 50; // offset from edges of the map in pixels
//            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
//
//            googleMap.animateCamera(cu);
//        }
//        return false;
//    }
}
