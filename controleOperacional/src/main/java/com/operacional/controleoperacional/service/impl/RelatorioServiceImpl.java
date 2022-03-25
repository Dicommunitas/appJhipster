package com.operacional.controleoperacional.service.impl;

import com.operacional.controleoperacional.domain.Relatorio;
import com.operacional.controleoperacional.repository.RelatorioRepository;
import com.operacional.controleoperacional.service.RelatorioService;
import com.operacional.controleoperacional.service.dto.RelatorioDTO;
import com.operacional.controleoperacional.service.mapper.RelatorioMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Relatorio}.
 */
@Service
@Transactional
public class RelatorioServiceImpl implements RelatorioService {

    private final Logger log = LoggerFactory.getLogger(RelatorioServiceImpl.class);

    private final RelatorioRepository relatorioRepository;

    private final RelatorioMapper relatorioMapper;

    public RelatorioServiceImpl(RelatorioRepository relatorioRepository, RelatorioMapper relatorioMapper) {
        this.relatorioRepository = relatorioRepository;
        this.relatorioMapper = relatorioMapper;
    }

    @Override
    public RelatorioDTO save(RelatorioDTO relatorioDTO) {
        log.debug("Request to save Relatorio : {}", relatorioDTO);
        Relatorio relatorio = relatorioMapper.toEntity(relatorioDTO);
        relatorio = relatorioRepository.save(relatorio);
        return relatorioMapper.toDto(relatorio);
    }

    @Override
    public Optional<RelatorioDTO> partialUpdate(RelatorioDTO relatorioDTO) {
        log.debug("Request to partially update Relatorio : {}", relatorioDTO);

        return relatorioRepository
            .findById(relatorioDTO.getId())
            .map(existingRelatorio -> {
                relatorioMapper.partialUpdate(existingRelatorio, relatorioDTO);

                return existingRelatorio;
            })
            .map(relatorioRepository::save)
            .map(relatorioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RelatorioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Relatorios");
        return relatorioRepository.findAll(pageable).map(relatorioMapper::toDto);
    }

    public Page<RelatorioDTO> findAllWithEagerRelationships(Pageable pageable) {
        return relatorioRepository.findAllWithEagerRelationships(pageable).map(relatorioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RelatorioDTO> findOne(Long id) {
        log.debug("Request to get Relatorio : {}", id);
        return relatorioRepository.findOneWithEagerRelationships(id).map(relatorioMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Relatorio : {}", id);
        relatorioRepository.deleteById(id);
    }
}
