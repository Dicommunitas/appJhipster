package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.Status;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Status entity.
 */
@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    @Query("select status from Status status where status.relator.login = ?#{principal.username}")
    List<Status> findByRelatorIsCurrentUser();

    @Query("select status from Status status where status.responsavel.login = ?#{principal.username}")
    List<Status> findByResponsavelIsCurrentUser();

    default Optional<Status> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Status> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Status> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct status from Status status left join fetch status.relator left join fetch status.responsavel left join fetch status.problema",
        countQuery = "select count(distinct status) from Status status"
    )
    Page<Status> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct status from Status status left join fetch status.relator left join fetch status.responsavel left join fetch status.problema"
    )
    List<Status> findAllWithToOneRelationships();

    @Query(
        "select status from Status status left join fetch status.relator left join fetch status.responsavel left join fetch status.problema where status.id =:id"
    )
    Optional<Status> findOneWithToOneRelationships(@Param("id") Long id);
}
