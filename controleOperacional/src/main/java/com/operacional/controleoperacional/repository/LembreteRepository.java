package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.Lembrete;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Lembrete entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LembreteRepository extends JpaRepository<Lembrete, Long> {}
