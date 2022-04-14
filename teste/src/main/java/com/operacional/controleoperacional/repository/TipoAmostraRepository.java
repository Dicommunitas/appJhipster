package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.TipoAmostra;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TipoAmostra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoAmostraRepository extends JpaRepository<TipoAmostra, Long> {}
