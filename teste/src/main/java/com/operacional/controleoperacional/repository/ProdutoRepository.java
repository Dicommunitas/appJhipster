package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.Produto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Produto entity.
 */
@Repository
public interface ProdutoRepository
    extends ProdutoRepositoryWithBagRelationships, JpaRepository<Produto, Long>, JpaSpecificationExecutor<Produto> {
    default Optional<Produto> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Produto> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Produto> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
