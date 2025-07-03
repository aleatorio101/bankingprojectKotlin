package com.example.demo.model

import jakarta.persistence.*
import jakarta.validation.Valid
import jakarta.validation.constraints.*
import java.time.LocalDateTime

@Entity
data class Cliente(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @field:NotBlank(message = "Nome é obrigatório")
    @field:Size(min = 2, max = 100)
    val nome: String,
    
    @field:NotBlank(message = "CPF é obrigatório")
    @field:Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF deve ter formato 000.000.000-00")
    @Column(unique = true)
    val cpf: String,
    
    @field:Email(message = "Email inválido")
    @field:NotBlank(message = "Email é obrigatório")
    @Column(unique = true)
    val email: String,
    
    @field:Pattern(regexp = "\\(\\d{2}\\)\\s\\d{4,5}-\\d{4}", message = "Telefone deve ter formato (00) 00000-0000")
    val telefone: String,
    
    @Valid
    @Embedded
    val endereco: Endereco,
    
    val dataCadastro: LocalDateTime = LocalDateTime.now(),
    
    var ativo: Boolean = true,
    
    @OneToMany(mappedBy = "cliente", cascade = [CascadeType.ALL])
    val contas: List<ContaBase> = mutableListOf()
)