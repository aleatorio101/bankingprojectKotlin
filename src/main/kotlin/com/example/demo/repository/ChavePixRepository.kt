package com.example.demo.repository

import com.example.demo.model.ChavePix
import com.example.demo.model.TipoChavePix
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ChavePixRepository : JpaRepository<ChavePix, Long> {
    fun findByChave(chave: String): Optional<ChavePix>
    fun findByContaId(contaId: Long): List<ChavePix>
    fun findByTipo(tipo: TipoChavePix): List<ChavePix>
    fun existsByChave(chave: String): Boolean
}