package com.medibot.back.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Poids {
    @Id
    public String date;

    @Column(nullable = false)
    public Float poids;

    public String toExport(){
        return "('" + date + "'," + poids + ")";
    }
}
