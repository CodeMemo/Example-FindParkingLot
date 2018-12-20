package com.example.codememo.findparkinglot.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.example.codememo.findparkinglot.po.NewTaipeiPublicParkingLotPositionPO;
import java.util.List;

@Dao
public interface NewTaipeiPublicParkingLotPositionDAO {
    @Query("SELECT * FROM NewTaipeiPublicParkingLotPosition")
    List<NewTaipeiPublicParkingLotPositionPO> getAll();

    @Query("SELECT * FROM NewTaipeiPublicParkingLotPosition WHERE ID IN (:IDs)")
    List<NewTaipeiPublicParkingLotPositionPO> loadAllByIds(int[] IDs);

    @Query("SELECT * FROM NewTaipeiPublicParkingLotPosition WHERE NAME LIKE :pNAME LIMIT 1")
    NewTaipeiPublicParkingLotPositionPO findByName(String pNAME);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(NewTaipeiPublicParkingLotPositionPO... NewTaipeiPublicParkingLotPositions);

    @Update
    void updateAll(NewTaipeiPublicParkingLotPositionPO... NewTaipeiPublicParkingLotPositions);

    @Delete
    void delete(NewTaipeiPublicParkingLotPositionPO NewTaipeiPublicParkingLotPositions);

}
