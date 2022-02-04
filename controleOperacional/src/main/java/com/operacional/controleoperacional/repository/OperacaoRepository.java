package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.Operacao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Operacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OperacaoRepository extends JpaRepository<Operacao, Long>, JpaSpecificationExecutor<Operacao> {}
