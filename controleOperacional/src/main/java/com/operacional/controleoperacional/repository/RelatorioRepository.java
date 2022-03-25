package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.Relatorio;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Relatorio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RelatorioRepository extends JpaRepository<Relatorio, Long>, JpaSpecificationExecutor<Relatorio> {
    @Query("select relatorio from Relatorio relatorio where relatorio.responsavel.login = ?#{principal.username}")
    List<Relatorio> findByResponsavelIsCurrentUser();
}
