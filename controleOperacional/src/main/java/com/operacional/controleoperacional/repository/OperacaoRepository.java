package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.Operacao;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Operacao entity.
 */
@Repository
public interface OperacaoRepository extends JpaRepository<Operacao, Long>, JpaSpecificationExecutor<Operacao> {
    default Optional<Operacao> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Operacao> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Operacao> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct operacao from Operacao operacao left join fetch operacao.produto left join fetch operacao.tipoOperacao",
        countQuery = "select count(distinct operacao) from Operacao operacao"
    )
    Page<Operacao> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct operacao from Operacao operacao left join fetch operacao.produto left join fetch operacao.tipoOperacao")
    List<Operacao> findAllWithToOneRelationships();

    @Query(
        "select operacao from Operacao operacao left join fetch operacao.produto left join fetch operacao.tipoOperacao where operacao.id =:id"
    )
    Optional<Operacao> findOneWithToOneRelationships(@Param("id") Long id);
}
