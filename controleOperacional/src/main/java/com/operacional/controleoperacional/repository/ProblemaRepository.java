package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.Problema;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Problema entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProblemaRepository extends JpaRepository<Problema, Long>, JpaSpecificationExecutor<Problema> {}
