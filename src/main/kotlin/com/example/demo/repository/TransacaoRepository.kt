package com.example.demo.repository

import com.example.demo.model.ContaBase
import com.example.demo.model.Transacao
import com.example.demo.model.TipoTransacao
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface TransacaoRepository : JpaRepository<Transacao, Long> {
    fun findByContaOrigemOrderByDataHoraDesc(conta: ContaBase): List<Transacao>
    fun findByContaDestinoOrderByDataHoraDesc(conta: ContaBase): List<Transacao>
    fun findByContaOrigemAndDataHoraBetween(conta: ContaBase, inicio: LocalDateTime, fim: LocalDateTime): List<Transacao>
    fun findByTipo(tipo: TipoTransacao): List<Transacao>
    
    @Query("SELECT t FROM Transacao t WHERE t.contaOrigem = :conta OR t.contaDestino = :conta ORDER BY t.dataHora DESC")
    fun findAllByContaOrderByDataHoraDesc(@Param("conta") conta: ContaBase): List<Transacao>
    
    @Query("SELECT SUM(t.valor) FROM Transacao t WHERE t.contaOrigem = :conta AND t.dataHora BETWEEN :inicio AND :fim")
    fun sumValorByContaOrigemAndDataHoraBetween(@Param("conta") conta: ContaBase, @Param("inicio") inicio: LocalDateTime, @Param("fim") fim: LocalDateTime): java.math.BigDecimal?
}