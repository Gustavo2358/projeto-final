package br.com.ada.programacaowebii.aula.controller;

import br.com.ada.programacaowebii.aula.controller.dto.ContaDTO;
import br.com.ada.programacaowebii.aula.controller.vo.ContaVO;
import br.com.ada.programacaowebii.aula.model.Cliente;
import br.com.ada.programacaowebii.aula.model.Conta;
import br.com.ada.programacaowebii.aula.service.ClienteService;
import br.com.ada.programacaowebii.aula.service.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@SecurityRequirement(name="conta-bancaria-api")
@AllArgsConstructor
public class ContaController {

    private ContaService contaService;
    private ClienteService clienteService;

    @Operation(summary = "Criar conta", tags = "Conta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ContaDTO.class))
                    }
            )

    })
    @PostMapping("/conta")
    public ResponseEntity<String> criarConta(@Valid @RequestHeader(value = "cpf") String cpf, @RequestBody ContaVO contaVO) {
        Optional<Cliente> cliente = clienteService.buscarClientePorCpf(cpf);
        if(cliente.isPresent()) {
            Conta conta = new Conta();
            conta.setNumero(contaVO.getNumero());
            conta.setDataCriacao(contaVO.getDataCriacao());
            conta.setSaldo(contaVO.getSaldo());
            conta.setCliente(cliente.get());
            contaService.criarConta(conta);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Atualizar conta", tags = "Conta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ContaDTO.class))
                    }
            )

    })
    @PutMapping("/conta")
    public ResponseEntity<ContaDTO> atualizarConta(@Valid @RequestBody ContaVO contaVO) {
        //TODO - atualizar conta
        Conta conta  = this.contaService.atualizaConta(contaVO);
        if(Objects.nonNull(conta)){
            ContaDTO contaDTO = new ContaDTO();
            contaDTO.setNumero(conta.getNumero());
            contaDTO.setDataCriacao(conta.getDataCriacao());
            contaDTO.setSaldo(conta.getSaldo());
            return ResponseEntity.ok(contaDTO);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Buscar conta pelo numero", tags = "Conta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ContaDTO.class))
                    }
            )

    })
    @GetMapping("/conta-por-numero/{numero}")
    public ResponseEntity<ContaDTO> buscarContaPeloNumero(@PathVariable("numero") Long numero) {
        Optional<Conta> optionalConta = this.contaService.buscaContaPorNumero(numero);
        if(optionalConta.isPresent()){
            Conta conta = optionalConta.get();
            ContaDTO contaDTO = new ContaDTO();
            contaDTO.setNumero(conta.getNumero());
            contaDTO.setDataCriacao(conta.getDataCriacao());
            contaDTO.setSaldo(conta.getSaldo());
            return ResponseEntity.ok(contaDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Remover conta pelo numero", tags = "Conta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ContaDTO.class))
                    }
            )

    })
    @DeleteMapping("/conta-por-numero/{numero}")
    public ResponseEntity<String> removerContaPeloNumero(@PathVariable("numero") Long numero) {
        Optional<Conta> optionalConta = this.contaService.buscaContaPorNumero(numero);
        if (optionalConta.isPresent()) {
            Conta conta = optionalConta.get();
            this.contaService.removerContaPorId(conta.getId());
            return ResponseEntity.ok("Conta removida!");
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Listar contas pelo cpf do cliente", tags = "Conta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ContaDTO.class))
                    }
            )

    })
    @GetMapping("/contas-por-cpf/{cpf}")
    public ResponseEntity<List<ContaDTO>> listarContasPorCpf(@PathVariable("cpf") String cpf) {
        Optional<Cliente> clienteOptional = clienteService.buscarClientePorCpf(cpf);
        if (clienteOptional.isPresent()){
            Cliente cliente = clienteOptional.get();
            List<Conta> contas = contaService.buscarContasPorClienteId(cliente.getId());
            List<ContaDTO> contasDTO = contas.stream().map(conta -> {
                ContaDTO contaDTO = new ContaDTO();
                contaDTO.setNumero(conta.getNumero());
                contaDTO.setDataCriacao(conta.getDataCriacao());
                contaDTO.setSaldo(conta.getSaldo());
                return contaDTO;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(contasDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
