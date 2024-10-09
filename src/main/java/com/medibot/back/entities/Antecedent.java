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
public class Antecedent {
    @Id
    public String date;

    @Column(nullable = false)
    public String description;

    public String toExport(){
        return "('" + date + "','" + description + "')";
    }
}
