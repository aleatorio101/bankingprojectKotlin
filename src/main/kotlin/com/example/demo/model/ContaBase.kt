package com.example.demo.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "conta")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_conta", discriminatorType = DiscriminatorType.STRING)
abstract class ContaBase(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long = 0,
    
    @Column(unique = true)
    open val numero: String,
    
    open var saldo: BigDecimal = BigDecimal.ZERO,
    
    open var ativa: Boolean = true,
    
    open val dataCriacao: LocalDateTime = LocalDateTime.now(),
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    open val cliente: Cliente,
    
    @OneToMany(mappedBy = "contaOrigem", cascade = [CascadeType.ALL])
    open val transacoesOrigem: List<Transacao> = mutableListOf(),
    
    @OneToMany(mappedBy = "contaDestino", cascade = [CascadeType.ALL])
    open val transacoesDestino: List<Transacao> = mutableListOf(),
    
    @OneToMany(mappedBy = "conta", cascade = [CascadeType.ALL])
    open val chavesPix: List<ChavePix> = mutableListOf()
)
