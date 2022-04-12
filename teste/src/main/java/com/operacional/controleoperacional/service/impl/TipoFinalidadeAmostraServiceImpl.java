package com.operacional.controleoperacional.service.impl;

import com.operacional.controleoperacional.domain.TipoFinalidadeAmostra;
import com.operacional.controleoperacional.repository.TipoFinalidadeAmostraRepository;
import com.operacional.controleoperacional.service.TipoFinalidadeAmostraService;
import com.operacional.controleoperacional.service.dto.TipoFinalidadeAmostraDTO;
import com.operacional.controleoperacional.service.mapper.TipoFinalidadeAmostraMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TipoFinalidadeAmostra}.
 */
@Service
@Transactional
public class TipoFinalidadeAmostraServiceImpl implements TipoFinalidadeAmostraService {

    private final Logger log = LoggerFactory.getLogger(TipoFinalidadeAmostraServiceImpl.class);

    private final TipoFinalidadeAmostraRepository tipoFinalidadeAmostraRepository;

    private final TipoFinalidadeAmostraMapper tipoFinalidadeAmostraMapper;

    public TipoFinalidadeAmostraServiceImpl(
        TipoFinalidadeAmostraRepository tipoFinalidadeAmostraRepository,
        TipoFinalidadeAmostraMapper tipoFinalidadeAmostraMapper
    ) {
        this.tipoFinalidadeAmostraRepository = tipoFinalidadeAmostraRepository;
        this.tipoFinalidadeAmostraMapper = tipoFinalidadeAmostraMapper;
    }

    @Override
    public TipoFinalidadeAmostraDTO save(TipoFinalidadeAmostraDTO tipoFinalidadeAmostraDTO) {
        log.debug("Request to save TipoFinalidadeAmostra : {}", tipoFinalidadeAmostraDTO);
        TipoFinalidadeAmostra tipoFinalidadeAmostra = tipoFinalidadeAmostraMapper.toEntity(tipoFinalidadeAmostraDTO);
        tipoFinalidadeAmostra = tipoFinalidadeAmostraRepository.save(tipoFinalidadeAmostra);
        return tipoFinalidadeAmostraMapper.toDto(tipoFinalidadeAmostra);
    }

    @Override
    public TipoFinalidadeAmostraDTO update(TipoFinalidadeAmostraDTO tipoFinalidadeAmostraDTO) {
        log.debug("Request to save TipoFinalidadeAmostra : {}", tipoFinalidadeAmostraDTO);
        TipoFinalidadeAmostra tipoFinalidadeAmostra = tipoFinalidadeAmostraMapper.toEntity(tipoFinalidadeAmostraDTO);
        tipoFinalidadeAmostra = tipoFinalidadeAmostraRepository.save(tipoFinalidadeAmostra);
        return tipoFinalidadeAmostraMapper.toDto(tipoFinalidadeAmostra);
    }

    @Override
    public Optional<TipoFinalidadeAmostraDTO> partialUpdate(TipoFinalidadeAmostraDTO tipoFinalidadeAmostraDTO) {
        log.debug("Request to partially update TipoFinalidadeAmostra : {}", tipoFinalidadeAmostraDTO);

        return tipoFinalidadeAmostraRepository
            .findById(tipoFinalidadeAmostraDTO.getId())
            .map(existingTipoFinalidadeAmostra -> {
                tipoFinalidadeAmostraMapper.partialUpdate(existingTipoFinalidadeAmostra, tipoFinalidadeAmostraDTO);

                return existingTipoFinalidadeAmostra;
            })
            .map(tipoFinalidadeAmostraRepository::save)
            .map(tipoFinalidadeAmostraMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoFinalidadeAmostraDTO> findAll() {
        log.debug("Request to get all TipoFinalidadeAmostras");
        return tipoFinalidadeAmostraRepository
            .findAll()
            .stream()
            .map(tipoFinalidadeAmostraMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoFinalidadeAmostraDTO> findOne(Long id) {
        log.debug("Request to get TipoFinalidadeAmostra : {}", id);
        return tipoFinalidadeAmostraRepository.findById(id).map(tipoFinalidadeAmostraMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TipoFinalidadeAmostra : {}", id);
        tipoFinalidadeAmostraRepository.deleteById(id);
    }
}
