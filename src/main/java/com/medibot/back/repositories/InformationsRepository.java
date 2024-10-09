package com.medibot.back.repositories;

import com.medibot.back.entities.Informations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InformationsRepository extends JpaRepository<Informations, Integer> {
}