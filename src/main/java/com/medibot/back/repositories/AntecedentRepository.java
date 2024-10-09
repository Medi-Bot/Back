package com.medibot.back.repositories;

import com.medibot.back.entities.Antecedent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AntecedentRepository extends JpaRepository<Antecedent, String> {
}