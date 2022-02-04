package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.TipoRelatorio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TipoRelatorio entity.
 */
@Repository
public interface TipoRelatorioRepository extends JpaRepository<TipoRelatorio, Long> {
    @Query(
        value = "select distinct tipoRelatorio from TipoRelatorio tipoRelatorio left join fetch tipoRelatorio.usuariosAuts",
        countQuery = "select count(distinct tipoRelatorio) from TipoRelatorio tipoRelatorio"
    )
    Page<TipoRelatorio> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct tipoRelatorio from TipoRelatorio tipoRelatorio left join fetch tipoRelatorio.usuariosAuts")
    List<TipoRelatorio> findAllWithEagerRelationships();

    @Query("select tipoRelatorio from TipoRelatorio tipoRelatorio left join fetch tipoRelatorio.usuariosAuts where tipoRelatorio.id =:id")
    Optional<TipoRelatorio> findOneWithEagerRelationships(@Param("id") Long id);
}
