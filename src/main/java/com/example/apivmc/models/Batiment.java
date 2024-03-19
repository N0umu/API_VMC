package com.example.apivmc.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Batiment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String nom;
    private String descrip;
    private String adresse;
    private String annee;
    private String lat;
    private String lon;
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "city_id")
//    private City ville;

    public Batiment(){}

    public Batiment(String nom, String desc, String adresse, String annee, String lat, String lon){
        this.nom = nom;
        this.descrip = desc;
        this.adresse = adresse;
        this.annee = annee;
        this.lat = lat;
        this.lon = lon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
//    public City getVille() {
//        return ville;
//    }
//
//    public void setVille(City ville) {
//        this.ville = ville;
//    }
}
