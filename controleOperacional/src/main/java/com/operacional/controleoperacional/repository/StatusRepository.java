package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.Status;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Status entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    @Query("select status from Status status where status.relator.login = ?#{principal.username}")
    List<Status> findByRelatorIsCurrentUser();

    @Query("select status from Status status where status.responsavel.login = ?#{principal.username}")
    List<Status> findByResponsavelIsCurrentUser();
}
