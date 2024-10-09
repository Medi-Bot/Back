package com.medibot.back.repositories;

import com.medibot.back.entities.MedicamentUtilise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicamentUtiliseRepository extends JpaRepository<MedicamentUtilise, MedicamentUtilise.MedicamentUtiliseId> {
}