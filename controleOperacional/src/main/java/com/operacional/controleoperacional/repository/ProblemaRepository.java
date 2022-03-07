package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.Problema;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Problema entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProblemaRepository extends JpaRepository<Problema, Long>, JpaSpecificationExecutor<Problema> {
    @Query("select problema from Problema problema where problema.relator.login = ?#{principal.username}")
    List<Problema> findByRelatorIsCurrentUser();
}
