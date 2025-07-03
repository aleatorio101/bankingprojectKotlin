package com.example.demo.repository

import com.example.demo.model.Cliente
import org.springframework.data.jpa.repository.JpaRepository

interface ClienteRepository : JpaRepository<Cliente, Long> {
    fun existsByCpf(cpf: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun findByCpf(cpf: String): Cliente?
    fun findByEmail(email: String): Cliente?
}