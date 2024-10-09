package com.medibot.back.controller;

import com.medibot.back.dtos.GetAllDTO;
import com.medibot.back.dtos.ProfileDTO;
import com.medibot.back.entities.*;
import com.medibot.back.services.MainService;
import com.medibot.back.sqlite.SqliteActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class MainController {
    @Autowired
    public MainService service;
    @Autowired
    private SqliteActions sqliteActions;

    @GetMapping("/get-all")
    public GetAllDTO getAll(){
        return service.getAll();
    }

    @PostMapping("/antecedent")
    public void postAntecedent(@RequestBody Antecedent antecedent){
        service.addAntecedent(antecedent);
    }

    @DeleteMapping("/antecedent/{id}")
    public void deleteAntecedent(@PathVariable String id){
        service.deleteAntecedent(id);
    }

    @PostMapping("/historiqueCommunication")
    public void postHistoriqueCommunication(@RequestBody HistoriqueCommunication historiqueCommunication){
        service.addHistoriqueCommunication(historiqueCommunication);
    }

    @DeleteMapping("/historiqueCommunication/{id}")
    public void deleteHistoriqueCommunication(@PathVariable String id){
        service.deleteHistoriqueCommunication(id);
    }

    @PostMapping("/informations")
    public void postInformations(@RequestBody Informations informations){
        service.addInformations(informations);
    }

    @DeleteMapping("/informations/{id}")
    public void deleteInformations(@PathVariable Integer id){
        service.deleteInformations(id);
    }

    @PostMapping("/poids")
    public void postPoids(@RequestBody Poids poids){
        service.addPoids(poids);
    }

    @DeleteMapping("/poids/{id}")
    public void deletePoids(@PathVariable String id){
        service.deletePoids(id);
    }

    @PostMapping("/taille")
    public void postTaille(@RequestBody Taille taille){
        service.addTaille(taille);
    }

    @DeleteMapping("/taille/{id}")
    public void deleteTaille(@PathVariable String id){
        service.deleteTaille(id);
    }

    @PostMapping("/medicamentUtilise")
    public void postMedicamentUtilise(@RequestBody MedicamentUtilise medicamentUtilise){
        service.addMedicamentUtilise(medicamentUtilise);
    }

    @PostMapping("/medicamentUtilise/delete")
    public void deleteMedicamentUtilise(@RequestBody MedicamentUtilise.MedicamentUtiliseId id){
        service.deleteMedicamentUtilise(id);
    }

    @PostMapping("/select-profile")
    public Boolean selectProfile(@RequestBody ProfileDTO profile){
        SqliteActions.CurrentBase = profile.name.replace(" ", "_");
        SqliteActions.SetPassword(profile.password);
        sqliteActions.ClearH2();
        if(!SqliteActions.Exist()){
            SqliteActions.InitBase();
        }
        if(!SqliteActions.CorrectPassword()){
            return false;
        }
        sqliteActions.ImportBase();
        return true;
    }

    @GetMapping("/profiles")
    public List<String> getProfiles(){
        return SqliteActions.AllExistingBases();
    }
}
