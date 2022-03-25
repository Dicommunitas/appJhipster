package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.Problema;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Problema entity.
 */
@Repository
public interface ProblemaRepository extends JpaRepository<Problema, Long>, JpaSpecificationExecutor<Problema> {
    @Query("select problema from Problema problema where problema.relator.login = ?#{principal.username}")
    List<Problema> findByRelatorIsCurrentUser();

    default Optional<Problema> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Problema> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Problema> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct problema from Problema problema left join fetch problema.relator",
        countQuery = "select count(distinct problema) from Problema problema"
    )
    Page<Problema> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct problema from Problema problema left join fetch problema.relator")
    List<Problema> findAllWithToOneRelationships();

    @Query("select problema from Problema problema left join fetch problema.relator where problema.id =:id")
    Optional<Problema> findOneWithToOneRelationships(@Param("id") Long id);
}
