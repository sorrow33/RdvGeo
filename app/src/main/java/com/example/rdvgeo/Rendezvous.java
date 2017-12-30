package com.example.rdvgeo;

import android.location.Location;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * File: ${FILE_NAME}
 * Created: 30/12/2017
 * Last changed: 30/12/2017 15:15
 * Author: William
 */

public class Rendezvous {

    private int id;
    private float latitude;
    private float longitude;
    private int groupe;

    public Rendezvous(){}

    public Rendezvous(float latitude, float longitude, int groupe) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.groupe = groupe;
    }

    public int getId() {
        return id;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public int getGroupe() {
        return groupe;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setGroupe(int groupe) {
        this.groupe = groupe;
    }

    @Override
    public String toString() {
        return "Rendezvous{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", groupe=" + groupe +
                '}';
    }
}
