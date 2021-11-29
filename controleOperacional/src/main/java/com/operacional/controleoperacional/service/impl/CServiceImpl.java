package com.operacional.controleoperacional.service.impl;

import com.operacional.controleoperacional.domain.C;
import com.operacional.controleoperacional.repository.CRepository;
import com.operacional.controleoperacional.service.CService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link C}.
 */
@Service
@Transactional
public class CServiceImpl implements CService {

    private final Logger log = LoggerFactory.getLogger(CServiceImpl.class);

    private final CRepository cRepository;

    public CServiceImpl(CRepository cRepository) {
        this.cRepository = cRepository;
    }

    @Override
    public Mono<C> save(C c) {
        log.debug("Request to save C : {}", c);
        return cRepository.save(c);
    }

    @Override
    public Mono<C> partialUpdate(C c) {
        log.debug("Request to partially update C : {}", c);

        return cRepository
            .findById(c.getId())
            .map(existingC -> {
                return existingC;
            })
            .flatMap(cRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<C> findAll() {
        log.debug("Request to get all CS");
        return cRepository.findAll();
    }

    public Mono<Long> countAll() {
        return cRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<C> findOne(Long id) {
        log.debug("Request to get C : {}", id);
        return cRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete C : {}", id);
        return cRepository.deleteById(id);
    }
}
