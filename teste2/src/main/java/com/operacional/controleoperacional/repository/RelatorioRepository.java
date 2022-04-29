package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.Relatorio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Relatorio entity.
 */
@Repository
public interface RelatorioRepository extends JpaRepository<Relatorio, Long>, JpaSpecificationExecutor<Relatorio> {
    @Query("select relatorio from Relatorio relatorio where relatorio.responsavel.login = ?#{principal.username}")
    List<Relatorio> findByResponsavelIsCurrentUser();

    default Optional<Relatorio> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Relatorio> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Relatorio> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct relatorio from Relatorio relatorio left join fetch relatorio.tipo left join fetch relatorio.responsavel",
        countQuery = "select count(distinct relatorio) from Relatorio relatorio"
    )
    Page<Relatorio> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct relatorio from Relatorio relatorio left join fetch relatorio.tipo left join fetch relatorio.responsavel")
    List<Relatorio> findAllWithToOneRelationships();

    @Query(
        "select relatorio from Relatorio relatorio left join fetch relatorio.tipo left join fetch relatorio.responsavel where relatorio.id =:id"
    )
    Optional<Relatorio> findOneWithToOneRelationships(@Param("id") Long id);
}
