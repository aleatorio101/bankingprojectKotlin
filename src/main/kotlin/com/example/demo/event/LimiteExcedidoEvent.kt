package com.example.demo.event

import com.example.demo.model.ContaBase
import org.springframework.context.ApplicationEvent
import java.math.BigDecimal

class LimiteExcedidoEvent(
    val conta: ContaBase,
    val valorTentativa: BigDecimal,
    val tipoLimite: String,
    val valorLimite: BigDecimal
) : ApplicationEvent(conta)
