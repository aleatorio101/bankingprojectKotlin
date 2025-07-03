package com.example.demo.service

import com.example.demo.event.TransacaoEvent
import com.example.demo.model.*
import com.example.demo.repository.ChavePixRepository
import com.example.demo.repository.ContaBaseRepository
import com.example.demo.repository.TransacaoRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*

@Service
@Transactional
class PixService(
    private val chavePixRepository: ChavePixRepository,
    private val contaBaseRepository: ContaBaseRepository,
    private val transacaoRepository: TransacaoRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val limitesService: LimitesService
) {

    fun criarChavePix(contaId: Long, chave: String, tipo: TipoChavePix): ChavePix {
        if (chavePixRepository.existsByChave(chave)) {
            throw RuntimeException("Chave PIX já cadastrada: $chave")
        }
        
        val conta = contaBaseRepository.findById(contaId)
            .orElseThrow { RuntimeException("Conta não encontrada: $contaId") }
        
        validarChavePix(chave, tipo)
        
        val chavePix = ChavePix(
            chave = chave,
            tipo = tipo,
            conta = conta
        )
        
        return chavePixRepository.save(chavePix)
    }

    fun transferirPix(chavePixDestino: String, contaOrigemNumero: String, valor: BigDecimal, descricao: String): Transacao {
        val chavePix = chavePixRepository.findByChave(chavePixDestino)
            .orElseThrow { RuntimeException("Chave PIX não encontrada: $chavePixDestino") }
        
        val contaOrigem = contaBaseRepository.findByNumero(contaOrigemNumero)
            .orElseThrow { RuntimeException("Conta origem não encontrada: $contaOrigemNumero") }
        
        val contaDestino = chavePix.conta
        
        verificarContaAtiva(contaOrigem)
        verificarContaAtiva(contaDestino)
        limitesService.verificarLimites(contaOrigem, valor)
        
        val saldoDisponivel = when (contaOrigem) {
            is ContaCorrente -> contaOrigem.saldoDisponivel()
            else -> contaOrigem.saldo
        }
        
        if (saldoDisponivel < valor) {
            throw RuntimeException("Saldo insuficiente")
        }
        
        contaOrigem.saldo = contaOrigem.saldo.subtract(valor)
        contaDestino.saldo = contaDestino.saldo.add(valor)
        
        contaBaseRepository.save(contaOrigem)
        contaBaseRepository.save(contaDestino)
        
        val transacao = Transacao(
            tipo = TipoTransacao.PIX,
            valor = valor,
            descricao = "PIX - $descricao",
            contaOrigem = contaOrigem,
            contaDestino = contaDestino
        )
        
        val transacaoSalva = transacaoRepository.save(transacao)
        eventPublisher.publishEvent(TransacaoEvent(transacaoSalva))
        
        return transacaoSalva
    }

    fun buscarChavesPorConta(contaId: Long): List<ChavePix> {
        return chavePixRepository.findByContaId(contaId)
    }

    fun gerarChaveAleatoria(): String {
        return UUID.randomUUID().toString()
    }

    private fun validarChavePix(chave: String, tipo: TipoChavePix) {
        when (tipo) {
            TipoChavePix.CPF -> {
                if (!chave.matches(Regex("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}"))) {
                    throw RuntimeException("CPF inválido para chave PIX")
                }
            }
            TipoChavePix.EMAIL -> {
                if (!chave.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$"))) {
                    throw RuntimeException("Email inválido para chave PIX")
                }
            }
            TipoChavePix.TELEFONE -> {
                if (!chave.matches(Regex("\\(\\d{2}\\)\\s\\d{4,5}-\\d{4}"))) {
                    throw RuntimeException("Telefone inválido para chave PIX")
                }
            }
            TipoChavePix.ALEATORIA -> {
                // Chave aleatória não precisa validação específica
            }
        }
    }

    private fun verificarContaAtiva(conta: ContaBase) {
        if (!conta.ativa) {
            throw RuntimeException("Conta inativa")
        }
    }
}