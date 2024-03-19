package com.example.apivmc.controllers;

import com.example.apivmc.dao.BatimentDAO;
import com.example.apivmc.dao.CityDAO;
import com.example.apivmc.models.Architecte;
import com.example.apivmc.models.Batiment;
import com.example.apivmc.models.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cities")
public class CityController {

    private CityDAO cities;

    private BatimentDAO batiments;

    public CityController(CityDAO cities, BatimentDAO batiments){
        this.cities = cities;
        this.batiments = batiments;
    }

    @GetMapping("")
    public List<City> getAllCities(){
        return this.cities.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<City> getCityById(@PathVariable long id) {
        Optional<City> city = this.cities.findById(id);
        if (city.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(city.get(), HttpStatus.OK);
    }

    @PostMapping("")
    public City createCity(@RequestBody CityDTO city){
        City created = new City(city.nom());
        created = this.cities.save(created);
        return created;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<City> deleteCityByID(@PathVariable int id) {
        Optional<City> city = this.cities.delete(id);
        if (city.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        this.cities.delete(city.get());
        return new ResponseEntity<>(city.get(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(@PathVariable long id, @RequestBody CityDTO newCityInfo) {
        Optional<City> city = this.cities.findById(id);
        if (city.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        city.get().setNom(newCityInfo.nom());

        return new ResponseEntity<>(this.cities.save(city.get()), HttpStatus.OK);
    }

    @PostMapping("/{id}/batiment")
    public ResponseEntity<City> addBatimentToCity(@RequestBody BatimentDTO batimentInfo, @PathVariable long id) {
        Optional<City> cityOptional = this.cities.findById(id);
        if (cityOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Batiment toAdd = new Batiment(batimentInfo.nom(),batimentInfo.descrip(),batimentInfo.adresse(),batimentInfo.annee(),batimentInfo.lat(),batimentInfo.lon());
        City city = cityOptional.get();
        city.add(toAdd);
        city = this.cities.save(city);
        return new ResponseEntity<>(city, HttpStatus.OK);

    }

    @PostMapping("/{idArchi}/batiment/{idBat}")
    public ResponseEntity<City> addBatimentByIdToCity(@PathVariable long idCity, @PathVariable long idBat) {
        Optional<City> cityOptional = this.cities.findById(idCity);
        if (cityOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Batiment> batimentOptional = this.batiments.findById(idBat);
        if (batimentOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        City city = cityOptional.get();
        Batiment batiment = batimentOptional.get();
        city.add(batiment);
        city = this.cities.save(city);
        return new ResponseEntity<>(city, HttpStatus.OK);

    }

    @DeleteMapping("/{idCity}/batiment/{idBat}")
    public ResponseEntity<City> removeBatimentByIDFromCity(@PathVariable long idCity, @PathVariable long idBat) {
        Optional<City> cityOptional = this.cities.delete(idCity);
        if (cityOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Optional<Batiment> batimentOptional = this.batiments.findById(idBat);
        if (batimentOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        City city = cityOptional.get();
        Batiment batiment = batimentOptional.get();
        city.remove(batiment);
        city = this.cities.save(city);
        return new ResponseEntity<>(city, HttpStatus.OK);
    }

}
