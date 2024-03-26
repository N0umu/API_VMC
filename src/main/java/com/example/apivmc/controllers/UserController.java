package com.example.apivmc.controllers;

import com.example.apivmc.dao.UserDAO;
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

    public UserController(UserDAO users){
        this.users = users;
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
}
