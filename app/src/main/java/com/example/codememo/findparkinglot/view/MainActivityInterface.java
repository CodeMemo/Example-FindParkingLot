package com.example.codememo.findparkinglot.view;

public interface MainActivityInterface {
    void setContentView();
    void setDefaultFragment();
    void setLocationList();
    void refreshRecycleView();
    void setSwipeRefreshLayoutStatus(boolean in);
    void showDetailOnMap();
}
