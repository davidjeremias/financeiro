package br.com.deliverit.financeiro.repository;

import br.com.deliverit.financeiro.entity.ContaApagar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContaApagarRepository extends JpaRepository<ContaApagar, Long> {
}
