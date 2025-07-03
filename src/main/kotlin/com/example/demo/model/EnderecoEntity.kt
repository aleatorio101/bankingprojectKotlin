package com.example.demo.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@Embeddable
data class Endereco(
    @field:NotBlank(message = "Logradouro é obrigatório")
    @field:Size(max = 255)
    val logradouro: String,
    
    @field:NotBlank(message = "Número é obrigatório")
    @field:Size(max = 10)
    val numero: String,
    
    @field:Size(max = 255)
    val complemento: String? = null,
    
    @field:NotBlank(message = "Bairro é obrigatório")
    @field:Size(max = 100)
    val bairro: String,
    
    @field:NotBlank(message = "Cidade é obrigatória")
    @field:Size(max = 100)
    val cidade: String,
    
    @field:NotBlank(message = "Estado é obrigatório")
    @field:Size(min = 2, max = 2)
    val estado: String,
    
    @field:NotBlank(message = "CEP é obrigatório")
    @field:Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP deve ter formato 00000-000")
    val cep: String
)
