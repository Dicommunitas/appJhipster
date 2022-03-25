package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.Lembrete;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Lembrete entity.
 */
@Repository
public interface LembreteRepository extends JpaRepository<Lembrete, Long> {
    default Optional<Lembrete> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Lembrete> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Lembrete> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct lembrete from Lembrete lembrete left join fetch lembrete.tipoRelatorio left join fetch lembrete.tipoOperacao",
        countQuery = "select count(distinct lembrete) from Lembrete lembrete"
    )
    Page<Lembrete> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct lembrete from Lembrete lembrete left join fetch lembrete.tipoRelatorio left join fetch lembrete.tipoOperacao")
    List<Lembrete> findAllWithToOneRelationships();

    @Query(
        "select lembrete from Lembrete lembrete left join fetch lembrete.tipoRelatorio left join fetch lembrete.tipoOperacao where lembrete.id =:id"
    )
    Optional<Lembrete> findOneWithToOneRelationships(@Param("id") Long id);
}
