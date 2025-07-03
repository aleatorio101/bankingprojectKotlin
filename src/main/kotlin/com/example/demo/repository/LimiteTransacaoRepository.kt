package com.example.demo.repository

import com.example.demo.model.LimiteTransacao
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface LimiteTransacaoRepository : JpaRepository<LimiteTransacao, Long> {
    fun findByContaId(contaId: Long): Optional<LimiteTransacao>
    fun existsByContaId(contaId: Long): Boolean
    fun deleteByContaId(contaId: Long)
}