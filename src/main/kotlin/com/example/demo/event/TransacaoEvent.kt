package com.example.demo.event

import com.example.demo.model.Transacao
import org.springframework.context.ApplicationEvent

class TransacaoEvent(val transacao: Transacao) : ApplicationEvent(transacao)
