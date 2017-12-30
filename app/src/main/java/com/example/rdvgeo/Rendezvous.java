package com.example.rdvgeo;

/**
 * File: ${FILE_NAME}
 * Created: 30/12/2017
 * Last changed: 30/12/2017 15:15
 * Author: William
 */

public class Rendezvous {

    private int id;
    private String titre;
    private float latitude;
    private float longitude;
    private int groupe;

    public Rendezvous() {
    }

    public Rendezvous(String titre, float latitude, float longitude, int groupe) {
        this.titre = titre;
        this.latitude = latitude;
        this.longitude = longitude;
        this.groupe = groupe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getGroupe() {
        return groupe;
    }

    public void setGroupe(int groupe) {
        this.groupe = groupe;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    @Override
    public String toString() {
        return "Rendezvous{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", groupe=" + groupe +
                '}';
    }
}