package com.medibot.back.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class HistoriqueCommunication {
    @Id
    public String date;

    @Column
    public String message;

    @Column
    public String reponse;

    public String toExport(){
        return "('" + date + "','" + Objects.requireNonNullElse(message, "") + "','" + Objects.requireNonNullElse(reponse, "") + "')";
    }
}
