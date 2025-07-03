package com.example.demo.controller

import com.example.demo.model.LimiteTransacao
import com.example.demo.service.LimitesService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/api/v1/limites")
@Tag(name = "Limites", description = "Operações relacionadas a limites de transação")
class LimitesController(
    private val limitesService: LimitesService
) {

    @PostMapping("/conta/{contaId}")
    @Operation(summary = "Definir limites de transação para uma conta")
    @PreAuthorize("hasRole('GERENTE') or hasRole('ADMIN')")
    fun definirLimites(
        @PathVariable contaId: Long,
        @RequestParam limiteDiario: BigDecimal,
        @RequestParam limiteMensal: BigDecimal,
        @RequestParam limitePorTransacao: BigDecimal
    ): ResponseEntity<LimiteTransacao> {
        val limite = limitesService.definirLimites(contaId, limiteDiario, limiteMensal, limitePorTransacao)
        return ResponseEntity.ok(limite)
    }
}
