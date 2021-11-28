package com.operacional.controleamostragateway.service.impl;

import com.operacional.controleamostragateway.domain.D;
import com.operacional.controleamostragateway.repository.DRepository;
import com.operacional.controleamostragateway.service.DService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link D}.
 */
@Service
@Transactional
public class DServiceImpl implements DService {

    private final Logger log = LoggerFactory.getLogger(DServiceImpl.class);

    private final DRepository dRepository;

    public DServiceImpl(DRepository dRepository) {
        this.dRepository = dRepository;
    }

    @Override
    public Mono<D> save(D d) {
        log.debug("Request to save D : {}", d);
        return dRepository.save(d);
    }

    @Override
    public Mono<D> partialUpdate(D d) {
        log.debug("Request to partially update D : {}", d);

        return dRepository
            .findById(d.getId())
            .map(existingD -> {
                return existingD;
            })
            .flatMap(dRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<D> findAll() {
        log.debug("Request to get all DS");
        return dRepository.findAll();
    }

    public Mono<Long> countAll() {
        return dRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<D> findOne(Long id) {
        log.debug("Request to get D : {}", id);
        return dRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete D : {}", id);
        return dRepository.deleteById(id);
    }
}
