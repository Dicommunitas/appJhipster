package com.operacional.controleamostragateway.service.impl;

import com.operacional.controleamostragateway.domain.A;
import com.operacional.controleamostragateway.repository.ARepository;
import com.operacional.controleamostragateway.service.AService;
import com.operacional.controleamostragateway.service.dto.ADTO;
import com.operacional.controleamostragateway.service.mapper.AMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link A}.
 */
@Service
@Transactional
public class AServiceImpl implements AService {

    private final Logger log = LoggerFactory.getLogger(AServiceImpl.class);

    private final ARepository aRepository;

    private final AMapper aMapper;

    public AServiceImpl(ARepository aRepository, AMapper aMapper) {
        this.aRepository = aRepository;
        this.aMapper = aMapper;
    }

    @Override
    public Mono<ADTO> save(ADTO aDTO) {
        log.debug("Request to save A : {}", aDTO);
        return aRepository.save(aMapper.toEntity(aDTO)).map(aMapper::toDto);
    }

    @Override
    public Mono<ADTO> partialUpdate(ADTO aDTO) {
        log.debug("Request to partially update A : {}", aDTO);

        return aRepository
            .findById(aDTO.getId())
            .map(existingA -> {
                aMapper.partialUpdate(existingA, aDTO);

                return existingA;
            })
            .flatMap(aRepository::save)
            .map(aMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ADTO> findAll() {
        log.debug("Request to get all AS");
        return aRepository.findAll().map(aMapper::toDto);
    }

    public Mono<Long> countAll() {
        return aRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ADTO> findOne(Long id) {
        log.debug("Request to get A : {}", id);
        return aRepository.findById(id).map(aMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete A : {}", id);
        return aRepository.deleteById(id);
    }
}
