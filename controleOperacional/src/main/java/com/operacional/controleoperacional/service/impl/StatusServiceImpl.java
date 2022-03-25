package com.operacional.controleoperacional.service.impl;

import com.operacional.controleoperacional.domain.Status;
import com.operacional.controleoperacional.repository.StatusRepository;
import com.operacional.controleoperacional.service.StatusService;
import com.operacional.controleoperacional.service.dto.StatusDTO;
import com.operacional.controleoperacional.service.mapper.StatusMapper;
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
 * Service Implementation for managing {@link Status}.
 */
@Service
@Transactional
public class StatusServiceImpl implements StatusService {

    private final Logger log = LoggerFactory.getLogger(StatusServiceImpl.class);

    private final StatusRepository statusRepository;

    private final StatusMapper statusMapper;

    public StatusServiceImpl(StatusRepository statusRepository, StatusMapper statusMapper) {
        this.statusRepository = statusRepository;
        this.statusMapper = statusMapper;
    }

    @Override
    public StatusDTO save(StatusDTO statusDTO) {
        log.debug("Request to save Status : {}", statusDTO);
        Status status = statusMapper.toEntity(statusDTO);
        status = statusRepository.save(status);
        return statusMapper.toDto(status);
    }

    @Override
    public Optional<StatusDTO> partialUpdate(StatusDTO statusDTO) {
        log.debug("Request to partially update Status : {}", statusDTO);

        return statusRepository
            .findById(statusDTO.getId())
            .map(existingStatus -> {
                statusMapper.partialUpdate(existingStatus, statusDTO);

                return existingStatus;
            })
            .map(statusRepository::save)
            .map(statusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatusDTO> findAll() {
        log.debug("Request to get all Statuses");
        return statusRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(statusMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<StatusDTO> findAllWithEagerRelationships(Pageable pageable) {
        return statusRepository.findAllWithEagerRelationships(pageable).map(statusMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StatusDTO> findOne(Long id) {
        log.debug("Request to get Status : {}", id);
        return statusRepository.findOneWithEagerRelationships(id).map(statusMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Status : {}", id);
        statusRepository.deleteById(id);
    }
}
