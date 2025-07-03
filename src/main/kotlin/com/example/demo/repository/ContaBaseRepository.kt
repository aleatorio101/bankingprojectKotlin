package com.example.demo.repository

import com.example.demo.model.ContaBase
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface ContaBaseRepository : JpaRepository<ContaBase, Long> {
    fun findByNumero(numero: String): Optional<ContaBase>
    fun findByClienteId(clienteId: Long): List<ContaBase>
    
    @Query("SELECT c FROM ContaBase c WHERE c.ativa = true")
    fun findAllActive(): List<ContaBase>
}