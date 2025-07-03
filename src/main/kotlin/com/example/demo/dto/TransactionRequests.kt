package com.example.demo.dto

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class TransacaoRequest(
    @field:NotBlank(message = "Número da conta é obrigatório")
    val numeroConta: String,
    
    @field:NotNull(message = "Valor é obrigatório")
    @field:DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    val valor: BigDecimal,
    
    val descricao: String = ""
)

data class TransferenciaRequest(
    @field:NotBlank(message = "Conta origem é obrigatória")
    val contaOrigem: String,
    
    @field:NotBlank(message = "Conta destino é obrigatória")
    val contaDestino: String,
    
    @field:NotNull(message = "Valor é obrigatório")
    @field:DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    val valor: BigDecimal,
    
    val descricao: String = ""
)

data class PixRequest(
    @field:NotBlank(message = "Chave PIX é obrigatória")
    val chavePix: String,
    
    @field:NotBlank(message = "Conta origem é obrigatória")
    val contaOrigem: String,
    
    @field:NotNull(message = "Valor é obrigatório")
    @field:DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    val valor: BigDecimal,
    
    val descricao: String = ""
)
