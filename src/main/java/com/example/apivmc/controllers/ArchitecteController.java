package com.example.apivmc.controllers;

import com.example.apivmc.dao.ArchitecteDAO;
import com.example.apivmc.dao.BatimentDAO;
import com.example.apivmc.dao.CityDAO;
import com.example.apivmc.models.Architecte;
import com.example.apivmc.models.Batiment;
import com.example.apivmc.models.City;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/architectes")
public class ArchitecteController {

    private ArchitecteDAO architectes;

    private BatimentDAO batiments;

    public ArchitecteController(ArchitecteDAO architectes, BatimentDAO batiments){
        this.architectes = architectes;
        this.batiments = batiments;
    }

    @GetMapping("")
    public List<Architecte> getAllArchitectes(){
        return this.architectes.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Architecte> getArchitecteById(@PathVariable long id) {
        Optional<Architecte> architecte = this.architectes.findById(id);
        if (architecte.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(architecte.get(), HttpStatus.OK);
    }

    @PostMapping("")
    public Architecte createArchitecte(@RequestBody CityDTO architecte){
        Architecte created = new Architecte(architecte.nom());
        created = this.architectes.save(created);
        return created;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Architecte> deleteArchitecteByID(@PathVariable int id) {
        Optional<Architecte> architecte = this.architectes.delete(id);
        if (architecte.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        this.architectes.delete(architecte.get());
        return new ResponseEntity<>(architecte.get(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Architecte> updateArchitecte(@PathVariable long id, @RequestBody CityDTO newCityInfo) {
        Optional<Architecte> architecte = this.architectes.findById(id);
        if (architecte.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        architecte.get().setNom(newCityInfo.nom());

        return new ResponseEntity<>(this.architectes.save(architecte.get()), HttpStatus.OK);
    }

    @PostMapping("/{id}/batiment")
    public ResponseEntity<Architecte> addBatimentToArchitecte(@RequestBody BatimentDTO batimentInfo, @PathVariable long id) {
        Optional<Architecte> architecteOptional = this.architectes.findById(id);
        if (architecteOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Batiment toAdd = new Batiment(batimentInfo.nom(),batimentInfo.descrip(),batimentInfo.adresse(),batimentInfo.annee(),batimentInfo.lat(),batimentInfo.lon());
        Architecte architecte = architecteOptional.get();
        architecte.add(toAdd);
        architecte = this.architectes.save(architecte);
        return new ResponseEntity<>(architecte, HttpStatus.OK);

    }

    @PostMapping("/{idArchi}/batiment/{idBat}")
    public ResponseEntity<Architecte> addBatimentByIdToArchitecte(@PathVariable long idArchi, @PathVariable long idBat) {
        Optional<Architecte> architecteOptional = this.architectes.findById(idArchi);
        if (architecteOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Batiment> batimentOptional = this.batiments.findById(idBat);
        if (batimentOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Architecte architecte = architecteOptional.get();
        Batiment batiment = batimentOptional.get();
        architecte.add(batiment);
        architecte = this.architectes.save(architecte);
        return new ResponseEntity<>(architecte, HttpStatus.OK);

    }
}
