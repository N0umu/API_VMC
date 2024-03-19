package com.example.apivmc.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String nom;

    @OneToMany(fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
    private List<Batiment> batiments;
    public City(){}
    public City(String nom){
        this.nom = nom;
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

    public List<Batiment> getBatiments() {
        return batiments;
    }

    public void setBatiments(List<Batiment> batiments) {
        this.batiments = batiments;
    }
    public void add(Batiment batiment){
        this.batiments.add(batiment);
    }
    public void remove(Batiment batiment){
        this.batiments.remove(batiment);
    }
}
