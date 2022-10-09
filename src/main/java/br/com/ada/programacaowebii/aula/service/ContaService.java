package br.com.ada.programacaowebii.aula.service;

import br.com.ada.programacaowebii.aula.controller.vo.ContaVO;
import br.com.ada.programacaowebii.aula.model.Conta;
import br.com.ada.programacaowebii.aula.repository.ContaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ContaService {

    private ContaRepository contaRepository;

    public void criarConta(Conta conta) {
        contaRepository.save(conta);
    }

    public Optional<Conta> buscaContaPorNumero(Long numero) {
        return contaRepository.findByNumero(numero);
    }

    public void removerContaPorId(Long id) {
        contaRepository.deleteById(id);
    }

    public List<Conta> buscarContasPorClienteId(Long id) {
        return contaRepository.findAllByCliente_Id(id);
    }

    public Conta atualizaConta(ContaVO dadosAtualizados) {
        Optional<Conta> contaOptional = this.contaRepository.findByNumero(dadosAtualizados.getNumero());
        if(contaOptional.isPresent()) {
            Conta conta = contaOptional.get();
            conta.setSaldo(dadosAtualizados.getSaldo());
            conta.setDataCriacao(dadosAtualizados.getDataCriacao());
            return contaRepository.save(conta);
        }
    return null;
    }
}
