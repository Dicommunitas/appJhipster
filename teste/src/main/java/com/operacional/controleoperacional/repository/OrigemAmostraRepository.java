package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.OrigemAmostra;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrigemAmostra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrigemAmostraRepository extends JpaRepository<OrigemAmostra, Long> {}
