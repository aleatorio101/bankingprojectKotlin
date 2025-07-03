package com.example.demo.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@DiscriminatorValue("SIMPLES")
data class Conta(
    override val numero: String,
    override val cliente: Cliente,
    override val id: Long = 0,
    override var saldo: BigDecimal = BigDecimal.ZERO,
    override var ativa: Boolean = true,
    override val dataCriacao: LocalDateTime = LocalDateTime.now()
) : ContaBase(id, numero, saldo, ativa, dataCriacao, cliente)