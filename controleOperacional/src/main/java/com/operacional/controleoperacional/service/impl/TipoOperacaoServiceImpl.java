package com.operacional.controleoperacional.service.impl;

import com.operacional.controleoperacional.domain.TipoOperacao;
import com.operacional.controleoperacional.repository.TipoOperacaoRepository;
import com.operacional.controleoperacional.service.TipoOperacaoService;
import com.operacional.controleoperacional.service.dto.TipoOperacaoDTO;
import com.operacional.controleoperacional.service.mapper.TipoOperacaoMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TipoOperacao}.
 */
@Service
@Transactional
public class TipoOperacaoServiceImpl implements TipoOperacaoService {

    private final Logger log = LoggerFactory.getLogger(TipoOperacaoServiceImpl.class);

    private final TipoOperacaoRepository tipoOperacaoRepository;

    private final TipoOperacaoMapper tipoOperacaoMapper;

    public TipoOperacaoServiceImpl(TipoOperacaoRepository tipoOperacaoRepository, TipoOperacaoMapper tipoOperacaoMapper) {
        this.tipoOperacaoRepository = tipoOperacaoRepository;
        this.tipoOperacaoMapper = tipoOperacaoMapper;
    }

    @Override
    public TipoOperacaoDTO save(TipoOperacaoDTO tipoOperacaoDTO) {
        log.debug("Request to save TipoOperacao : {}", tipoOperacaoDTO);
        TipoOperacao tipoOperacao = tipoOperacaoMapper.toEntity(tipoOperacaoDTO);
        tipoOperacao = tipoOperacaoRepository.save(tipoOperacao);
        return tipoOperacaoMapper.toDto(tipoOperacao);
    }

    @Override
    public TipoOperacaoDTO update(TipoOperacaoDTO tipoOperacaoDTO) {
        log.debug("Request to save TipoOperacao : {}", tipoOperacaoDTO);
        TipoOperacao tipoOperacao = tipoOperacaoMapper.toEntity(tipoOperacaoDTO);
        tipoOperacao = tipoOperacaoRepository.save(tipoOperacao);
        return tipoOperacaoMapper.toDto(tipoOperacao);
    }

    @Override
    public Optional<TipoOperacaoDTO> partialUpdate(TipoOperacaoDTO tipoOperacaoDTO) {
        log.debug("Request to partially update TipoOperacao : {}", tipoOperacaoDTO);

        return tipoOperacaoRepository
            .findById(tipoOperacaoDTO.getId())
            .map(existingTipoOperacao -> {
                tipoOperacaoMapper.partialUpdate(existingTipoOperacao, tipoOperacaoDTO);

                return existingTipoOperacao;
            })
            .map(tipoOperacaoRepository::save)
            .map(tipoOperacaoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoOperacaoDTO> findAll() {
        log.debug("Request to get all TipoOperacaos");
        return tipoOperacaoRepository.findAll().stream().map(tipoOperacaoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoOperacaoDTO> findOne(Long id) {
        log.debug("Request to get TipoOperacao : {}", id);
        return tipoOperacaoRepository.findById(id).map(tipoOperacaoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TipoOperacao : {}", id);
        tipoOperacaoRepository.deleteById(id);
    }
}
