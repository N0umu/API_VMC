package com.example.apivmc.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Getter @Setter @NoArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String nom;
    @Column(length = 50000000)
    private byte[] picByte;

    public Photo(String nom, byte[] picByte){
        this.nom = nom;
        this.picByte = picByte;
    }
}
