package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.TipoRelatorio;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class TipoRelatorioRepositoryWithBagRelationshipsImpl implements TipoRelatorioRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<TipoRelatorio> fetchBagRelationships(Optional<TipoRelatorio> tipoRelatorio) {
        return tipoRelatorio.map(this::fetchUsuariosAuts);
    }

    @Override
    public Page<TipoRelatorio> fetchBagRelationships(Page<TipoRelatorio> tipoRelatorios) {
        return new PageImpl<>(
            fetchBagRelationships(tipoRelatorios.getContent()),
            tipoRelatorios.getPageable(),
            tipoRelatorios.getTotalElements()
        );
    }

    @Override
    public List<TipoRelatorio> fetchBagRelationships(List<TipoRelatorio> tipoRelatorios) {
        return Optional.of(tipoRelatorios).map(this::fetchUsuariosAuts).orElse(Collections.emptyList());
    }

    TipoRelatorio fetchUsuariosAuts(TipoRelatorio result) {
        return entityManager
            .createQuery(
                "select tipoRelatorio from TipoRelatorio tipoRelatorio left join fetch tipoRelatorio.usuariosAuts where tipoRelatorio is :tipoRelatorio",
                TipoRelatorio.class
            )
            .setParameter("tipoRelatorio", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<TipoRelatorio> fetchUsuariosAuts(List<TipoRelatorio> tipoRelatorios) {
        return entityManager
            .createQuery(
                "select distinct tipoRelatorio from TipoRelatorio tipoRelatorio left join fetch tipoRelatorio.usuariosAuts where tipoRelatorio in :tipoRelatorios",
                TipoRelatorio.class
            )
            .setParameter("tipoRelatorios", tipoRelatorios)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
