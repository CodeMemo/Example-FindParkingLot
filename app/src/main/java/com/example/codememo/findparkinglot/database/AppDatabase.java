package com.example.codememo.findparkinglot.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.codememo.findparkinglot.dao.NewTaipeiPublicParkingLotPositionDAO;
import com.example.codememo.findparkinglot.po.NewTaipeiPublicParkingLotPositionPO;

@Database(entities = {NewTaipeiPublicParkingLotPositionPO.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NewTaipeiPublicParkingLotPositionDAO getTaipeiPublicParkingLotPositionDAO();
}
