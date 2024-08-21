package tn.esprit.ticketmaeassurrance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.ticketmaeassurrance.entities.Tutorial;
import tn.esprit.ticketmaeassurrance.repositories.TutorialRepository;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class TutorialService {

    @Autowired
    private TutorialRepository tutorialRepository;

    private static final String UPLOAD_DIR = "C:/xampp/htdocs/MAE/video/";
   // C:/xampp/htdocs/easyFund/img/
    public Tutorial saveTutorial(String title, MultipartFile videoFile) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + videoFile.getOriginalFilename();

        // Chemin complet de destination du fichier
        Path uploadPath = Paths.get("C:/xampp/htdocs/MAE/video/", fileName);

        // Sauvegarde du fichier vidéo
        try (InputStream inputStream = videoFile.getInputStream()) {
            Files.copy(inputStream, uploadPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Erreur lors du transfert du fichier : " + e.getMessage());
            throw e;
        }

        // Enregistrement du chemin vidéo dans la base de données
        Tutorial tutorial = new Tutorial();
        tutorial.setTitle(title);
        tutorial.setVideoPath(fileName);

        return tutorialRepository.save(tutorial);
    }
    public List<Tutorial> getAllTutorials() {
        return tutorialRepository.findAll();
    }
}
