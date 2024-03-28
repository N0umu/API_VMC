package com.example.apivmc.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity @Getter @Setter @NoArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private boolean expert = false;
    @ManyToMany
    @JoinTable(
            name = "myList",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "batiment_id") }
    )
    private List<Batiment> myList;

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    public void add(Batiment batiment){
        this.myList.add(batiment);
    }

    public void remove(Batiment batiment){
        this.myList.remove(batiment);
    }
}
