package com.medibot.back.dtos;

import com.medibot.back.entities.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public class GetAllDTO {
    public List<Antecedent> antecedents;
    public List<HistoriqueCommunication> historiqueCommunications;
    public List<Informations> informations;
    public List<MedicamentUtilise> medicamentUtilises;
    public List<Poids> poids;
    public List<Taille> tailles;
}
