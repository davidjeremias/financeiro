package br.com.deliverit.financeiro.repository;

import br.com.deliverit.financeiro.entity.Acrescimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcrescimoRepository extends JpaRepository<Acrescimo, Long> {

    Acrescimo findByNome(String nome);
}
