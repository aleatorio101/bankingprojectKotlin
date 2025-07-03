package com.example.demo.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
data class Transacao(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val tipo: TipoTransacao,
    val valor: BigDecimal,
    val dataHora: LocalDateTime = LocalDateTime.now(),
    val descricao: String,
    @ManyToOne val contaOrigem: ContaBase,
    @ManyToOne val contaDestino: ContaBase? = null
)