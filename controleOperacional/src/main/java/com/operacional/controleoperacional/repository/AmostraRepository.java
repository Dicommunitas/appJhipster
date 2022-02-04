package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.Amostra;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Amostra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AmostraRepository extends JpaRepository<Amostra, Long>, JpaSpecificationExecutor<Amostra> {}
