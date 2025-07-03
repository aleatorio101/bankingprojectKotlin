package com.example.demo.controller

import com.example.demo.dto.CriarClienteRequest
import com.example.demo.dto.CriarContaRequest
import com.example.demo.dto.response.ContaResponseDTO
import com.example.demo.model.Cliente
import com.example.demo.service.ContaService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/clientes")
@Tag(name = "Clientes", description = "Operações relacionadas a clientes")
class ClienteController(
    private val contaService: ContaService
) {

    @PostMapping
    @Operation(summary = "Criar novo cliente")
    @PreAuthorize("hasRole('GERENTE') or hasRole('ADMIN')")
    fun criarCliente(
        @Valid @RequestBody request: CriarClienteRequest
    ): ResponseEntity<Cliente> {
        val cliente = contaService.criarCliente(
            nome = request.nome,
            cpf = request.cpf,
            email = request.email,
            telefone = request.telefone,
            endereco = request.endereco
        )
        return ResponseEntity.ok(cliente)
    }

    @PostMapping("/{clienteId}/contas")
    @Operation(summary = "Criar nova conta para cliente")
    @PreAuthorize("hasRole('GERENTE') or hasRole('ADMIN')")
    fun criarConta(
        @PathVariable clienteId: Long,
        @Valid @RequestBody request: CriarContaRequest
    ): ResponseEntity<ContaResponseDTO?> {
        val conta = contaService.criarConta(
            numero = request.numero,
            clienteId = clienteId,
            tipoConta = request.tipoConta
        )
        return ResponseEntity.ok(conta)
    }
}