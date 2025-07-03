package com.example.demo.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class ChavePix(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @Column(unique = true)
    val chave: String,
    
    @Enumerated(EnumType.STRING)
    val tipo: TipoChavePix,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id")
    val conta: ContaBase,
    
    val dataCriacao: LocalDateTime = LocalDateTime.now(),
    
    var ativa: Boolean = true
)

enum class TipoChavePix {
    CPF, EMAIL, TELEFONE, ALEATORIA
}
