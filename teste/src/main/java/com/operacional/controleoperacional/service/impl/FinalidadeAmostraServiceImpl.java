package com.operacional.controleoperacional.service.impl;

import com.operacional.controleoperacional.domain.FinalidadeAmostra;
import com.operacional.controleoperacional.repository.FinalidadeAmostraRepository;
import com.operacional.controleoperacional.service.FinalidadeAmostraService;
import com.operacional.controleoperacional.service.dto.FinalidadeAmostraDTO;
import com.operacional.controleoperacional.service.mapper.FinalidadeAmostraMapper;
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
 * Service Implementation for managing {@link FinalidadeAmostra}.
 */
@Service
@Transactional
public class FinalidadeAmostraServiceImpl implements FinalidadeAmostraService {

    private final Logger log = LoggerFactory.getLogger(FinalidadeAmostraServiceImpl.class);

    private final FinalidadeAmostraRepository finalidadeAmostraRepository;

    private final FinalidadeAmostraMapper finalidadeAmostraMapper;

    public FinalidadeAmostraServiceImpl(
        FinalidadeAmostraRepository finalidadeAmostraRepository,
        FinalidadeAmostraMapper finalidadeAmostraMapper
    ) {
        this.finalidadeAmostraRepository = finalidadeAmostraRepository;
        this.finalidadeAmostraMapper = finalidadeAmostraMapper;
    }

    @Override
    public FinalidadeAmostraDTO save(FinalidadeAmostraDTO finalidadeAmostraDTO) {
        log.debug("Request to save FinalidadeAmostra : {}", finalidadeAmostraDTO);
        FinalidadeAmostra finalidadeAmostra = finalidadeAmostraMapper.toEntity(finalidadeAmostraDTO);
        finalidadeAmostra = finalidadeAmostraRepository.save(finalidadeAmostra);
        return finalidadeAmostraMapper.toDto(finalidadeAmostra);
    }

    @Override
    public FinalidadeAmostraDTO update(FinalidadeAmostraDTO finalidadeAmostraDTO) {
        log.debug("Request to save FinalidadeAmostra : {}", finalidadeAmostraDTO);
        FinalidadeAmostra finalidadeAmostra = finalidadeAmostraMapper.toEntity(finalidadeAmostraDTO);
        finalidadeAmostra = finalidadeAmostraRepository.save(finalidadeAmostra);
        return finalidadeAmostraMapper.toDto(finalidadeAmostra);
    }

    @Override
    public Optional<FinalidadeAmostraDTO> partialUpdate(FinalidadeAmostraDTO finalidadeAmostraDTO) {
        log.debug("Request to partially update FinalidadeAmostra : {}", finalidadeAmostraDTO);

        return finalidadeAmostraRepository
            .findById(finalidadeAmostraDTO.getId())
            .map(existingFinalidadeAmostra -> {
                finalidadeAmostraMapper.partialUpdate(existingFinalidadeAmostra, finalidadeAmostraDTO);

                return existingFinalidadeAmostra;
            })
            .map(finalidadeAmostraRepository::save)
            .map(finalidadeAmostraMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinalidadeAmostraDTO> findAll() {
        log.debug("Request to get all FinalidadeAmostras");
        return finalidadeAmostraRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(finalidadeAmostraMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<FinalidadeAmostraDTO> findAllWithEagerRelationships(Pageable pageable) {
        return finalidadeAmostraRepository.findAllWithEagerRelationships(pageable).map(finalidadeAmostraMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FinalidadeAmostraDTO> findOne(Long id) {
        log.debug("Request to get FinalidadeAmostra : {}", id);
        return finalidadeAmostraRepository.findOneWithEagerRelationships(id).map(finalidadeAmostraMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FinalidadeAmostra : {}", id);
        finalidadeAmostraRepository.deleteById(id);
    }
}
