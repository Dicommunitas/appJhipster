package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.Produto;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ProdutoRepositoryWithBagRelationshipsImpl implements ProdutoRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Produto> fetchBagRelationships(Optional<Produto> produto) {
        return produto.map(this::fetchAlertas);
    }

    @Override
    public Page<Produto> fetchBagRelationships(Page<Produto> produtos) {
        return new PageImpl<>(fetchBagRelationships(produtos.getContent()), produtos.getPageable(), produtos.getTotalElements());
    }

    @Override
    public List<Produto> fetchBagRelationships(List<Produto> produtos) {
        return Optional.of(produtos).map(this::fetchAlertas).get();
    }

    Produto fetchAlertas(Produto result) {
        return entityManager
            .createQuery("select produto from Produto produto left join fetch produto.alertas where produto is :produto", Produto.class)
            .setParameter("produto", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Produto> fetchAlertas(List<Produto> produtos) {
        return entityManager
            .createQuery(
                "select distinct produto from Produto produto left join fetch produto.alertas where produto in :produtos",
                Produto.class
            )
            .setParameter("produtos", produtos)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
