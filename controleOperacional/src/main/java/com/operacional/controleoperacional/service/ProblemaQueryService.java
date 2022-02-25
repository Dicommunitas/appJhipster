package com.operacional.controleoperacional.service;

import com.operacional.controleoperacional.domain.*; // for static metamodels
import com.operacional.controleoperacional.domain.Problema;
import com.operacional.controleoperacional.repository.ProblemaRepository;
import com.operacional.controleoperacional.service.criteria.ProblemaCriteria;
import com.operacional.controleoperacional.service.dto.ProblemaDTO;
import com.operacional.controleoperacional.service.mapper.ProblemaMapper;
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
 * Service for executing complex queries for {@link Problema} entities in the database.
 * The main input is a {@link ProblemaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProblemaDTO} or a {@link Page} of {@link ProblemaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProblemaQueryService extends QueryService<Problema> {

    private final Logger log = LoggerFactory.getLogger(ProblemaQueryService.class);

    private final ProblemaRepository problemaRepository;

    private final ProblemaMapper problemaMapper;

    public ProblemaQueryService(ProblemaRepository problemaRepository, ProblemaMapper problemaMapper) {
        this.problemaRepository = problemaRepository;
        this.problemaMapper = problemaMapper;
    }

    /**
     * Return a {@link List} of {@link ProblemaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProblemaDTO> findByCriteria(ProblemaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Problema> specification = createSpecification(criteria);
        return problemaMapper.toDto(problemaRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProblemaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProblemaDTO> findByCriteria(ProblemaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Problema> specification = createSpecification(criteria);
        return problemaRepository.findAll(specification, page).map(problemaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProblemaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Problema> specification = createSpecification(criteria);
        return problemaRepository.count(specification);
    }

    /**
     * Function to convert {@link ProblemaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Problema> createSpecification(ProblemaCriteria criteria) {
        Specification<Problema> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Problema_.id));
            }
            if (criteria.getData() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getData(), Problema_.data));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), Problema_.descricao));
            }
            if (criteria.getCriticidade() != null) {
                specification = specification.and(buildSpecification(criteria.getCriticidade(), Problema_.criticidade));
            }
            if (criteria.getAceitarFinalizacao() != null) {
                specification = specification.and(buildSpecification(criteria.getAceitarFinalizacao(), Problema_.aceitarFinalizacao));
            }
            if (criteria.getImpacto() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImpacto(), Problema_.impacto));
            }
            if (criteria.getStatusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getStatusId(), root -> root.join(Problema_.statuses, JoinType.LEFT).get(Status_.id))
                    );
            }
            if (criteria.getRelatorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRelatorId(), root -> root.join(Problema_.relator, JoinType.LEFT).get(Usuario_.id))
                    );
            }
        }
        return specification;
    }
}
