package com.example.codememo.findparkinglot.presenter;

import android.content.Context;
import com.example.codememo.findparkinglot.model.MainActivityModel;
import com.example.codememo.findparkinglot.other.Common;
import com.example.codememo.findparkinglot.view.MainActivityInterface;

public class MainActivityPresenter {
    private MainActivityInterface mainView;
    private MainActivityModel mainModel;
    private Context mContext;

    public MainActivityPresenter(Context pContext,MainActivityInterface mainView, MainActivityModel mainModel) {
        this.mContext = pContext;
        this.mainView = mainView;
        this.mainModel = mainModel;
    }

    public void onCreate(){
        mainModel.getData(mContext);
        mainModel.setRecycleViewData();
        mainView.setContentView();
        mainView.setDefaultFragment();
    }

    public void createLocationList() {
        mainView.setLocationList();
    }

    public void refreshRecycleViewData() {
        mainModel.setRecycleViewData();
        while (Common.isInRefresh){
            refreshRecycleView();
            setSwipeRefreshLayoutStatus(false);
        }
    }

    public void refreshRecycleView() {
        mainView.refreshRecycleView();
    }

    public void setSwipeRefreshLayoutStatus(boolean in) {
        mainView.setSwipeRefreshLayoutStatus(in);
    }

    public void showDetailOnMap() {
        mainView.showDetailOnMap();
    }

    public void routePlan(){
        mainModel.setRoutePlanData();
        mainView.drawRoutePlanOnMap();
    }


}
