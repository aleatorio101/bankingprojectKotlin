package com.example.demo.dto.response

import com.example.demo.model.*

fun Cliente.toResponseDTO(): ClienteResponseDTO {
    return ClienteResponseDTO(
        id = this.id,
        nome = this.nome,
        cpf = this.cpf,
        email = this.email,
        telefone = this.telefone,
        endereco = this.endereco.toResponseDTO(),
        contas = this.contas.map { it.toSimpleResponseDTO() }
    )
}

fun Cliente.toSimpleResponseDTO(): ClienteSimpleResponseDTO {
    return ClienteSimpleResponseDTO(
        id = this.id,
        nome = this.nome,
        cpf = this.cpf,
        email = this.email
    )
}

fun Endereco.toResponseDTO(): EnderecoResponseDTO {
    return EnderecoResponseDTO(
        logradouro = this.logradouro,
        numero = this.numero,
        complemento = this.complemento,
        bairro = this.bairro,
        cidade = this.cidade,
        estado = this.estado,
        cep = this.cep
    )
}

fun ContaBase.toResponseDTO(): ContaResponseDTO {
    return ContaResponseDTO(
        id = this.id,
        numero = this.numero,
        saldo = this.saldo,
        tipoConta = this.getTipoConta(),
        cliente = this.cliente.toSimpleResponseDTO(),
        dataCriacao = this.dataCriacao,
        limiteChequeEspecial = if (this is ContaCorrente) this.limiteChequeEspecial else null,
        taxaRendimento = if (this is ContaPoupanca) this.taxaRendimento else null
    )
}

fun ContaBase.toSimpleResponseDTO(): ContaSimpleResponseDTO {
    return ContaSimpleResponseDTO(
        id = this.id,
        numero = this.numero,
        saldo = this.saldo,
        tipoConta = this.getTipoConta(),
        dataCriacao = this.dataCriacao
    )
}

fun ContaBase.getTipoConta(): String {
    return when (this) {
        is ContaCorrente -> "CORRENTE"
        is ContaPoupanca -> "POUPANCA"
        else -> "SIMPLES"
    }
}