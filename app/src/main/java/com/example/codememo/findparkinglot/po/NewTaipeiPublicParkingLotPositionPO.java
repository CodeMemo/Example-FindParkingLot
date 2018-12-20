package com.example.codememo.findparkinglot.po;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "NewTaipeiPublicParkingLotPosition")
public class NewTaipeiPublicParkingLotPositionPO {
    @PrimaryKey
    @NonNull
    private String ID;

    private String AREA;

    private String NAME;

    private String TYPE;

    private String SUMMARY;

    private String ADDRESS;

    private String TEL;

    private String PAYTEX;

    private String SERVICETIME;

    private String TW97X;

    private String TW97Y;

    private String TOTALCAR;

    private String TOTALMOTOR;

    private String TOTALBIKE;

    public String getID() {
        return ID;
    }

    public void setID(String pID) {
        this.ID = pID;
    }

    public String getAREA() {
        return AREA;
    }

    public void setAREA(String AREA) {
        this.AREA = AREA;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String pNAME) {
        this.NAME = pNAME;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String pTYPE) {
        this.TYPE = pTYPE;
    }

    public String getSUMMARY() {
        return SUMMARY;
    }

    public void setSUMMARY(String pSUMMARY) {
        this.SUMMARY = pSUMMARY;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String pADDRESS) {
        this.ADDRESS = pADDRESS;
    }

    public String getTEL() {
        return TEL;
    }

    public void setTEL(String TEL) {
        this.TEL = TEL;
    }

    public String getPAYTEX() {
        return PAYTEX;
    }

    public void setPAYTEX(String pPAYTEX) {
        this.PAYTEX = pPAYTEX;
    }

    public String getSERVICETIME() {
        return SERVICETIME;
    }

    public void setSERVICETIME(String pSERVICETIME) {
        this.SERVICETIME = pSERVICETIME;
    }

    public String getTW97X() {
        return TW97X;
    }

    public void setTW97X(String pTW97X) {
        this.TW97X = pTW97X;
    }

    public String getTW97Y() {
        return TW97Y;
    }

    public void setTW97Y(String pTW97Y) {
        this.TW97Y = pTW97Y;
    }

    public String getTOTALCAR() {
        return TOTALCAR;
    }

    public void setTOTALCAR(String pTOTALCAR) {
        this.TOTALCAR = pTOTALCAR;
    }

    public String getTOTALMOTOR() {
        return TOTALMOTOR;
    }

    public void setTOTALMOTOR(String pTOTALMOTOR) {
        this.TOTALMOTOR = pTOTALMOTOR;
    }

    public String getTOTALBIKE() {
        return TOTALBIKE;
    }

    public void setTOTALBIKE(String pTOTALBIKE) {
        this.TOTALBIKE = pTOTALBIKE;
    }
}
