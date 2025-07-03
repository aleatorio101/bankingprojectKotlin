
package com.example.demo.service

import com.example.demo.event.TransacaoEvent
import com.example.demo.model.AuditoriaTransacao
import com.example.demo.repository.AuditoriaTransacaoRepository
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class NotificacaoService(
    private val mailSender: JavaMailSender,
    private val auditoriaRepository: AuditoriaTransacaoRepository
) {
    
    private val logger = LoggerFactory.getLogger(NotificacaoService::class.java)

    @EventListener
    @Async
    fun handleTransacao(evento: TransacaoEvent) {
        val transacao = evento.transacao
        
        try {
            enviarNotificacaoEmail(transacao)

            registrarAuditoria(transacao)
            
            logger.info("Notificação enviada para transação: ${transacao.id}")
        } catch (e: Exception) {
            logger.error("Erro ao enviar notificação para transação: ${transacao.id}", e)
        }
    }

    private fun enviarNotificacaoEmail(transacao: com.example.demo.model.Transacao) {
        val message = SimpleMailMessage()
        message.setTo(transacao.contaOrigem.cliente.email)
        message.setSubject("Transação Realizada - ${transacao.tipo}")
        message.setText("""
            Prezado(a) ${transacao.contaOrigem.cliente.nome},
            
            Uma transação foi realizada em sua conta:
            
            Tipo: ${transacao.tipo}
            Valor: R$ ${transacao.valor}
            Data/Hora: ${transacao.dataHora}
            Descrição: ${transacao.descricao}
            
            Saldo atual: R$ ${transacao.contaOrigem.saldo}
            
            Atenciosamente,
            Banco Digital
        """.trimIndent())
        
        mailSender.send(message)
    }

    private fun registrarAuditoria(transacao: com.example.demo.model.Transacao) {
        val auditoria = AuditoriaTransacao(
            acao = "TRANSACAO_${transacao.tipo}",
            usuarioId = 1L, // TODO: Obter do contexto de segurança
            detalhes = "Transação de ${transacao.tipo} no valor de R$ ${transacao.valor}",
            transacao = transacao
        )
        
        auditoriaRepository.save(auditoria)
    }
}
