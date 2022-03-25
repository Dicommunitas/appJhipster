package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.FinalidadeAmostra;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FinalidadeAmostra entity.
 */
@Repository
public interface FinalidadeAmostraRepository extends JpaRepository<FinalidadeAmostra, Long> {
    default Optional<FinalidadeAmostra> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<FinalidadeAmostra> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<FinalidadeAmostra> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct finalidadeAmostra from FinalidadeAmostra finalidadeAmostra left join fetch finalidadeAmostra.tipoFinalidadeAmostra left join fetch finalidadeAmostra.amostra",
        countQuery = "select count(distinct finalidadeAmostra) from FinalidadeAmostra finalidadeAmostra"
    )
    Page<FinalidadeAmostra> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct finalidadeAmostra from FinalidadeAmostra finalidadeAmostra left join fetch finalidadeAmostra.tipoFinalidadeAmostra left join fetch finalidadeAmostra.amostra"
    )
    List<FinalidadeAmostra> findAllWithToOneRelationships();

    @Query(
        "select finalidadeAmostra from FinalidadeAmostra finalidadeAmostra left join fetch finalidadeAmostra.tipoFinalidadeAmostra left join fetch finalidadeAmostra.amostra where finalidadeAmostra.id =:id"
    )
    Optional<FinalidadeAmostra> findOneWithToOneRelationships(@Param("id") Long id);
}
