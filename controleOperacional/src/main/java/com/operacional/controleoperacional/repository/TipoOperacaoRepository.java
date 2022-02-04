package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.TipoOperacao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TipoOperacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoOperacaoRepository extends JpaRepository<TipoOperacao, Long> {}
