package com.example.apivmc.controllers;

import com.example.apivmc.dao.ArchitecteDAO;
import com.example.apivmc.dao.BatimentDAO;
import com.example.apivmc.dao.CityDAO;
import com.example.apivmc.dao.PhotoDAO;
import com.example.apivmc.models.Architecte;
import com.example.apivmc.models.Batiment;
import com.example.apivmc.models.City;
import com.example.apivmc.models.Photo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/batiments")
public class BatimentController {
    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";
    private BatimentDAO batiments;
    private CityDAO cities;
    private ArchitecteDAO architectes;
    private PhotoDAO    photo;

    public BatimentController(BatimentDAO batiments, CityDAO cities, ArchitecteDAO architectes, PhotoDAO photo){
        this.batiments = batiments;
        this.cities = cities;
        this.architectes = architectes;
        this.photo = photo;
    }

    @GetMapping("")
    public List<Batiment> getAllBatiments(){
        return this.batiments.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Batiment> getBatimentById(@PathVariable long id) {
        Optional<Batiment> batiment = this.batiments.findById(id);
        if (batiment.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(batiment.get(), HttpStatus.OK);
    }


    @PostMapping("")
    public ResponseEntity<Batiment> createBatiment(@RequestBody BatimentDTO batiment){

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
        return new ResponseEntity<>(created, HttpStatus.OK);
    }
   @PostMapping("/image/{batimentId}")
public String posteImageID(@PathVariable Long batimentId, @RequestParam("image") MultipartFile file) throws IOException {
    // Convertir l'image en byte[]
    byte[] imageData = file.getBytes();

    // Récupérer le bâtiment correspondant à batimentId
    Optional<Batiment> batimentOptional = batiments.findById(batimentId);
    if (batimentOptional.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Batiment not found");
    }
    Batiment batiment = batimentOptional.get();

    // Créer une nouvelle instance de l'entité Photo
    Photo image = new Photo();
    image.setNom(file.getOriginalFilename());
    image.setPicByte(imageData);
    image.setBatiment(batiment);

    // Enregistrer l'image dans la base de données
    photo.save(image);

    return "Success";
}



    @PostMapping("/image")
    public String uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());


        // Convertir l'image en byte[]
        byte[] imageData = file.getBytes();
// Récupérer le bâtiment correspondant à batimentId
      //  Batiment batiment = batiments.findById(batimentId).get();

         //Créer une nouvelle instance de l'entité Image
        Photo image = new Photo();
        image.setNom(file.getOriginalFilename());
        image.setPicByte(imageData);
       // image.setBatiment(batiment);
        //Photo image = new Photo(file.getOriginalFilename(), imageData, batiment);
        // Enregistrer l'image dans la base de données
        photo.save(image);
        return "Success";
    }

  @GetMapping("/{id}/image")
public ResponseEntity<byte[]> getImagesByBatimentId(@PathVariable Long id) {
    try {
        // Récupérer le bâtiment correspondant à l'ID
        Optional<Batiment> batiment = this.batiments.findById(id);
        if (batiment.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Récupérer toutes les images associées à ce bâtiment
        List<Photo> photos = this.photo.findByBatiment(batiment.get());

        // Vérifier qu'il y a au moins une image
        if (photos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Récupérer toutes les images et renvoyer les données binaires
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (Photo photo : photos) {
            outputStream.write(photo.getPicByte());
        }
        byte[] allImagesData = outputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(allImagesData, headers, HttpStatus.OK);
    } catch (Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
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
        batiment.get().setDescription(newBatimentInfo.description());
        batiment.get().setAdresse(newBatimentInfo.adresse());
        batiment.get().setAnnee(newBatimentInfo.annee());
        batiment.get().setLat(newBatimentInfo.lat());
        batiment.get().setLon(newBatimentInfo.lon());

        return new ResponseEntity<>(this.batiments.save(batiment.get()), HttpStatus.OK);
    }
}
