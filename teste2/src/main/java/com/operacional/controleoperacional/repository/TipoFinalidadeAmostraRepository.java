package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.TipoFinalidadeAmostra;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TipoFinalidadeAmostra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoFinalidadeAmostraRepository extends JpaRepository<TipoFinalidadeAmostra, Long> {}
