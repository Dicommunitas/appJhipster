package com.operacional.controleoperacional.service.impl;

import com.operacional.controleoperacional.domain.OrigemAmostra;
import com.operacional.controleoperacional.repository.OrigemAmostraRepository;
import com.operacional.controleoperacional.service.OrigemAmostraService;
import com.operacional.controleoperacional.service.dto.OrigemAmostraDTO;
import com.operacional.controleoperacional.service.mapper.OrigemAmostraMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrigemAmostra}.
 */
@Service
@Transactional
public class OrigemAmostraServiceImpl implements OrigemAmostraService {

    private final Logger log = LoggerFactory.getLogger(OrigemAmostraServiceImpl.class);

    private final OrigemAmostraRepository origemAmostraRepository;

    private final OrigemAmostraMapper origemAmostraMapper;

    public OrigemAmostraServiceImpl(OrigemAmostraRepository origemAmostraRepository, OrigemAmostraMapper origemAmostraMapper) {
        this.origemAmostraRepository = origemAmostraRepository;
        this.origemAmostraMapper = origemAmostraMapper;
    }

    @Override
    public OrigemAmostraDTO save(OrigemAmostraDTO origemAmostraDTO) {
        log.debug("Request to save OrigemAmostra : {}", origemAmostraDTO);
        OrigemAmostra origemAmostra = origemAmostraMapper.toEntity(origemAmostraDTO);
        origemAmostra = origemAmostraRepository.save(origemAmostra);
        return origemAmostraMapper.toDto(origemAmostra);
    }

    @Override
    public OrigemAmostraDTO update(OrigemAmostraDTO origemAmostraDTO) {
        log.debug("Request to save OrigemAmostra : {}", origemAmostraDTO);
        OrigemAmostra origemAmostra = origemAmostraMapper.toEntity(origemAmostraDTO);
        origemAmostra = origemAmostraRepository.save(origemAmostra);
        return origemAmostraMapper.toDto(origemAmostra);
    }

    @Override
    public Optional<OrigemAmostraDTO> partialUpdate(OrigemAmostraDTO origemAmostraDTO) {
        log.debug("Request to partially update OrigemAmostra : {}", origemAmostraDTO);

        return origemAmostraRepository
            .findById(origemAmostraDTO.getId())
            .map(existingOrigemAmostra -> {
                origemAmostraMapper.partialUpdate(existingOrigemAmostra, origemAmostraDTO);

                return existingOrigemAmostra;
            })
            .map(origemAmostraRepository::save)
            .map(origemAmostraMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrigemAmostraDTO> findAll() {
        log.debug("Request to get all OrigemAmostras");
        return origemAmostraRepository.findAll().stream().map(origemAmostraMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrigemAmostraDTO> findOne(Long id) {
        log.debug("Request to get OrigemAmostra : {}", id);
        return origemAmostraRepository.findById(id).map(origemAmostraMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrigemAmostra : {}", id);
        origemAmostraRepository.deleteById(id);
    }
}
