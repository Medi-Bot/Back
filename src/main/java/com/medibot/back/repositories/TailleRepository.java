package com.medibot.back.repositories;

import com.medibot.back.entities.Taille;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TailleRepository extends JpaRepository<Taille, String> {
}