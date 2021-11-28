package com.operacional.controleamostragateway.service.impl;

import com.operacional.controleamostragateway.domain.B;
import com.operacional.controleamostragateway.repository.BRepository;
import com.operacional.controleamostragateway.service.BService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link B}.
 */
@Service
@Transactional
public class BServiceImpl implements BService {

    private final Logger log = LoggerFactory.getLogger(BServiceImpl.class);

    private final BRepository bRepository;

    public BServiceImpl(BRepository bRepository) {
        this.bRepository = bRepository;
    }

    @Override
    public Mono<B> save(B b) {
        log.debug("Request to save B : {}", b);
        return bRepository.save(b);
    }

    @Override
    public Mono<B> partialUpdate(B b) {
        log.debug("Request to partially update B : {}", b);

        return bRepository
            .findById(b.getId())
            .map(existingB -> {
                return existingB;
            })
            .flatMap(bRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<B> findAll() {
        log.debug("Request to get all BS");
        return bRepository.findAll();
    }

    public Mono<Long> countAll() {
        return bRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<B> findOne(Long id) {
        log.debug("Request to get B : {}", id);
        return bRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete B : {}", id);
        return bRepository.deleteById(id);
    }
}
