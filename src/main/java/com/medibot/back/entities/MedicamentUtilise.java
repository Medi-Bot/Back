package com.medibot.back.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class MedicamentUtilise {
    @EmbeddedId
    public MedicamentUtiliseId id;

    @Column
    public String frequence;

    @Column
    public String dateFin;

    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MedicamentUtiliseId{
        @Column(nullable = false)
        public String dateDebut;

        @Column(nullable = false)
        public String nom;
    }

    public String toExport(){
        return "('" + id.dateDebut + "','" + id.nom + "','" + Objects.requireNonNullElse(frequence, "") + "','" + Objects.requireNonNullElse(dateFin, "") + "')";
    }
}
