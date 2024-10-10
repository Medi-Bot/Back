package com.medibot.back.services;

import com.medibot.back.dtos.GetAllDTO;
import com.medibot.back.entities.*;
import com.medibot.back.repositories.*;
import com.medibot.back.sqlite.SqliteActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MainService {
    @Autowired
    SqliteActions sqliteActions;
    @Autowired
    public AntecedentRepository antecedentRepository;
    @Autowired
    public HistoriqueCommunicationRepository historiqueCommunicationRepository;
    @Autowired
    public InformationsRepository informationsRepository;
    @Autowired
    public PoidsRepository poidsRepository;
    @Autowired
    public TailleRepository tailleRepository;
    @Autowired
    public MedicamentUtiliseRepository medicamentUtiliseRepository;

    public List<Antecedent> getAllAntecedent(){
        return antecedentRepository.findAll();
    }
    
    public List<HistoriqueCommunication> getAllHistoriqueCommunication(){
        return historiqueCommunicationRepository.findAll();
    }
    
    public List<Informations> getAllInformations(){
        return informationsRepository.findAll();
    }
    
    public List<MedicamentUtilise> getAllMedicamentUtilise(){
        return medicamentUtiliseRepository.findAll();
    }

    public List<MedicamentUtilise> getAllMedicamentUtiliseNow() {
        LocalDate localDate = LocalDate.now();
        List<MedicamentUtilise> allMedicamentUtilise = getAllMedicamentUtilise();
        List<MedicamentUtilise> allMedicamentUtiliseNow = new ArrayList<>();
        for (MedicamentUtilise medicamentUtilise : allMedicamentUtilise) {
            LocalDate startDate = LocalDate.parse(medicamentUtilise.id.dateDebut.split("T")[0]);
            LocalDate endDate = LocalDate.parse(medicamentUtilise.dateFin.split("T")[0]);
            if (localDate.isAfter(startDate) && localDate.isBefore(endDate)) {
                allMedicamentUtiliseNow.add(medicamentUtilise);
            }
        }
        return allMedicamentUtiliseNow;
    }

    public List<Poids> getAllPoids(){
        return poidsRepository.findAll();
    }
    
    public List<Taille> getAllTaille(){
        return tailleRepository.findAll();
    }

    public GetAllDTO getAll(){
        GetAllDTO dto = new GetAllDTO();
        dto.antecedents = getAllAntecedent();
        dto.historiqueCommunications = getAllHistoriqueCommunication();
        dto.poids = getAllPoids();
        dto.informations = getAllInformations();
        dto.medicamentUtilises = getAllMedicamentUtiliseNow();
        dto.tailles = getAllTaille();
        return dto;
    }

    public void addAntecedent(Antecedent antecedent){
        antecedentRepository.save(antecedent);
        sqliteActions.ExportBase();
    }

    public void addHistoriqueCommunication(HistoriqueCommunication historiqueCommunication){
        historiqueCommunicationRepository.save(historiqueCommunication);
        sqliteActions.ExportBase();
    }

    public void addInformations(Informations informations){
        informationsRepository.save(informations);
        sqliteActions.ExportBase();
    }

    public void addMedicamentUtilise(MedicamentUtilise medicamentUtilise){
        medicamentUtiliseRepository.save(medicamentUtilise);
        sqliteActions.ExportBase();
    }

    public void addPoids(Poids poids){
        poidsRepository.save(poids);
        sqliteActions.ExportBase();
    }

    public void addTaille(Taille taille){
        tailleRepository.save(taille);
        sqliteActions.ExportBase();
    }

    public void deleteAntecedent(String id){
        antecedentRepository.deleteById(id);
        sqliteActions.ExportBase();
    }

    public void deleteHistoriqueCommunication(String id){
        historiqueCommunicationRepository.deleteById(id);
        sqliteActions.ExportBase();
    }

    public void deleteInformations(Integer id){
        informationsRepository.deleteById(id);
        sqliteActions.ExportBase();
    }

    public void deleteMedicamentUtilise(MedicamentUtilise.MedicamentUtiliseId id){
        medicamentUtiliseRepository.deleteById(id);
        sqliteActions.ExportBase();
    }

    public void deletePoids(String id){
        poidsRepository.deleteById(id);
        sqliteActions.ExportBase();
    }

    public void deleteTaille(String id){
        tailleRepository.deleteById(id);
        sqliteActions.ExportBase();
    }
}
