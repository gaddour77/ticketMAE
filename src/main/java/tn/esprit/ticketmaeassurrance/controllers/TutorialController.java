package tn.esprit.ticketmaeassurrance.controllers;

import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.ticketmaeassurrance.entities.Tutorial;
import tn.esprit.ticketmaeassurrance.services.TutorialService;

import java.util.List;

@RestController
@RequestMapping("/api/tutorials")
@CrossOrigin(origins = "http://localhost:4200")  // Angular frontend origin
public class TutorialController {

    @Autowired
    private TutorialService tutorialService;

    @PostMapping("/create")
    public ResponseEntity<Tutorial> createTutorial(@RequestParam("title") String title,
                                                   @RequestParam("video") MultipartFile video,
                                                   @RequestParam("access") String access) {
        System.out.println("Titre reçu : " + title);
        // Ajoutez un log ici
        System.out.println("Nom du fichier vidéo : " + video.getOriginalFilename());
        try {
            Tutorial tutorial = tutorialService.saveTutorial(title, video,access);
            return ResponseEntity.ok(tutorial);
        } catch (IOException | java.io.IOException e) {
            return ResponseEntity.status(500).build();
        }
    }
    @GetMapping("/all")
    public ResponseEntity<List<Tutorial>> getAllTutorials() {
        List<Tutorial> tutorials = tutorialService.getAllTutorials();
        return ResponseEntity.ok(tutorials);
    }
    @GetMapping("/getbyacces")
    public List<Tutorial> getbyAcces(){
        return tutorialService.getTutorials();
    }
}
