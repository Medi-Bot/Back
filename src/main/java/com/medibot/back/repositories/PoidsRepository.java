package com.medibot.back.repositories;

import com.medibot.back.entities.Poids;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PoidsRepository extends JpaRepository<Poids, String> {
}