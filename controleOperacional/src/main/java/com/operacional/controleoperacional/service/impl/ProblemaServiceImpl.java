package com.operacional.controleoperacional.service.impl;

import com.operacional.controleoperacional.domain.Problema;
import com.operacional.controleoperacional.repository.ProblemaRepository;
import com.operacional.controleoperacional.service.ProblemaService;
import com.operacional.controleoperacional.service.dto.ProblemaDTO;
import com.operacional.controleoperacional.service.mapper.ProblemaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Problema}.
 */
@Service
@Transactional
public class ProblemaServiceImpl implements ProblemaService {

    private final Logger log = LoggerFactory.getLogger(ProblemaServiceImpl.class);

    private final ProblemaRepository problemaRepository;

    private final ProblemaMapper problemaMapper;

    public ProblemaServiceImpl(ProblemaRepository problemaRepository, ProblemaMapper problemaMapper) {
        this.problemaRepository = problemaRepository;
        this.problemaMapper = problemaMapper;
    }

    @Override
    public ProblemaDTO save(ProblemaDTO problemaDTO) {
        log.debug("Request to save Problema : {}", problemaDTO);
        Problema problema = problemaMapper.toEntity(problemaDTO);
        problema = problemaRepository.save(problema);
        return problemaMapper.toDto(problema);
    }

    @Override
    public Optional<ProblemaDTO> partialUpdate(ProblemaDTO problemaDTO) {
        log.debug("Request to partially update Problema : {}", problemaDTO);

        return problemaRepository
            .findById(problemaDTO.getId())
            .map(existingProblema -> {
                problemaMapper.partialUpdate(existingProblema, problemaDTO);

                return existingProblema;
            })
            .map(problemaRepository::save)
            .map(problemaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProblemaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Problemas");
        return problemaRepository.findAll(pageable).map(problemaMapper::toDto);
    }

    public Page<ProblemaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return problemaRepository.findAllWithEagerRelationships(pageable).map(problemaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProblemaDTO> findOne(Long id) {
        log.debug("Request to get Problema : {}", id);
        return problemaRepository.findOneWithEagerRelationships(id).map(problemaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Problema : {}", id);
        problemaRepository.deleteById(id);
    }
}
