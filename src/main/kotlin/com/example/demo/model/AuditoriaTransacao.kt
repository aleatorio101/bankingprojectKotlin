
package com.example.demo.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class AuditoriaTransacao(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    val acao: String,
    val usuarioId: Long,
    val dataHora: LocalDateTime = LocalDateTime.now(),
    val detalhes: String,
    val ip: String? = null,
    val userAgent: String? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transacao_id")
    val transacao: Transacao? = null
)
