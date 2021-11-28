package com.operacional.controleamostramicroservice.service.impl;

import com.operacional.controleamostramicroservice.domain.A;
import com.operacional.controleamostramicroservice.repository.ARepository;
import com.operacional.controleamostramicroservice.service.AService;
import com.operacional.controleamostramicroservice.service.dto.ADTO;
import com.operacional.controleamostramicroservice.service.mapper.AMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ADTO save(ADTO aDTO) {
        log.debug("Request to save A : {}", aDTO);
        A a = aMapper.toEntity(aDTO);
        a = aRepository.save(a);
        return aMapper.toDto(a);
    }

    @Override
    public Optional<ADTO> partialUpdate(ADTO aDTO) {
        log.debug("Request to partially update A : {}", aDTO);

        return aRepository
            .findById(aDTO.getId())
            .map(existingA -> {
                aMapper.partialUpdate(existingA, aDTO);

                return existingA;
            })
            .map(aRepository::save)
            .map(aMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ADTO> findAll() {
        log.debug("Request to get all AS");
        return aRepository.findAll().stream().map(aMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ADTO> findOne(Long id) {
        log.debug("Request to get A : {}", id);
        return aRepository.findById(id).map(aMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete A : {}", id);
        aRepository.deleteById(id);
    }
}
