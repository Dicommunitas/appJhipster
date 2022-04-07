package com.operacional.controleoperacional.service.impl;

import com.operacional.controleoperacional.domain.TipoRelatorio;
import com.operacional.controleoperacional.repository.TipoRelatorioRepository;
import com.operacional.controleoperacional.service.TipoRelatorioService;
import com.operacional.controleoperacional.service.dto.TipoRelatorioDTO;
import com.operacional.controleoperacional.service.mapper.TipoRelatorioMapper;
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
 * Service Implementation for managing {@link TipoRelatorio}.
 */
@Service
@Transactional
public class TipoRelatorioServiceImpl implements TipoRelatorioService {

    private final Logger log = LoggerFactory.getLogger(TipoRelatorioServiceImpl.class);

    private final TipoRelatorioRepository tipoRelatorioRepository;

    private final TipoRelatorioMapper tipoRelatorioMapper;

    public TipoRelatorioServiceImpl(TipoRelatorioRepository tipoRelatorioRepository, TipoRelatorioMapper tipoRelatorioMapper) {
        this.tipoRelatorioRepository = tipoRelatorioRepository;
        this.tipoRelatorioMapper = tipoRelatorioMapper;
    }

    @Override
    public TipoRelatorioDTO save(TipoRelatorioDTO tipoRelatorioDTO) {
        log.debug("Request to save TipoRelatorio : {}", tipoRelatorioDTO);
        TipoRelatorio tipoRelatorio = tipoRelatorioMapper.toEntity(tipoRelatorioDTO);
        tipoRelatorio = tipoRelatorioRepository.save(tipoRelatorio);
        return tipoRelatorioMapper.toDto(tipoRelatorio);
    }

    @Override
    public TipoRelatorioDTO update(TipoRelatorioDTO tipoRelatorioDTO) {
        log.debug("Request to save TipoRelatorio : {}", tipoRelatorioDTO);
        TipoRelatorio tipoRelatorio = tipoRelatorioMapper.toEntity(tipoRelatorioDTO);
        tipoRelatorio = tipoRelatorioRepository.save(tipoRelatorio);
        return tipoRelatorioMapper.toDto(tipoRelatorio);
    }

    @Override
    public Optional<TipoRelatorioDTO> partialUpdate(TipoRelatorioDTO tipoRelatorioDTO) {
        log.debug("Request to partially update TipoRelatorio : {}", tipoRelatorioDTO);

        return tipoRelatorioRepository
            .findById(tipoRelatorioDTO.getId())
            .map(existingTipoRelatorio -> {
                tipoRelatorioMapper.partialUpdate(existingTipoRelatorio, tipoRelatorioDTO);

                return existingTipoRelatorio;
            })
            .map(tipoRelatorioRepository::save)
            .map(tipoRelatorioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoRelatorioDTO> findAll() {
        log.debug("Request to get all TipoRelatorios");
        return tipoRelatorioRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(tipoRelatorioMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<TipoRelatorioDTO> findAllWithEagerRelationships(Pageable pageable) {
        return tipoRelatorioRepository.findAllWithEagerRelationships(pageable).map(tipoRelatorioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoRelatorioDTO> findOne(Long id) {
        log.debug("Request to get TipoRelatorio : {}", id);
        return tipoRelatorioRepository.findOneWithEagerRelationships(id).map(tipoRelatorioMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TipoRelatorio : {}", id);
        tipoRelatorioRepository.deleteById(id);
    }
}
