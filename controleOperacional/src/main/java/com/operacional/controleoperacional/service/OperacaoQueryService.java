package com.operacional.controleoperacional.service;

import com.operacional.controleoperacional.domain.*; // for static metamodels
import com.operacional.controleoperacional.domain.Operacao;
import com.operacional.controleoperacional.repository.OperacaoRepository;
import com.operacional.controleoperacional.service.criteria.OperacaoCriteria;
import com.operacional.controleoperacional.service.dto.OperacaoDTO;
import com.operacional.controleoperacional.service.mapper.OperacaoMapper;
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
 * Service for executing complex queries for {@link Operacao} entities in the database.
 * The main input is a {@link OperacaoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OperacaoDTO} or a {@link Page} of {@link OperacaoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OperacaoQueryService extends QueryService<Operacao> {

    private final Logger log = LoggerFactory.getLogger(OperacaoQueryService.class);

    private final OperacaoRepository operacaoRepository;

    private final OperacaoMapper operacaoMapper;

    public OperacaoQueryService(OperacaoRepository operacaoRepository, OperacaoMapper operacaoMapper) {
        this.operacaoRepository = operacaoRepository;
        this.operacaoMapper = operacaoMapper;
    }

    /**
     * Return a {@link List} of {@link OperacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OperacaoDTO> findByCriteria(OperacaoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Operacao> specification = createSpecification(criteria);
        return operacaoMapper.toDto(operacaoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OperacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OperacaoDTO> findByCriteria(OperacaoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Operacao> specification = createSpecification(criteria);
        return operacaoRepository.findAll(specification, page).map(operacaoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OperacaoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Operacao> specification = createSpecification(criteria);
        return operacaoRepository.count(specification);
    }

    /**
     * Function to convert {@link OperacaoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Operacao> createSpecification(OperacaoCriteria criteria) {
        Specification<Operacao> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Operacao_.id));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), Operacao_.descricao));
            }
            if (criteria.getVolume() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVolume(), Operacao_.volume));
            }
            if (criteria.getInicio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInicio(), Operacao_.inicio));
            }
            if (criteria.getFim() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFim(), Operacao_.fim));
            }
            if (criteria.getQuantidadeAmostras() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantidadeAmostras(), Operacao_.quantidadeAmostras));
            }
            if (criteria.getObservacao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObservacao(), Operacao_.observacao));
            }
            if (criteria.getTipoOperacaoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTipoOperacaoId(),
                            root -> root.join(Operacao_.tipoOperacao, JoinType.LEFT).get(TipoOperacao_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
