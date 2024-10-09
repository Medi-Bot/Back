package com.medibot.back.repositories;

import com.medibot.back.entities.HistoriqueCommunication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoriqueCommunicationRepository extends JpaRepository<HistoriqueCommunication, String> {
}