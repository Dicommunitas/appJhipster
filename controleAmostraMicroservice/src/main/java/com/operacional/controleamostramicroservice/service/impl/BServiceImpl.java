package com.operacional.controleamostramicroservice.service.impl;

import com.operacional.controleamostramicroservice.domain.B;
import com.operacional.controleamostramicroservice.repository.BRepository;
import com.operacional.controleamostramicroservice.service.BService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public B save(B b) {
        log.debug("Request to save B : {}", b);
        return bRepository.save(b);
    }

    @Override
    public Optional<B> partialUpdate(B b) {
        log.debug("Request to partially update B : {}", b);

        return bRepository
            .findById(b.getId())
            .map(existingB -> {
                return existingB;
            })
            .map(bRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<B> findAll() {
        log.debug("Request to get all BS");
        return bRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<B> findOne(Long id) {
        log.debug("Request to get B : {}", id);
        return bRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete B : {}", id);
        bRepository.deleteById(id);
    }
}
