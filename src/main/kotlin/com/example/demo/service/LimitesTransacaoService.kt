package com.example.demo.service

import com.example.demo.model.ContaBase
import com.example.demo.model.LimiteTransacao
import com.example.demo.repository.ContaBaseRepository
import com.example.demo.repository.LimiteTransacaoRepository
import com.example.demo.repository.TransacaoRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class LimitesService(
    private val limiteRepository: LimiteTransacaoRepository,
    private val transacaoRepository: TransacaoRepository,
    private val contaBaseRepository: ContaBaseRepository
) {

    fun verificarLimites(conta: ContaBase, valor: BigDecimal) {
        val limite = limiteRepository.findByContaId(conta.id)
            .orElse(criarLimitePadrao(conta))
        
        // Verificar limite por transação
        if (valor > limite.limitePorTransacao) {
            throw RuntimeException("Valor excede o limite por transação: ${limite.limitePorTransacao}")
        }
        
        // Verificar limite diário
        val inicioHoje = LocalDateTime.now().toLocalDate().atStartOfDay()
        val fimHoje = inicioHoje.plusDays(1)
        val totalHoje = transacaoRepository.sumValorByContaOrigemAndDataHoraBetween(conta, inicioHoje, fimHoje) 
            ?: BigDecimal.ZERO
        
        if (totalHoje.add(valor) > limite.limiteDiario) {
            throw RuntimeException("Valor excede o limite diário: ${limite.limiteDiario}")
        }
        
        // Verificar limite mensal
        val inicioMes = LocalDateTime.now().toLocalDate().withDayOfMonth(1).atStartOfDay()
        val fimMes = inicioMes.plusMonths(1)
        val totalMes = transacaoRepository.sumValorByContaOrigemAndDataHoraBetween(conta, inicioMes, fimMes)
            ?: BigDecimal.ZERO
        
        if (totalMes.add(valor) > limite.limiteMensal) {
            throw RuntimeException("Valor excede o limite mensal: ${limite.limiteMensal}")
        }
    }

    fun definirLimites(contaId: Long, limiteDiario: BigDecimal, limiteMensal: BigDecimal, limitePorTransacao: BigDecimal): LimiteTransacao {
        val conta = contaBaseRepository.findById(contaId)
            .orElseThrow { RuntimeException("Conta não encontrada: $contaId") }
        
        val limiteExistente = limiteRepository.findByContaId(contaId)
            .orElse(null)
        
        val limite = if (limiteExistente != null) {
            // Como LimiteTransacao é data class, precisamos criar uma nova instância
            LimiteTransacao(
                id = limiteExistente.id,
                limiteDiario = limiteDiario,
                limiteMensal = limiteMensal,
                limitePorTransacao = limitePorTransacao,
                conta = conta
            )
        } else {
            LimiteTransacao(
                limiteDiario = limiteDiario,
                limiteMensal = limiteMensal,
                limitePorTransacao = limitePorTransacao,
                conta = conta
            )
        }
        
        return limiteRepository.save(limite)
    }

    private fun criarLimitePadrao(conta: ContaBase): LimiteTransacao {
        val limitePadrao = LimiteTransacao(
            limiteDiario = BigDecimal("5000.00"),
            limiteMensal = BigDecimal("50000.00"),
            limitePorTransacao = BigDecimal("1000.00"),
            conta = conta
        )
        return limiteRepository.save(limitePadrao)
    }
}