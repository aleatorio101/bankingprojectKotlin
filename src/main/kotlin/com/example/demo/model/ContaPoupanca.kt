package com.example.demo.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@DiscriminatorValue("POUPANCA")
data class ContaPoupanca(
    override val numero: String,
    override val cliente: Cliente,
    val taxaRendimento: BigDecimal = BigDecimal("0.005"),
    override val id: Long = 0,
    override var saldo: BigDecimal = BigDecimal.ZERO,
    override var ativa: Boolean = true,
    override val dataCriacao: LocalDateTime = LocalDateTime.now()
) : ContaBase(id, numero, saldo, ativa, dataCriacao, cliente) {
    
    fun calcularRendimento(): BigDecimal = saldo.multiply(taxaRendimento)
}