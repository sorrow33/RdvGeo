package com.example.rdvgeo;

public class Rendezvous {

    private int id;
    private String titre;
    private int emetteur;
    private double latitude;
    private double longitude;
    private int groupe;

    public Rendezvous() {
    }

    public Rendezvous(String titre,int emetteur, double longitude, double latitude) {
        this.titre = titre;
        this.emetteur = emetteur;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public int getEmetteur() {
        return emetteur;
    }

    public void setEmetteur(int emetteur) {
        this.emetteur = emetteur;
    }

    @Override
    public String toString() {
        return "Rendezvous{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}