package com.example.demo.service

import com.example.demo.dto.response.ContaResponseDTO
import com.example.demo.dto.response.toResponseDTO
import com.example.demo.event.TransacaoEvent
import com.example.demo.model.*
import com.example.demo.repository.*
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
@Transactional
class ContaService(
    private val contaBaseRepository: ContaBaseRepository,
    private val clienteRepository: ClienteRepository,
    private val transacaoRepository: TransacaoRepository,
    private val eventPublisher: ApplicationEventPublisher,
    private val limitesService: LimitesService
) {

    @Cacheable("contas")
    fun buscarContaPorNumero(numero: String): ContaBase {
        return contaBaseRepository.findByNumero(numero)
            .orElseThrow { RuntimeException("Conta não encontrada: $numero") }
    }

    @CacheEvict("contas", allEntries = true)
    fun depositar(numeroConta: String, valor: BigDecimal): ContaBase {
        validarValor(valor)
        
        val conta = buscarContaPorNumero(numeroConta)
        verificarContaAtiva(conta)
        
        conta.saldo = conta.saldo.add(valor)
        val contaSalva = contaBaseRepository.save(conta)
        
        val transacao = criarTransacao(TipoTransacao.DEPOSITO, valor, "Depósito", conta, null)
        eventPublisher.publishEvent(TransacaoEvent(transacao))
        
        return contaSalva
    }

    @CacheEvict("contas", allEntries = true)
    fun sacar(numeroConta: String, valor: BigDecimal): ContaBase {
        validarValor(valor)
        
        val conta = buscarContaPorNumero(numeroConta)
        verificarContaAtiva(conta)
        limitesService.verificarLimites(conta, valor)
        
        val saldoDisponivel = when (conta) {
            is ContaCorrente -> conta.saldoDisponivel()
            else -> conta.saldo
        }
        
        if (saldoDisponivel < valor) {
            throw RuntimeException("Saldo insuficiente")
        }
        
        conta.saldo = conta.saldo.subtract(valor)
        val contaSalva = contaBaseRepository.save(conta)
        
        val transacao = criarTransacao(TipoTransacao.SAQUE, valor, "Saque", conta, null)
        eventPublisher.publishEvent(TransacaoEvent(transacao))
        
        return contaSalva
    }

    @CacheEvict("contas", allEntries = true)
    fun transferir(origemNumero: String, destinoNumero: String, valor: BigDecimal): ContaBase {
        validarValor(valor)
        
        val contaOrigem = buscarContaPorNumero(origemNumero)
        val contaDestino = buscarContaPorNumero(destinoNumero)
        
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
        
        val transacao = criarTransacao(TipoTransacao.TRANSFERENCIA, valor, "Transferência", contaOrigem, contaDestino)
        eventPublisher.publishEvent(TransacaoEvent(transacao))
        
        return contaOrigem
    }

    fun criarCliente(nome: String, cpf: String, email: String, telefone: String, endereco: Endereco): Cliente {
        if (clienteRepository.existsByCpf(cpf)) {
            throw RuntimeException("CPF já cadastrado: $cpf")
        }
        if (clienteRepository.existsByEmail(email)) {
            throw RuntimeException("Email já cadastrado: $email")
        }
        
        val cliente = Cliente(
            nome = nome,
            cpf = cpf,
            email = email,
            telefone = telefone,
            endereco = endereco
        )
        
        return clienteRepository.save(cliente)
    }

    fun criarConta(numero: String, clienteId: Long, tipoConta: String = "SIMPLES"): ContaResponseDTO {
        if (contaBaseRepository.findByNumero(numero).isPresent) {
            throw RuntimeException("Número de conta já existe: $numero")
        }

        val cliente = clienteRepository.findById(clienteId)
            .orElseThrow { RuntimeException("Cliente não encontrado: $clienteId") }

        val conta = when (tipoConta.uppercase()) {
            "CORRENTE" -> ContaCorrente(
                numero = numero,
                cliente = cliente,
                limiteChequeEspecial = BigDecimal("1000.00")
            )
            "POUPANCA" -> ContaPoupanca(
                numero = numero,
                cliente = cliente,
                taxaRendimento = BigDecimal("0.005")
            )
            else -> Conta(numero = numero, cliente = cliente)
        }

        val contaSalva = contaBaseRepository.save(conta)

        return contaSalva.toResponseDTO()
    }

    private fun validarValor(valor: BigDecimal) {
        if (valor <= BigDecimal.ZERO) {
            throw RuntimeException("Valor deve ser maior que zero")
        }
    }

    private fun verificarContaAtiva(conta: ContaBase) {
        if (!conta.ativa) {
            throw RuntimeException("Conta inativa")
        }
    }

    private fun criarTransacao(
        tipo: TipoTransacao,
        valor: BigDecimal,
        descricao: String,
        contaOrigem: ContaBase,
        contaDestino: ContaBase?
    ): Transacao {
        val transacao = Transacao(
            tipo = tipo,
            valor = valor,
            descricao = descricao,
            contaOrigem = contaOrigem,
            contaDestino = contaDestino
        )
        return transacaoRepository.save(transacao)
    }
}