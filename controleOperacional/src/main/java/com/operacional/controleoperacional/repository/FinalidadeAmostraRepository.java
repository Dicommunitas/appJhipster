package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.FinalidadeAmostra;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FinalidadeAmostra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FinalidadeAmostraRepository extends JpaRepository<FinalidadeAmostra, Long> {}
