package com.example.apivmc.controllers;

import com.example.apivmc.dao.BatimentDAO;
import com.example.apivmc.dao.CityDAO;
import com.example.apivmc.models.Batiment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/batiments")
public class BatimentController {
    private BatimentDAO batiments;
    private CityDAO cities;

    public BatimentController(BatimentDAO batiments, CityDAO cities){
        this.batiments = batiments;
        this.cities = cities;

    }

    @GetMapping("")
    public List<Batiment> getAllBatiments(){
        return this.batiments.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Batiment> getBatimentById(@PathVariable long id) {
        Optional<Batiment> user = this.batiments.findById(id);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }


    @PostMapping("")
    public Batiment createBatiment(@RequestBody BatimentDTO batiment){
        Batiment created = new Batiment(batiment.nom(), batiment.descrip(), batiment.adresse(), batiment.annee(), batiment.lat(), batiment.lon());
        created = this.batiments.save(created);
        return created;
    }

    //Alternative
//    @PostMapping("/cities/{cityId}/batiments")
//    public ResponseEntity<Batiment> createBatiment(@PathVariable(value = "cityId") Long cityId, @RequestBody Batiment batimentInfo) {
//        Optional<Batiment> batiment = cities.findById(cityId).map(city -> {
//            batimentInfo.setVille(city);
//            return batiments.save(batimentInfo);
//        });
//        return new ResponseEntity<>(batiment.get(), HttpStatus.OK);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Batiment> deleteBatimentByID(@PathVariable int id) {
        Optional<Batiment> batiment = this.batiments.delete(id);
        if (batiment.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        this.batiments.delete(batiment.get());
        return new ResponseEntity<>(batiment.get(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Batiment> updateBatiment(@PathVariable long id, @RequestBody BatimentDTO newBatimentInfo) {
        Optional<Batiment> batiment = this.batiments.findById(id);
        if (batiment.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        batiment.get().setNom(newBatimentInfo.nom());
        batiment.get().setDescrip(newBatimentInfo.descrip());
        batiment.get().setAdresse(newBatimentInfo.adresse());
        batiment.get().setAnnee(newBatimentInfo.annee());
        batiment.get().setLat(newBatimentInfo.lat());
        batiment.get().setLon(newBatimentInfo.lon());

        return new ResponseEntity<>(this.batiments.save(batiment.get()), HttpStatus.OK);
    }
}
