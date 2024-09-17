package tn.esprit.ticketmaeassurrance.controllers;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ticketmaeassurrance.entities.Panne;
import tn.esprit.ticketmaeassurrance.services.PanneServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/panne")
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
public class PanneController {
    private final PanneServiceImpl panneService;
    @PostMapping("/addpanne")
    public ResponseEntity<Panne> addPanne(@RequestBody  Panne panne){
        return ResponseEntity.ok(panneService.addPanne(panne));
    }
    @GetMapping("/getall")
    public List<Panne> getAll(){
        return panneService.getAll();
    }
    @GetMapping("/getbytype")
    public List<Panne> getByType(@RequestParam String type){
        return panneService.getDescriptionsParEnum(type);
    }
    @DeleteMapping("/deletepanne/{id}")
    public String deletepanne(@PathVariable Long id){
        return panneService.delete(id);
    }
}
