package com.operacional.controleoperacional.service;

import com.operacional.controleoperacional.domain.*; // for static metamodels
import com.operacional.controleoperacional.domain.Relatorio;
import com.operacional.controleoperacional.repository.RelatorioRepository;
import com.operacional.controleoperacional.service.criteria.RelatorioCriteria;
import com.operacional.controleoperacional.service.dto.RelatorioDTO;
import com.operacional.controleoperacional.service.mapper.RelatorioMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Relatorio} entities in the database.
 * The main input is a {@link RelatorioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RelatorioDTO} or a {@link Page} of {@link RelatorioDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RelatorioQueryService extends QueryService<Relatorio> {

    private final Logger log = LoggerFactory.getLogger(RelatorioQueryService.class);

    private final RelatorioRepository relatorioRepository;

    private final RelatorioMapper relatorioMapper;

    public RelatorioQueryService(RelatorioRepository relatorioRepository, RelatorioMapper relatorioMapper) {
        this.relatorioRepository = relatorioRepository;
        this.relatorioMapper = relatorioMapper;
    }

    /**
     * Return a {@link List} of {@link RelatorioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RelatorioDTO> findByCriteria(RelatorioCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Relatorio> specification = createSpecification(criteria);
        return relatorioMapper.toDto(relatorioRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RelatorioDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RelatorioDTO> findByCriteria(RelatorioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Relatorio> specification = createSpecification(criteria);
        return relatorioRepository.findAll(specification, page).map(relatorioMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RelatorioCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Relatorio> specification = createSpecification(criteria);
        return relatorioRepository.count(specification);
    }

    /**
     * Function to convert {@link RelatorioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Relatorio> createSpecification(RelatorioCriteria criteria) {
        Specification<Relatorio> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Relatorio_.id));
            }
            if (criteria.getTipoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTipoId(), root -> root.join(Relatorio_.tipo, JoinType.LEFT).get(TipoRelatorio_.id))
                    );
            }
            if (criteria.getResponsavelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getResponsavelId(),
                            root -> root.join(Relatorio_.responsavel, JoinType.LEFT).get(Usuario_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
