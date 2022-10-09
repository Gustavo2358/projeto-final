package br.com.ada.programacaowebii.aula.repository;

import br.com.ada.programacaowebii.aula.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    Optional<Conta> findByNumero(Long numero);

    List<Conta> findAllByCliente_Id(Long id);
}
