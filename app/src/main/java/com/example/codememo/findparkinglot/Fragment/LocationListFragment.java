package com.example.codememo.findparkinglot.Fragment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.codememo.findparkinglot.R;
import com.example.codememo.findparkinglot.other.Common;
import com.example.codememo.findparkinglot.po.NewTaipeiPublicParkingLotPositionPO;
import java.util.ArrayList;
import java.util.List;

public class LocationListFragment extends Fragment {

    public SwipeRefreshLayout locationListswipeRefreshLayout;
    private RecyclerView locationListRecyclerView;
    private List<NewTaipeiPublicParkingLotPositionPO> locationList = new ArrayList<NewTaipeiPublicParkingLotPositionPO>();
    private LinearLayoutManager linearLayoutManager;
    public LocationListAdapter locationListAdapter;

    public static LocationListFragment newInstance(Bundle args) {
        LocationListFragment f = new LocationListFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,@Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_locationlist, container, false);
        if (savedInstanceState != null) {
            if (savedInstanceState.getSerializable("LocationList") != null) {
                locationList.clear();
                locationList.addAll((ArrayList<NewTaipeiPublicParkingLotPositionPO>) savedInstanceState.getSerializable("LocationList"));
                savedInstanceState.remove("LocationList");
            }
        }else {
            if (getArguments().getSerializable("LocationList") != null) {
                locationList.clear();
                locationList.addAll((ArrayList<NewTaipeiPublicParkingLotPositionPO>) getArguments().getSerializable("LocationList"));
                getArguments().remove("LocationList");
            }
        }

        findViews(view);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        locationListAdapter = new LocationListAdapter(getActivity());
        locationListRecyclerView.setLayoutManager(linearLayoutManager);
        locationListRecyclerView.setAdapter(locationListAdapter);

        locationListswipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Common.mainPresenter.refreshRecycleViewData();
            }
        });

        return view;
    }

    private void findViews(View view) {
        locationListswipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.locationListSwipeRefreshLayout);
        locationListRecyclerView = (RecyclerView)view.findViewById(R.id.locationListRecycleView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("LocationList",(ArrayList<NewTaipeiPublicParkingLotPositionPO>)locationList);
        super.onSaveInstanceState(outState);
    }

    public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {
        private List<NewTaipeiPublicParkingLotPositionPO> rvLocationList;
        private Context context;

        public LocationListAdapter(Context context) {
            this.context = context;
            if(locationList != null && locationList.size() > 0){
                rvLocationList = new ArrayList<NewTaipeiPublicParkingLotPositionPO>();
                rvLocationList.addAll(locationList);
            }else{
                rvLocationList = new ArrayList<NewTaipeiPublicParkingLotPositionPO>();
            }

        }

        public List<NewTaipeiPublicParkingLotPositionPO> getLocationList() {
            return rvLocationList;
        }

        public void DataChage(List<NewTaipeiPublicParkingLotPositionPO> receivedLocationList){
            rvLocationList.clear();
            rvLocationList.addAll(receivedLocationList);
            notifyDataSetChanged();
        }

        public void DataClear(){
            rvLocationList.clear();
            notifyDataSetChanged();
        }

        protected class ViewHolder extends RecyclerView.ViewHolder {
            TextView locationVhName;
            TextView locationVhAddress;
            TextView locationVhServiceTime;
            TextView locationVhArea;
            public ViewHolder(View itemView) {
                super(itemView);
                locationVhName = (TextView)itemView.findViewById(R.id.textView_vhName);
                locationVhAddress = (TextView)itemView.findViewById(R.id.textView_vhAddress);
                locationVhServiceTime = (TextView)itemView.findViewById(R.id.textView_vhServiceTime);
                locationVhArea = (TextView)itemView.findViewById(R.id.textView_vhArea);
            }
        }

        @Override
        public LocationListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.fragment_locationlist_rvitem, parent, false);
            LocationListAdapter.ViewHolder viewHolder = new LocationListAdapter.ViewHolder(itemView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(LocationListAdapter.ViewHolder viewHolder, int position) {
            if((position + 1) <= rvLocationList.size()) {
                final int iposition = position;
                final NewTaipeiPublicParkingLotPositionPO tempNewTaipeiPublicParkingLotPositionPO = rvLocationList.get(position);
                viewHolder.locationVhName.setText(tempNewTaipeiPublicParkingLotPositionPO.getNAME());
                viewHolder.locationVhArea.setText(tempNewTaipeiPublicParkingLotPositionPO.getAREA());
                viewHolder.locationVhAddress.setText(tempNewTaipeiPublicParkingLotPositionPO.getADDRESS());
                viewHolder.locationVhServiceTime.setText(tempNewTaipeiPublicParkingLotPositionPO.getSERVICETIME());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Common.usingNewTaipeiPublicParkingLotPositionPO = tempNewTaipeiPublicParkingLotPositionPO;
                        Common.mainPresenter.showDetailOnMap();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return rvLocationList.size();
        }
    }
}
