package com.example.demo.dto.response

data class ClienteResponseDTO(
    val id: Long,
    val nome: String,
    val cpf: String,
    val email: String,
    val telefone: String,
    val endereco: EnderecoResponseDTO,
    val contas: List<ContaSimpleResponseDTO> = emptyList()
)

data class ClienteSimpleResponseDTO(
    val id: Long,
    val nome: String,
    val cpf: String,
    val email: String
)