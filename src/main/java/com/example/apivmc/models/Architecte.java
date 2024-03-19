package com.example.apivmc.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Architecte {
    @Id @GeneratedValue
    private Long Id;
    private String nom;

    @OneToMany(fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
    private List<Batiment> batiments;

    public Architecte(){}

    public Architecte(String nom){
        this.nom = nom;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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
}
