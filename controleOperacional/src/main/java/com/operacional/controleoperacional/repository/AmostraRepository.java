package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.Amostra;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Amostra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AmostraRepository extends JpaRepository<Amostra, Long>, JpaSpecificationExecutor<Amostra> {
    @Query("select amostra from Amostra amostra where amostra.amostradaPor.login = ?#{principal.username}")
    List<Amostra> findByAmostradaPorIsCurrentUser();

    @Query("select amostra from Amostra amostra where amostra.recebidaPor.login = ?#{principal.username}")
    List<Amostra> findByRecebidaPorIsCurrentUser();
}
