package com.operacional.myapp2.service.impl;

import com.operacional.myapp2.domain.C;
import com.operacional.myapp2.repository.CRepository;
import com.operacional.myapp2.service.CService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public C save(C c) {
        log.debug("Request to save C : {}", c);
        return cRepository.save(c);
    }

    @Override
    public Optional<C> partialUpdate(C c) {
        log.debug("Request to partially update C : {}", c);

        return cRepository
            .findById(c.getId())
            .map(existingC -> {
                return existingC;
            })
            .map(cRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<C> findAll() {
        log.debug("Request to get all CS");
        return cRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<C> findOne(Long id) {
        log.debug("Request to get C : {}", id);
        return cRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete C : {}", id);
        cRepository.deleteById(id);
    }
}
