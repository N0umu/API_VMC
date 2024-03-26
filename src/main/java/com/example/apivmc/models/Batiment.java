package com.example.apivmc.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity @Getter @Setter @NoArgsConstructor
public class Batiment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String description;
    private String adresse;
    private String annee;
    private String lat;
    private String lon;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Photo> photos;
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "city_id")
//    private City ville;

    public Batiment(String nom, String desc, String adresse, String annee, String lat, String lon){
        this.nom = nom;
        this.description = desc;
        this.adresse = adresse;
        this.annee = annee;
        this.lat = lat;
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
