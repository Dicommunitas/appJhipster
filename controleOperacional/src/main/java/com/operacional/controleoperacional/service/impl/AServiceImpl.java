package com.operacional.controleoperacional.service.impl;

import com.operacional.controleoperacional.domain.A;
import com.operacional.controleoperacional.repository.ARepository;
import com.operacional.controleoperacional.service.AService;
import java.util.List;
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

    public AServiceImpl(ARepository aRepository) {
        this.aRepository = aRepository;
    }

    @Override
    public Mono<A> save(A a) {
        log.debug("Request to save A : {}", a);
        return aRepository.save(a);
    }

    @Override
    public Mono<A> partialUpdate(A a) {
        log.debug("Request to partially update A : {}", a);

        return aRepository
            .findById(a.getId())
            .map(existingA -> {
                return existingA;
            })
            .flatMap(aRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<A> findAll() {
        log.debug("Request to get all AS");
        return aRepository.findAll();
    }

    public Mono<Long> countAll() {
        return aRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<A> findOne(Long id) {
        log.debug("Request to get A : {}", id);
        return aRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete A : {}", id);
        return aRepository.deleteById(id);
    }
}
