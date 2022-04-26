package com.operacional.controleoperacional.service;

import com.operacional.controleoperacional.domain.*; // for static metamodels
import com.operacional.controleoperacional.domain.Amostra;
import com.operacional.controleoperacional.repository.AmostraRepository;
import com.operacional.controleoperacional.service.criteria.AmostraCriteria;
import com.operacional.controleoperacional.service.dto.AmostraDTO;
import com.operacional.controleoperacional.service.mapper.AmostraMapper;
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
 * Service for executing complex queries for {@link Amostra} entities in the database.
 * The main input is a {@link AmostraCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AmostraDTO} or a {@link Page} of {@link AmostraDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AmostraQueryService extends QueryService<Amostra> {

    private final Logger log = LoggerFactory.getLogger(AmostraQueryService.class);

    private final AmostraRepository amostraRepository;

    private final AmostraMapper amostraMapper;

    public AmostraQueryService(AmostraRepository amostraRepository, AmostraMapper amostraMapper) {
        this.amostraRepository = amostraRepository;
        this.amostraMapper = amostraMapper;
    }

    /**
     * Return a {@link List} of {@link AmostraDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AmostraDTO> findByCriteria(AmostraCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Amostra> specification = createSpecification(criteria);
        return amostraMapper.toDto(amostraRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AmostraDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AmostraDTO> findByCriteria(AmostraCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Amostra> specification = createSpecification(criteria);
        return amostraRepository.findAll(specification, page).map(amostraMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AmostraCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Amostra> specification = createSpecification(criteria);
        return amostraRepository.count(specification);
    }

    /**
     * Function to convert {@link AmostraCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Amostra> createSpecification(AmostraCriteria criteria) {
        Specification<Amostra> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Amostra_.id));
            }
            if (criteria.getDataHoraColeta() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataHoraColeta(), Amostra_.dataHoraColeta));
            }
            if (criteria.getObservacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservacao(), Amostra_.observacao));
            }
            if (criteria.getIdentificadorExterno() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getIdentificadorExterno(), Amostra_.identificadorExterno));
            }
            if (criteria.getRecebimentoNoLaboratorio() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getRecebimentoNoLaboratorio(), Amostra_.recebimentoNoLaboratorio));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Amostra_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Amostra_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Amostra_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Amostra_.lastModifiedDate));
            }
            if (criteria.getFinalidadesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFinalidadesId(),
                            root -> root.join(Amostra_.finalidades, JoinType.LEFT).get(FinalidadeAmostra_.id)
                        )
                    );
            }
            if (criteria.getOperacaoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOperacaoId(), root -> root.join(Amostra_.operacao, JoinType.LEFT).get(Operacao_.id))
                    );
            }
            if (criteria.getOrigemAmostraId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrigemAmostraId(),
                            root -> root.join(Amostra_.origemAmostra, JoinType.LEFT).get(OrigemAmostra_.id)
                        )
                    );
            }
            if (criteria.getTipoAmostraId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTipoAmostraId(),
                            root -> root.join(Amostra_.tipoAmostra, JoinType.LEFT).get(TipoAmostra_.id)
                        )
                    );
            }
            if (criteria.getAmostradaPorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAmostradaPorId(),
                            root -> root.join(Amostra_.amostradaPor, JoinType.LEFT).get(User_.id)
                        )
                    );
            }
            if (criteria.getRecebidaPorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRecebidaPorId(),
                            root -> root.join(Amostra_.recebidaPor, JoinType.LEFT).get(User_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
