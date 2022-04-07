package com.operacional.controleoperacional.repository;

import com.operacional.controleoperacional.domain.Produto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ProdutoRepositoryWithBagRelationships {
    Optional<Produto> fetchBagRelationships(Optional<Produto> produto);

    List<Produto> fetchBagRelationships(List<Produto> produtos);

    Page<Produto> fetchBagRelationships(Page<Produto> produtos);
}
