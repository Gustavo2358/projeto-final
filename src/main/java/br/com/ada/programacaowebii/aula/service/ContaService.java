package br.com.ada.programacaowebii.aula.service;

import br.com.ada.programacaowebii.aula.model.Conta;
import br.com.ada.programacaowebii.aula.repository.ContaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ContaService {

    private ContaRepository contaRepository;

    public void criarConta(Conta conta) {
        contaRepository.save(conta);
    }
}
