package com.example.demo.model

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
data class LimiteTransacao(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    val limiteDiario: BigDecimal,
    val limiteMensal: BigDecimal,
    val limitePorTransacao: BigDecimal,
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id")
    val conta: ContaBase
)
