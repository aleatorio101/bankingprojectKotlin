package com.example.demo.controller

import com.example.demo.dto.PixRequest
import com.example.demo.dto.TransacaoRequest
import com.example.demo.dto.TransferenciaRequest
import com.example.demo.dto.response.ContaResponseDTO
import com.example.demo.dto.response.toResponseDTO
import com.example.demo.model.ContaBase
import com.example.demo.model.Transacao
import com.example.demo.service.ContaService
import com.example.demo.service.PixService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/contas")
@Tag(name = "Contas", description = "Operações relacionadas a contas bancárias")
class ContaRestController(
    private val contaService: ContaService,
    private val pixService: PixService
) {

    @PostMapping("/{numero}/deposito")
    @Operation(summary = "Realizar depósito")
    @PreAuthorize("hasRole('CLIENTE') or hasRole('GERENTE')")
    fun depositar(
        @PathVariable numero: String,
        @Valid @RequestBody request: TransacaoRequest
    ): ResponseEntity<ContaBase> {
        val conta = contaService.depositar(numero, request.valor)
        return ResponseEntity.ok(conta)
    }

    @PostMapping("/{numero}/saque")
    @Operation(summary = "Realizar saque")
    @PreAuthorize("hasRole('CLIENTE') or hasRole('GERENTE')")
    fun sacar(
        @PathVariable numero: String,
        @Valid @RequestBody request: TransacaoRequest
    ): ResponseEntity<ContaBase> {
        val conta = contaService.sacar(numero, request.valor)
        return ResponseEntity.ok(conta)
    }

    @PostMapping("/transferencia")
    @Operation(summary = "Realizar transferência")
    @PreAuthorize("hasRole('CLIENTE') or hasRole('GERENTE')")
    fun transferir(@Valid @RequestBody request: TransferenciaRequest): ResponseEntity<ContaBase> {
        val conta = contaService.transferir(request.contaOrigem, request.contaDestino, request.valor)
        return ResponseEntity.ok(conta)
    }

    @PostMapping("/pix")
    @Operation(summary = "Realizar PIX")
    @PreAuthorize("hasRole('CLIENTE') or hasRole('GERENTE')")
    fun pix(@Valid @RequestBody request: PixRequest): ResponseEntity<Transacao> {
        val transacao = pixService.transferirPix(request.chavePix, request.contaOrigem, request.valor, request.descricao)
        return ResponseEntity.ok(transacao)
    }

    @GetMapping("/{numero}")
    @Operation(summary = "Buscar conta por número")
    @PreAuthorize("hasRole('CLIENTE') or hasRole('GERENTE') or hasRole('ADMIN')")
    fun buscarConta(@PathVariable numero: String): ResponseEntity<ContaResponseDTO?> {
        val conta = contaService.buscarContaPorNumero(numero)
        return ResponseEntity.ok(conta.toResponseDTO())
    }
}
