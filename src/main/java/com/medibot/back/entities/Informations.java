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
public class Informations {
    @Id
    public Integer id;

    @Column
    public String dateDeNaissance;

    public String toExport(){
        return "(" + id + ",'" + Objects.requireNonNullElse(dateDeNaissance, "") + "')";
    }
}
