package com.operacional.controleoperacional.service.impl;

import com.operacional.controleoperacional.domain.Lembrete;
import com.operacional.controleoperacional.repository.LembreteRepository;
import com.operacional.controleoperacional.service.LembreteService;
import com.operacional.controleoperacional.service.dto.LembreteDTO;
import com.operacional.controleoperacional.service.mapper.LembreteMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Lembrete}.
 */
@Service
@Transactional
public class LembreteServiceImpl implements LembreteService {

    private final Logger log = LoggerFactory.getLogger(LembreteServiceImpl.class);

    private final LembreteRepository lembreteRepository;

    private final LembreteMapper lembreteMapper;

    public LembreteServiceImpl(LembreteRepository lembreteRepository, LembreteMapper lembreteMapper) {
        this.lembreteRepository = lembreteRepository;
        this.lembreteMapper = lembreteMapper;
    }

    @Override
    public LembreteDTO save(LembreteDTO lembreteDTO) {
        log.debug("Request to save Lembrete : {}", lembreteDTO);
        Lembrete lembrete = lembreteMapper.toEntity(lembreteDTO);
        lembrete = lembreteRepository.save(lembrete);
        return lembreteMapper.toDto(lembrete);
    }

    @Override
    public Optional<LembreteDTO> partialUpdate(LembreteDTO lembreteDTO) {
        log.debug("Request to partially update Lembrete : {}", lembreteDTO);

        return lembreteRepository
            .findById(lembreteDTO.getId())
            .map(existingLembrete -> {
                lembreteMapper.partialUpdate(existingLembrete, lembreteDTO);

                return existingLembrete;
            })
            .map(lembreteRepository::save)
            .map(lembreteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LembreteDTO> findAll() {
        log.debug("Request to get all Lembretes");
        return lembreteRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(lembreteMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<LembreteDTO> findAllWithEagerRelationships(Pageable pageable) {
        return lembreteRepository.findAllWithEagerRelationships(pageable).map(lembreteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LembreteDTO> findOne(Long id) {
        log.debug("Request to get Lembrete : {}", id);
        return lembreteRepository.findOneWithEagerRelationships(id).map(lembreteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Lembrete : {}", id);
        lembreteRepository.deleteById(id);
    }
}
