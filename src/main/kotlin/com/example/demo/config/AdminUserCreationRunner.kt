package com.example.demo.config

import com.example.demo.model.Usuario
import com.example.demo.model.Role
import com.example.demo.repository.UsuarioRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DataLoader(
    private val usuarioRepository: UsuarioRepository,
    private val passwordEncoder: PasswordEncoder
) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        try {

            if (!usuarioRepository.existsByUsername("admin")) {
                val admin = Usuario(
                    username = "admin",
                    password = passwordEncoder.encode("admin123"),
                    role = Role.ADMIN
                )
                usuarioRepository.save(admin)
                println("Usuário admin criado com sucesso! (username: admin, password: admin123)")
            }

            if (!usuarioRepository.existsByUsername("gerente")) {
                val gerente = Usuario(
                    username = "gerente",
                    password = passwordEncoder.encode("gerente123"),
                    role = Role.GERENTE
                )
                usuarioRepository.save(gerente)
                println("Usuário gerente criado com sucesso! (username: gerente, password: gerente123)")
            }

            if (!usuarioRepository.existsByUsername("user")) {
                val user = Usuario(
                    username = "user",
                    password = passwordEncoder.encode("user123"),
                    role = Role.CLIENTE
                )
                usuarioRepository.save(user)
                println("Usuário comum criado com sucesso! (username: user, password: user123)")
            }
        } catch (e: Exception) {
            println("Erro ao criar usuários iniciais: ${e.message}")
        }
    }
}