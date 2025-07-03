package com.example.demo.dto

import com.example.demo.model.Endereco
import jakarta.validation.Valid
import jakarta.validation.constraints.*

data class CriarClienteRequest(
    @field:NotBlank(message = "Nome é obrigatório")
    @field:Size(min = 2, max = 100)
    val nome: String,
    
    @field:NotBlank(message = "CPF é obrigatório")
    @field:Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve ter formato 000.000.000-00")
    val cpf: String,
    
    @field:Email(message = "Email inválido")
    @field:NotBlank(message = "Email é obrigatório")
    val email: String,
    
    @field:Pattern(regexp = "\\(\\d{2}\\)\\s\\d{4,5}-\\d{4}", message = "Telefone deve ter formato (00) 00000-0000")
    val telefone: String,
    
    @field:Valid
    val endereco: Endereco
)
