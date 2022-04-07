package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.AlertaProduto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AlertaProduto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlertaProdutoRepository extends JpaRepository<AlertaProduto, Long> {}
