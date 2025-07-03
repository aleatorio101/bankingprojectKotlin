package com.example.demo.dto.response

data class EnderecoResponseDTO(
    val logradouro: String,
    val numero: String,
    val complemento: String?,
    val bairro: String,
    val cidade: String,
    val estado: String,
    val cep: String
)
