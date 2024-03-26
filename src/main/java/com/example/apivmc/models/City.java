package com.example.apivmc.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity @Getter @Setter @NoArgsConstructor
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Batiment> batiments;
    public City(String nom){
        this.nom = nom;
    }
    public void add(Batiment batiment){
        this.batiments.add(batiment);
    }
    public void remove(Batiment batiment){
        this.batiments.remove(batiment);
    }
}
