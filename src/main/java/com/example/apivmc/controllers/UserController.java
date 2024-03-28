package com.example.apivmc.controllers;

import com.example.apivmc.dao.ArchitecteDAO;
import com.example.apivmc.dao.BatimentDAO;
import com.example.apivmc.dao.CityDAO;
import com.example.apivmc.dao.UserDAO;
import com.example.apivmc.models.Architecte;
import com.example.apivmc.models.Batiment;
import com.example.apivmc.models.City;
import com.example.apivmc.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserDAO users;
    private BatimentDAO batiments;
    private CityDAO cities;
    private ArchitecteDAO architectes;

    public UserController(UserDAO users, BatimentDAO batiments, CityDAO cities, ArchitecteDAO architectes){
        this.users = users;
        this.batiments = batiments;
        this.cities = cities;
        this.architectes = architectes;
    }

    @GetMapping("")
    public List<User> getAllUsers(){
        return this.users.findAll();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email){
        Optional<User> user = this.users.findExistingUserWhereEmailLike(email);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        Optional<User> user = this.users.findById(id);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody UserDTO user){
        User created = new User(user.email(), user.password());
        if(this.findExistingUserWhereEmailLike(user.email()).isEmpty()){
            created = this.users.save(created);
            return new ResponseEntity<>(created, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/myList/{id}")
    public ResponseEntity<User> addBatimentToUserList(@PathVariable long id, @RequestBody BatimentDTO batimentInfo){
        Optional<User> user = this.users.findById(id);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        Optional<Batiment> batiment = this.batiments.findExistingBatimentWhereLatAndLonLike(batimentInfo.lat(), batimentInfo.lon());
        if (batiment.isEmpty()) {
            Batiment newBatiment = createBatiment(batimentInfo);
            user.get().add(newBatiment);
        }else{
            user.get().add(batiment.get());
        }
        this.users.save(user.get());
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUserByID(@PathVariable int id) {
        Optional<User> user = this.users.delete(id);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        this.users.delete(user.get());
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody UserDTO newUserInfo) {
        Optional<User> user = this.users.findById(id);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        user.get().setEmail(newUserInfo.email());
        user.get().setPrenom(newUserInfo.prenom());
        user.get().setNom(newUserInfo.nom());
        user.get().setExpert(newUserInfo.expert());

        return new ResponseEntity<>(this.users.save(user.get()), HttpStatus.OK);
    }

    @PutMapping("/resetPass/{id}")
    public ResponseEntity<User> updateUserPass(@PathVariable long id, @RequestBody UserDTO newUserInfo) {
        Optional<User> user = this.users.findById(id);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        user.get().setPassword(newUserInfo.password());

        return new ResponseEntity<>(this.users.save(user.get()), HttpStatus.OK);
    }

    @GetMapping("/emailVerif/{email}")
    public Optional<User> findExistingUserWhereEmailLike(@PathVariable String email) {
        return this.users.findExistingUserWhereEmailLike(email);
    }

    public Batiment createBatiment(BatimentDTO batiment){

        Batiment created = new Batiment(batiment.nom(), batiment.description(), batiment.adresse(), batiment.annee(), batiment.lat(), batiment.lon());
        created = this.batiments.save(created);

        Optional<Architecte> architecte = this.architectes.findExistingArchitecteWhereNomLike(batiment.archi());
        if (architecte.isEmpty()) {
            Architecte newArchi = new Architecte(batiment.archi());
            newArchi.add(created);
            this.architectes.save(newArchi);
        }else{
            architecte.get().add(created);
            this.architectes.save(architecte.get());
        }
        Optional<City> city = this.cities.findExistingCityWhereNomLike(batiment.ville());
        if (city.isEmpty()) {
            City newCity = new City(batiment.ville());
            newCity.add(created);
            this.cities.save(newCity);
        }else{
            city.get().add(created);
            this.cities.save(city.get());
        }
        return created;
    }
}
