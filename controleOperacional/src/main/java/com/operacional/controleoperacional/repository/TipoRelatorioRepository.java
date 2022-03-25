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
public interface TipoRelatorioRepository extends TipoRelatorioRepositoryWithBagRelationships, JpaRepository<TipoRelatorio, Long> {
    default Optional<TipoRelatorio> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<TipoRelatorio> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<TipoRelatorio> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
