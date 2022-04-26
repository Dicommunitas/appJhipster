package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.Amostra;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Amostra entity.
 */
@Repository
public interface AmostraRepository extends JpaRepository<Amostra, Long>, JpaSpecificationExecutor<Amostra> {
    @Query("select amostra from Amostra amostra where amostra.amostradaPor.login = ?#{principal.username}")
    List<Amostra> findByAmostradaPorIsCurrentUser();

    @Query("select amostra from Amostra amostra where amostra.recebidaPor.login = ?#{principal.username}")
    List<Amostra> findByRecebidaPorIsCurrentUser();

    default Optional<Amostra> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Amostra> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Amostra> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct amostra from Amostra amostra left join fetch amostra.operacao left join fetch amostra.origemAmostra left join fetch amostra.tipoAmostra left join fetch amostra.amostradaPor left join fetch amostra.recebidaPor",
        countQuery = "select count(distinct amostra) from Amostra amostra"
    )
    Page<Amostra> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct amostra from Amostra amostra left join fetch amostra.operacao left join fetch amostra.origemAmostra left join fetch amostra.tipoAmostra left join fetch amostra.amostradaPor left join fetch amostra.recebidaPor"
    )
    List<Amostra> findAllWithToOneRelationships();

    @Query(
        "select amostra from Amostra amostra left join fetch amostra.operacao left join fetch amostra.origemAmostra left join fetch amostra.tipoAmostra left join fetch amostra.amostradaPor left join fetch amostra.recebidaPor where amostra.id =:id"
    )
    Optional<Amostra> findOneWithToOneRelationships(@Param("id") Long id);
}
