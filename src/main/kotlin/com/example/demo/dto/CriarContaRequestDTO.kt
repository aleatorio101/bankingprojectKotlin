package com.example.demo.dto

import jakarta.validation.constraints.*

data class CriarContaRequest(
    @field:NotBlank(message = "Número da conta é obrigatório")
    @field:Size(min = 5, max = 20, message = "Número da conta deve ter entre 5 e 20 caracteres")
    val numero: String,

    @field:NotNull(message = "ID do cliente é obrigatório")
    val clienteId: Long,

    @field:NotBlank(message = "Tipo da conta é obrigatório")
    @field:Pattern(
        regexp = "^(SIMPLES|CORRENTE|POUPANCA|PREMIUM)$",
        message = "Tipo da conta deve ser: SIMPLES, CORRENTE, POUPANCA ou PREMIUM"
    )
    val tipoConta: String = "SIMPLES"
)
