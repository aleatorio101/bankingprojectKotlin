package com.example.demo.dto.response

import java.math.BigDecimal
import java.time.LocalDateTime

data class ContaResponseDTO(
    val id: Long,
    val numero: String,
    val saldo: BigDecimal,
    val tipoConta: String,
    val cliente: ClienteSimpleResponseDTO,
    val dataCriacao: LocalDateTime,
    val limiteChequeEspecial: BigDecimal? = null,
    val taxaRendimento: BigDecimal? = null,
    val limiteMensal: BigDecimal? = null
)

data class ContaSimpleResponseDTO(
    val id: Long,
    val numero: String,
    val saldo: BigDecimal,
    val tipoConta: String,
    val dataCriacao: LocalDateTime
)