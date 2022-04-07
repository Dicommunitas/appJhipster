package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.TipoRelatorio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface TipoRelatorioRepositoryWithBagRelationships {
    Optional<TipoRelatorio> fetchBagRelationships(Optional<TipoRelatorio> tipoRelatorio);

    List<TipoRelatorio> fetchBagRelationships(List<TipoRelatorio> tipoRelatorios);

    Page<TipoRelatorio> fetchBagRelationships(Page<TipoRelatorio> tipoRelatorios);
}
