package com.operacional.controleoperacional.service.impl;

import com.operacional.controleoperacional.domain.TipoAmostra;
import com.operacional.controleoperacional.repository.TipoAmostraRepository;
import com.operacional.controleoperacional.service.TipoAmostraService;
import com.operacional.controleoperacional.service.dto.TipoAmostraDTO;
import com.operacional.controleoperacional.service.mapper.TipoAmostraMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TipoAmostra}.
 */
@Service
@Transactional
public class TipoAmostraServiceImpl implements TipoAmostraService {

    private final Logger log = LoggerFactory.getLogger(TipoAmostraServiceImpl.class);

    private final TipoAmostraRepository tipoAmostraRepository;

    private final TipoAmostraMapper tipoAmostraMapper;

    public TipoAmostraServiceImpl(TipoAmostraRepository tipoAmostraRepository, TipoAmostraMapper tipoAmostraMapper) {
        this.tipoAmostraRepository = tipoAmostraRepository;
        this.tipoAmostraMapper = tipoAmostraMapper;
    }

    @Override
    public TipoAmostraDTO save(TipoAmostraDTO tipoAmostraDTO) {
        log.debug("Request to save TipoAmostra : {}", tipoAmostraDTO);
        TipoAmostra tipoAmostra = tipoAmostraMapper.toEntity(tipoAmostraDTO);
        tipoAmostra = tipoAmostraRepository.save(tipoAmostra);
        return tipoAmostraMapper.toDto(tipoAmostra);
    }

    @Override
    public TipoAmostraDTO update(TipoAmostraDTO tipoAmostraDTO) {
        log.debug("Request to save TipoAmostra : {}", tipoAmostraDTO);
        TipoAmostra tipoAmostra = tipoAmostraMapper.toEntity(tipoAmostraDTO);
        tipoAmostra = tipoAmostraRepository.save(tipoAmostra);
        return tipoAmostraMapper.toDto(tipoAmostra);
    }

    @Override
    public Optional<TipoAmostraDTO> partialUpdate(TipoAmostraDTO tipoAmostraDTO) {
        log.debug("Request to partially update TipoAmostra : {}", tipoAmostraDTO);

        return tipoAmostraRepository
            .findById(tipoAmostraDTO.getId())
            .map(existingTipoAmostra -> {
                tipoAmostraMapper.partialUpdate(existingTipoAmostra, tipoAmostraDTO);

                return existingTipoAmostra;
            })
            .map(tipoAmostraRepository::save)
            .map(tipoAmostraMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoAmostraDTO> findAll() {
        log.debug("Request to get all TipoAmostras");
        return tipoAmostraRepository.findAll().stream().map(tipoAmostraMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoAmostraDTO> findOne(Long id) {
        log.debug("Request to get TipoAmostra : {}", id);
        return tipoAmostraRepository.findById(id).map(tipoAmostraMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TipoAmostra : {}", id);
        tipoAmostraRepository.deleteById(id);
    }
}
