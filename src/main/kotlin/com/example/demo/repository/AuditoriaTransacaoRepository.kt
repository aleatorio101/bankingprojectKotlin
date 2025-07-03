package com.example.demo.repository

import com.example.demo.model.AuditoriaTransacao
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface AuditoriaTransacaoRepository : JpaRepository<AuditoriaTransacao, Long> {
    fun findByUsuarioId(usuarioId: Long): List<AuditoriaTransacao>
    
    @Query("SELECT a FROM AuditoriaTransacao a WHERE a.dataHora BETWEEN :inicio AND :fim")
    fun findByPeriodo(inicio: LocalDateTime, fim: LocalDateTime): List<AuditoriaTransacao>
}