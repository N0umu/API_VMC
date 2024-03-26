package com.example.apivmc.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity @Getter @Setter @NoArgsConstructor
public class Architecte {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String nom;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Batiment> batiments;

    public Architecte(String nom){
        this.nom = nom;
    }
    public void add(Batiment batiment){
        this.batiments.add(batiment);
    }
}
