package com.example.apivmc.controllers;

import com.example.apivmc.models.City;

public record BatimentDTO(String ville,String nom, String description, String adresse, String annee, String archi, String lat, String lon) {
}
