package br.com.ada.programacaowebii.aula.repository;

import br.com.ada.programacaowebii.aula.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaRepository extends JpaRepository<Conta, Long> {

}
